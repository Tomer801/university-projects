#include "MapReduceFramework.h"
#include "Barrier.h"
#include <thread>
#include <atomic>
#include <mutex>
#include <vector>
#include <algorithm>
#include <cstdlib>

struct ThreadContext {
    int threadID;
    struct JobContext* job;
    std::vector<std::pair<K2*, V2*>> intermediateVec;
};

struct JobContext {
    const MapReduceClient& client;
    const InputVec& inputVec;
    OutputVec& outputVec;
    int threadCount;
    std::vector<std::thread> threads;
    std::vector<ThreadContext*> threadContexts;
    std::vector<IntermediateVec> shuffledVectors;
    std::atomic<int> atomicInputCounter;
    std::atomic<int> atomicOutputCounter;
    std::atomic<int> atomicIntermidiatePairs;
    std::atomic<int> atomicVectorToReduce;
    std::atomic<int> atomicThread;
    std::atomic<stage_t> currentStage;
    std::mutex outputMutex;
    std::mutex shuffleMutex;
    std::mutex joinMutex;
    std::mutex stateMutex;
    bool joined = false;
    Barrier* barrier;

    JobContext(const MapReduceClient& client, const InputVec& inputVec, OutputVec& outputVec, int threadCount)
        : client(client), inputVec(inputVec), outputVec(outputVec), threadCount(threadCount),
          atomicInputCounter(0), atomicOutputCounter(0),
          atomicIntermidiatePairs(0), atomicVectorToReduce(0), atomicThread(0),
          currentStage(MAP_STAGE) {
        barrier = new Barrier(threadCount);
    }
};

void emit2(K2* key, V2* value, void* context) {
    ThreadContext* tc = static_cast<ThreadContext*>(context);
    tc->intermediateVec.push_back({std::move(key), std::move(value)});
    tc->job->atomicIntermidiatePairs++;
}

void emit3(K3* key, V3* value, void* context) {
    auto* tc = static_cast<ThreadContext*>(context);
    {
        std::lock_guard<std::mutex> lock(tc->job->outputMutex);
        tc->job->outputVec.push_back({std::move(key), std::move(value)});
    }
    tc->job->atomicOutputCounter++;
}

void handelMapPhase(ThreadContext* tc) {
    JobContext* job = tc->job;
    while (true) {
        int index = job->atomicInputCounter.fetch_add(1);
        if (index >= job->inputVec.size()) break;

        InputPair pair = job->inputVec[index];
        job->client.map(pair.first, pair.second, tc);
    }

    if (tc->threadID == 0) {
        job->currentStage = SHUFFLE_STAGE;
    }
}

void handelSort(ThreadContext* tc) {
    std::sort(tc->intermediateVec.begin(), tc->intermediateVec.end(),
              [](const IntermediatePair& a, const IntermediatePair& b) {
                  return *a.first < *b.first;
              });
}
// #include <queue>
//
// // עוטף מקור (vector) עם האינדקס של המקור והזוג
// struct HeapNode {
//     int sourceIdx;
//     std::pair<K2*, V2*> pair;
//
//     bool operator>(const HeapNode& other) const {
//         return *(pair.first) > *(other.pair.first);
//     }
// };
//
// void handelShufflePhase(ThreadContext* tc) {
//     JobContext* job = tc->job;
//     if (tc->threadID != 0) return;
//
//     // הפוך כל וקטור כי אנחנו שולפים מהסוף
//     for (ThreadContext* context : job->threadContexts) {
//         std::reverse(context->intermediateVec.begin(), context->intermediateVec.end());
//     }
//
//     std::priority_queue<HeapNode, std::vector<HeapNode>, std::greater<>> minHeap;
//
//     // אתחול ה־heap עם האלמנט האחרון מכל vector
//     for (int i = 0; i < job->threadContexts.size(); ++i) {
//         auto& vec = job->threadContexts[i]->intermediateVec;
//         if (!vec.empty()) {
//             minHeap.push(HeapNode{i, vec.back()});
//             vec.pop_back();
//         }
//     }
//
//     while (!minHeap.empty()) {
//         auto top = minHeap.top();
//         minHeap.pop();
//         K2* currKey = top.pair.first;
//
//         IntermediateVec group;
//         group.push_back(top.pair);
//
//         // המשכי איסוף מכל ה־vectors עם אותו key
//         for (int i = 0; i < job->threadContexts.size(); ++i) {
//             auto& vec = job->threadContexts[i]->intermediateVec;
//             while (!vec.empty()) {
//                 K2* k = vec.back().first;
//                 if (!(*k < *currKey) && !(*currKey < *k)) {
//                     group.push_back(vec.back());
//                     vec.pop_back();
//                 } else {
//                     break;
//                 }
//             }
//         }
//
//         job->shuffledVectors.push_back(std::move(group));
//         ++job->atomicVectorToReduce;
//
//         // דחיפת הבא מכל vector שהוציא משהו
//         for (int i = 0; i < job->threadContexts.size(); ++i) {
//             auto& vec = job->threadContexts[i]->intermediateVec;
//             if (!vec.empty()) {
//                 minHeap.push(HeapNode{i, vec.back()});
//                 vec.pop_back();
//             }
//         }
//     }
//
//     job->currentStage = REDUCE_STAGE;
// }


