# Multithreaded MapReduce Framework

A production-grade C++ MapReduce framework using POSIX threads with lock-free atomic work distribution, barrier-based phase synchronisation, and a generic client API — modelled after Google's MapReduce paper.

---

## Problem Solved

Implement a parallel data-processing framework that partitions work across N threads in four phases (Map → Sort → Shuffle → Reduce), with correct phase transitions, load balancing, and non-blocking progress reporting — all without a central scheduler.

---

## Processing Pipeline

```
Input (K1, V1)
  → [MAP]     — all threads in parallel, atomic work-stealing
  → [SORT]    — each thread sorts its local intermediate vector by key
  → [SHUFFLE] — thread 0 merges sorted vectors, groups by key
  → [REDUCE]  — all threads in parallel, atomic work-stealing
Output (K3, V3)
```

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Lock-free work distribution | Single `std::atomic<uint64_t>` incremented by each thread to claim the next item; no mutex needed for work stealing in Map or Reduce phases |
| 64-bit encoded job state | The same atomic encodes both `stage` (2 bits) and `processed count` (lower 31 bits), allowing `getJobState()` to read progress without a lock |
| Barrier synchronisation | Custom `Barrier` (mutex + condition variable + generation counter) ensures all threads complete the current phase before any thread starts the next |
| Thread-local intermediate storage | Each thread writes to its own `IntermediateVec` during Map — no inter-thread contention; Shuffle reads them all after Map is complete |
| Output protection | A single mutex guards the shared `OutputVec` during Reduce; `emit3` acquires it before appending |
| Generic template API | `K1/V1/K2/V2/K3/V3` are abstract base classes; users subclass to define any data types — the framework is fully type-agnostic |

---

## Synchronisation Architecture

```
Map Phase      → parallel (atomic counter per item)
Barrier ──── all threads wait here ─────────────────
Sort Phase     → parallel (each thread sorts own data)
Barrier
Shuffle Phase  → thread 0 only; others wait at barrier
Barrier
Reduce Phase   → parallel (atomic counter per group)
Barrier
→ job complete; waitForJob() unblocks
```

---

## API

```cpp
// User implements:
class MyClient : public MapReduceClient {
    void map(const K1* key, const V1* val, void* ctx) const override;    // call emit2(...)
    void reduce(const IntermediateVec* pairs, void* ctx) const override;  // call emit3(...)
};

// Framework functions:
JobHandle startMapReduceJob(client, inputVec, outputVec, numThreads);
void      waitForJob(JobHandle job);
void      getJobState(JobHandle job, JobState* state);  // non-blocking
void      closeJobHandle(JobHandle job);
```

---

## Tech Stack & Concepts

- **Language:** C++ (C++20)
- **Build:** `make` → `libMapReduceFramework.a`
- **Key concepts:** `pthreads`, lock-free atomics, barrier synchronisation, work-stealing, generic API design, concurrent data structures

---

## Build & Link

```bash
make
g++ -std=c++20 -pthread -o my_app my_client.cpp -L. -lMapReduceFramework
```

**Example** (`SampleClient.cpp`): character-frequency counter — maps each string to `(char, count)` pairs, reduces by summing counts per character.
