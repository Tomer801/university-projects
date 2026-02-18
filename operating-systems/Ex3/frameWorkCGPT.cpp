#include "MapReduceFramework.h"
#include "Barrier.h"
#include <thread>
#include <atomic>
#include <mutex>
#include <vector>
#include <map>
#include <queue>
#include <algorithm>
#include <iostream>
#include <cstdlib>

struct ThreadContext;

struct JobContext {
    const MapReduceClient& client;
    const InputVec& inputVec;
    OutputVec& outputVec;
    int threadCount;
    std::vector<std::thread> threads;
    std::vector<ThreadContext*> threadContexts;
    std::vector<IntermediateVec> intermediateVectors;
    std::vector<IntermediateVec> shuffledVectors;
    std::atomic<int> atomicInputCounter;
    std::atomic<int> atomicOutputCounter;
    std::atomic<int> atomicIntermidiatePairs;
    std::atomic<uint64_t> atomicJobState;
    std::mutex outputMutex;
    std::mutex shuffleMutex;
    std::mutex joinMutex;
    std::mutex stateMutex;
    bool joined = false;

    Barrier* barrier;
    JobContext(const MapReduceClient& client, const InputVec& inputVec, OutputVec& outputVec, int threadCount);
};

struct ThreadContext {
    int threadID;
    JobContext* job;
    IntermediateVec intermediateVec; // per-thread intermediate vector
};

JobContext::JobContext(const MapReduceClient& client, const InputVec& inputVec, OutputVec& outputVec, int threadCount)
    : client(client), inputVec(inputVec), outputVec(outputVec), threadCount(threadCount),
      atomicInputCounter(0), atomicOutputCounter(0), atomicIntermidiatePairs(0), atomicJobState(0) {
    barrier = new Barrier(threadCount);
}

void emit2(K2* key, V2* value, void* context) {
    ThreadContext* tc = static_cast<ThreadContext*>(context);
    tc->intermediateVec.push_back({key, value});
    tc->job->atomicIntermidiatePairs++;
}

void emit3(K3* key, V3* value, void* context) {
    ThreadContext* tc = static_cast<ThreadContext*>(context);
    std::lock_guard<std::mutex> lock(tc->job->outputMutex);
    tc->job->outputVec.push_back({key, value});
    tc->job->atomicOutputCounter++;
}

static void runMap(ThreadContext* tc) {
    JobContext* job = tc->job;
    int index;
    while ((index = job->atomicInputCounter++) < job->inputVec.size()) {
        InputPair pair = job->inputVec[index];
        job->client.map(pair.first, pair.second, tc);
    }
    std::sort(tc->intermediateVec.begin(), tc->intermediateVec.end(),
              [](const IntermediatePair& a, const IntermediatePair& b) { return *a.first < *b.first; });
    job->barrier->barrier();
    if (tc->threadID == 0) {
        std::vector<std::vector<IntermediatePair>*> sources;
        for (ThreadContext* context : job->threadContexts) {
            std::reverse(context->intermediateVec.begin(), context->intermediateVec.end());
            sources.push_back(&context->intermediateVec);
        }
        while (true) {
            K2* maxKey = nullptr;
            for (auto& vec : sources) {
                if (!vec->empty()) {
                    if (!maxKey || *maxKey < *vec->back().first) {
                        maxKey = vec->back().first;
                    }
                }
            }
            if (!maxKey) break;
            IntermediateVec group;
            for (auto& vec : sources) {
                while (!vec->empty() && !(*vec->back().first < *maxKey) && !(*maxKey < *vec->back().first)) {
                    group.push_back(vec->back());
                    vec->pop_back();
                }
            }
            job->shuffledVectors.push_back(std::move(group));
        }
    }
    job->barrier->barrier();
    while (true) {
        IntermediateVec group;
        {
            std::lock_guard<std::mutex> lock(job->shuffleMutex);
            if (job->shuffledVectors.empty()) return;
            group = std::move(job->shuffledVectors.back());
            job->shuffledVectors.pop_back();
        }
        job->client.reduce(&group, tc);
    }
}

JobHandle startMapReduceJob(const MapReduceClient& client, const InputVec& inputVec,
                            OutputVec& outputVec, int multiThreadLevel) {
    JobContext* job = new JobContext(client, inputVec, outputVec, multiThreadLevel);
    for (int i = 0; i < multiThreadLevel; ++i) {
        ThreadContext* tc = new ThreadContext{i, job};
        job->threadContexts.push_back(tc);
        job->threads.emplace_back(runMap, tc);
    }
    return static_cast<JobHandle>(job);
}

void waitForJob(JobHandle jobHandle) {
    JobContext* job = static_cast<JobContext*>(jobHandle);
    std::lock_guard<std::mutex> lock(job->joinMutex);
    if (!job->joined) {
        for (auto& thread : job->threads) {
            thread.join();
        }
        job->joined = true;
    }
}

void getJobState(JobHandle job, JobState* state) {
    JobContext* job_context = static_cast<JobContext*>(job);
    int total = job_context->inputVec.size();
    int done = job_context->atomicInputCounter.load();
    state->stage = MAP_STAGE;
    state->percentage = 100.0f * done / total;
    if (!job_context->shuffledVectors.empty()) {
        state->stage = SHUFFLE_STAGE;
        state->percentage = 0; // could be more precise
    } else if (job_context->atomicOutputCounter > 0) {
        state->stage = REDUCE_STAGE;
        state->percentage = 100.0f * job_context->atomicOutputCounter / job_context->shuffledVectors.size();
    }
}

void closeJobHandle(JobHandle jobHandle) {
    JobContext* job = static_cast<JobContext*>(jobHandle);
    waitForJob(jobHandle);
    for (ThreadContext* tc : job->threadContexts) delete tc;
    delete job->barrier;
    delete job;
}