void handelShufflePhase(ThreadContext* tc) {
    JobContext* job = tc->job;
    if (tc->threadID != 0) return;

    std::vector<std::vector<std::pair<K2*, V2*>>*> sources;
    for (ThreadContext* context : job->threadContexts) {
        std::reverse(context->intermediateVec.begin(), context->intermediateVec.end());
        sources.push_back(&context->intermediateVec);
    }

    while (true) {
        K2* minKey = nullptr;
        bool found = false;

        for (auto& vec : sources) {
            if (!vec->empty()) {
                if (!found || *vec->back().first < *minKey) {
                    minKey = vec->back().first;
                    found = true;
                }
            }
        }

        if (!found) break;

        IntermediateVec group;
        for (auto& vec : sources) {
            while (!vec->empty()) {
                K2* currKey = vec->back().first;
                if (!(*currKey < *minKey) && !(*minKey < *currKey)) {
                    group.push_back(vec->back());
                    vec->pop_back();
                } else {
                    break;
                }
            }
        }

        if (!group.empty()) {
            job->shuffledVectors.push_back(std::move(group));
            ++job->atomicVectorToReduce;
        }
    }

    job->currentStage = REDUCE_STAGE;
}

void handelReducePhase(ThreadContext* tc) {
    JobContext* job = tc->job;
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

static void doAll(ThreadContext* tc) {
    JobContext* job = tc->job;
    handelMapPhase(tc);
    handelSort(tc);
    job->barrier->barrier();
    handelShufflePhase(tc);
    job->barrier->barrier();
    handelReducePhase(tc);
}

JobHandle startMapReduceJob(const MapReduceClient& client,
                            const InputVec& inputVec, OutputVec& outputVec, int multiThreadLevel) {
    JobContext* job = new JobContext(client, inputVec, outputVec, multiThreadLevel);
    for (int i = 0; i < multiThreadLevel; ++i) {
        int oldValue = job->atomicThread++;
        ThreadContext* tc = new ThreadContext{oldValue, job};
        job->threadContexts.push_back(tc);
        job->threads.emplace_back(doAll, tc);
    }
    return job;
}

void waitForJob(JobHandle job) {
    JobContext* jobContext = static_cast<JobContext*>(job);
    std::lock_guard lock(jobContext->joinMutex);
    if (!jobContext->joined) {
        for (auto& thread : jobContext->threads) {
            thread.join();
        }
        jobContext->joined = true;
    }
}

void getJobState(JobHandle job, JobState* state) {
    auto* job_context = static_cast<JobContext*>(job);
    std::lock_guard<std::mutex> lock(job_context->stateMutex);

    stage_t stage = job_context->currentStage.load();
    state->stage = stage;

    if (stage == MAP_STAGE) {
        int inputDone = job_context->atomicInputCounter.load();
        int inputTotal = job_context->inputVec.size();
        state->percentage = inputTotal == 0 ? 100.0f : 100.0f * std::min(inputDone, inputTotal) / inputTotal;
    }
    else if (stage == SHUFFLE_STAGE) {
        int interCount = job_context->atomicIntermidiatePairs.load();
        int shuffleGroups = job_context->atomicVectorToReduce.load();
        state->percentage = interCount == 0 ? 100.0f : 100.0f * shuffleGroups / interCount;
    }
    else if (stage == REDUCE_STAGE) {
        int reduceDone = job_context->atomicOutputCounter.load();
        int totalGroups = reduceDone + job_context->shuffledVectors.size();
        state->percentage = totalGroups == 0 ? 100.0f : 100.0f * reduceDone / totalGroups;
    }
}

void closeJobHandle(JobHandle job) {
    auto* jobHandle = static_cast<JobContext*>(job);
    waitForJob(jobHandle);
    for (ThreadContext* tc : jobHandle->threadContexts) {
        delete tc;
    }
    delete jobHandle->barrier;
    delete jobHandle;
}
