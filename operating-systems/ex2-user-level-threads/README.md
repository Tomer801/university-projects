# User-Level Thread Scheduler

A preemptive, round-robin user-space threading library for Linux/POSIX, implementing OS scheduling primitives (virtual timer, signal handler, context save/restore) entirely in user space without kernel threads.

---

## Problem Solved

Implement a complete threading library — including preemptive scheduling, blocking, sleeping, and accurate quantum accounting — using only POSIX process signals and `setjmp`/`longjmp`, with no kernel-level thread support.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Preemptive scheduling without kernel threads | `setitimer(ITIMER_VIRTUAL, quantum_usecs)` fires `SIGVTALRM` periodically; the signal handler saves the current thread's context and switches to the next READY thread |
| Context save/restore | `sigsetjmp` saves CPU registers and signal mask; `siglongjmp` restores them — the suspended thread resumes exactly where it paused |
| Per-thread stack isolation | Each spawned thread receives a private heap-allocated stack of `STACK_SIZE` bytes; the initial `PC` is set to the entry point function |
| SLEEP quantum accounting | Sleeping threads store the target wake-up quantum; the scheduler checks and re-queues them on each quantum transition |
| `SLEEP_BLOCKED` state | A thread blocked while sleeping enters `SLEEP_BLOCKED`; `uthread_resume()` transitions it back to `SLEEP` rather than `READY`, preserving the remaining sleep duration |
| Main thread exception | `tid=0` uses the process stack; `uthread_block` and `uthread_sleep` are forbidden on it to prevent deadlock |

---

## Thread State Machine

```
READY ──(scheduled)──► RUNNING ──(block)──► BLOCKED
  ▲                        │                    │
  │         (preempt)      │     (resume)       │
  └────────────────────────┘◄───────────────────┘
                           │
                     (sleep N quanta)
                           ▼
                         SLEEP ──(block)──► SLEEP_BLOCKED
                           │                    │
                     (wake-up quantum)   (resume → SLEEP)
                           ▼
                         READY
```

---

## API

```cpp
int uthread_init(int quantum_usecs);           // initialise; main = RUNNING
int uthread_spawn(thread_entry_point fn);      // create READY thread, return tid
int uthread_terminate(int tid);               // terminate tid; tid=0 exits process
int uthread_block(int tid);                   // move tid to BLOCKED
int uthread_resume(int tid);                  // move BLOCKED → READY
int uthread_sleep(int num_quantums);          // sleep running thread for N quanta
int uthread_get_tid();                        // current thread id
int uthread_get_total_quantums();             // total quantums elapsed since init
int uthread_get_quantums(int tid);            // quantums thread tid has been RUNNING
```

All functions return `-1` on error with a message to stderr.

---

## Tech Stack & Concepts

- **Language:** C++ (C++11)
- **Build:** `make` → `libuthreads.a` (static) | `make shared` → `libuthreads.so`
- **Key concepts:** POSIX signals, `SIGVTALRM`, `sigsetjmp/siglongjmp`, virtual timer, round-robin scheduling, quantum accounting, user-space OS primitives

---

## Build & Link

```bash
make
g++ -std=c++11 -O2 -I. your_program.cpp -L. -luthreads -o your_program
```
