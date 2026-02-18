# uthreads – User-Level Threads Library (C++)

User-level threads library for the HUJI OS course. Preemptive scheduling using `SIGVTALRM` and a virtual timer; context switching via `setjmp`/`longjmp`. Provides spawn, block/resume, sleep, terminate, and time/quantum queries.

## Build
Static library (default):
```bash
make
```
Shared library:
```bash
make shared
```

This produces `libuthreads.a` (and optionally `libuthreads.so`).

## Use
Include the header and link the library:
```bash
g++ -std=c++11 -O2 -Wall -Wextra -Werror -I. your_program.cpp -L. -luthreads -o your_program
```

Minimal example:
```cpp
#include "uthreads.h"
#include <iostream>

void worker() {
  for (int i = 0; i < 3; ++i) {
    std::cout << "tid=" << uthread_get_tid() << " q=" << uthread_get_total_quantums() << "\n";
  }
  uthread_terminate(uthread_get_tid());
}

int main() {
  uthread_init(100000);             // 100ms quantum
  int tid = uthread_spawn(worker);  // add a ready thread
  // main keeps running while timer preempts between main and worker
  for (int i = 0; i < 5; ++i) { /* do work */ }
  uthread_terminate(0);             // terminate all and exit
}
```

## API (summary)
- `int uthread_init(int quantum_usecs)` – init library; main thread becomes RUNNING; quantum in µs.
- `int uthread_spawn(thread_entry_point entry_point)` – create thread with its own stack (size `STACK_SIZE`).
- `int uthread_terminate(int tid)` – terminate `tid`; `tid==0` terminates the process after cleanup.
- `int uthread_block(int tid)` – block thread `tid` (not allowed for main).
- `int uthread_resume(int tid)` – move blocked thread to READY (or SLEEP→SLEEP_BLOCKED handling as implemented).
- `int uthread_sleep(int num_quantums)` – put RUNNING thread to sleep for given quantums (not allowed for main).
- `int uthread_get_tid()` – current thread id.
- `int uthread_get_total_quantums()` – total quantums since init (starts at 1 right after init).
- `int uthread_get_quantums(int tid)` – number of quantums a thread has RUN.

States used internally: `READY, RUNNING, BLOCKED, SLEEP, SLEEP_BLOCKED`.

## Notes
- Preemption by `SIGVTALRM` + `setitimer(ITIMER_VIRTUAL)`; context via `sigsetjmp/siglongjmp`.
- Each spawned thread gets a private stack of size `STACK_SIZE`.
- Main thread (`tid==0`) uses the process stack and cannot be blocked or put to sleep.
- Time accounting: on each new quantum, `total_quantums` increases; the first quantum after init is counted.
- Error cases return `-1` as described in the header comments.

## Files
- `uthreads.h` – public API and constants.
- `uthreads.cpp` – implementation (scheduler, timer handler, ready queue, sleep logic).

## License
Educational use.
