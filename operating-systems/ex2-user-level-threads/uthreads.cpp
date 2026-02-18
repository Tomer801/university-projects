#include "uthreads.h"

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <sys/time.h>
#include <list>
extern "C" {
   #include <setjmp.h>
}


#ifdef __x86_64__
/* code for 64 bit Intel arch */

typedef unsigned long address_t;
#define JB_SP 6
#define JB_PC 7

/* A translation is required when using an address of a variable.
   Use this as a black box in your code. */
address_t translate_address(address_t addr)
{
    address_t ret;
    asm volatile("xor    %%fs:0x30,%0\n"
                 "rol    $0x11,%0\n"
            : "=g" (ret)
            : "0" (addr));
    return ret;
}

#endif


using namespace std;

// data of threads
static sigjmp_buf env[MAX_THREAD_NUM];
static char* stacks[MAX_THREAD_NUM];//pointer to array of char*[size of stack]
static State states[MAX_THREAD_NUM];
static int quantums[MAX_THREAD_NUM];//quantums of each thread
static int sleep_counter[MAX_THREAD_NUM];
static int quantum;
static int currently_runing_tid;
static int total_quantums;
static char* stack_to_delete = nullptr;

//Que
static list<int> ready_que;

// Block signals during critical sections
static sigset_t signal_set;
//timer
static struct itimerval timer;
//un\block signals
void block_signals() {
    sigprocmask(SIG_BLOCK, &signal_set, nullptr);
}

void unblock_signals() {
    sigprocmask(SIG_UNBLOCK, &signal_set, nullptr);
}


void reset_timer() {
    timer.it_value.tv_sec = 0;
    timer.it_value.tv_usec = quantum;
    timer.it_interval.tv_sec = 0;
    timer.it_interval.tv_usec = quantum;
    if (setitimer(ITIMER_VIRTUAL, &timer, nullptr) < 0) {
        perror("system error: setitimer");
        exit(1);
    }
}


// Free all stacks and remove them from the ready queue
void freeAll() {
    for (int i = 1; i < MAX_THREAD_NUM; ++i) {
        if (stacks[i]) {
            ready_que.remove(i);  // Fix: use i instead of tid
            states[i] = BLOCKED;
            delete[] stacks[i];
            stacks[i] = nullptr;
        }
    }

}

void sleepCount() {
    for (int i = 0; i < MAX_THREAD_NUM; ++i){
        if (sleep_counter[i] > 0){
            if (sleep_counter[i] == total_quantums){
                if(states[i] == SLEEP_BLOCKED){
                    states[i] = BLOCKED;
                }
                else if(states[i] == SLEEP){
                    states[i] = READY;
                    ready_que.push_back(i);
                }
                sleep_counter[i] = 0;
            }
        }
    }
}

void scheduler()
{
    block_signals();
    // the tread we came back from
    int old_tid = currently_runing_tid;
    // Process sleeping threads
    sleepCount();
    // add to total count of quantums
    total_quantums++;
    // If no thread is ready, continue with the main thread
    if (states[old_tid] == RUNNING) {
        states[old_tid] = READY;
        ready_que.push_back(old_tid);
    }

    int next_tid = ready_que.front();
    ready_que.pop_front();
    currently_runing_tid = next_tid;
    states[next_tid] = RUNNING;
    quantums[next_tid]++;
    unblock_signals();
    siglongjmp(env[next_tid], 1);
}

void timer_handler(int sig)
{

    if (sig == SIGVTALRM)
    {
        block_signals();
        // in case of tread(not 0) kil himself
        if (stack_to_delete != nullptr) {
            delete[] stack_to_delete;
            stack_to_delete = nullptr;
        }
        // jmp to here from term to switch threads befor kilinig main 0
        int ret = sigsetjmp(env[currently_runing_tid], 1);
        if (ret == 1) {
            if (states[0]==KILL) {
                freeAll();
                exit(0);
            }
            unblock_signals();
            return;
        }
        unblock_signals();
        scheduler();
    }
}


int uthread_init(int quantum_usecs) {
    if (quantum_usecs <= 0){
        //print err msg
        return -1;
    }

    // Initialize signal set for blocking
    sigemptyset(&signal_set);
    sigaddset(&signal_set, SIGVTALRM);
    // Initialize thread states and arrays
    for (int i = 0; i < MAX_THREAD_NUM; i++) {
        stacks[i] = nullptr;
        states[i] = BLOCKED;
        quantums[i] = 0;
        sleep_counter[i] = 0;
    }

    quantum = quantum_usecs;
    currently_runing_tid = 0;
    quantums[0] = 1;
    total_quantums++;
    states[0] = RUNNING;

    //SIGVTALRM handler
    struct sigaction sa;
    sa.sa_handler = &timer_handler;      // forward-declared elsewhere
    sa.sa_flags   = SA_RESTART;     // restart syscalls if interrupted
    sigemptyset(&sa.sa_mask);       // don’t block any extra signals
    if (sigaction(SIGVTALRM, &sa, nullptr) < 0) {
        perror("system error: sigaction");
        exit(1);
    }
    //Configure the virtual timer (ITIMER_VIRTUAL)
    reset_timer();
    return 0;
}


int uthread_spawn(thread_entry_point entry_point) {
    block_signals();

    int tid = -1;
    if(entry_point == nullptr){
        //err msg
        unblock_signals();
        return tid;
    }
    // check if we have enough threads
    for(int i = 1;i<MAX_THREAD_NUM; i++){
        if(stacks[i]== nullptr){
            tid = i;
            break;
        }
    }
    if(tid == -1){
        //err msg
        unblock_signals();
        return tid;
    }

    // allocate stack
    stacks[tid] = new char[STACK_SIZE];
    if (!stacks[tid]) {
        //err msg
        unblock_signals();
        return tid;
    }

    // initialize context
    sigsetjmp(env[tid], 1);

    // set stack pointer and program counter
    address_t sp = (address_t) stacks[tid] + STACK_SIZE - sizeof(address_t);
    address_t pc = (address_t) entry_point;
    env[tid]->__jmpbuf[JB_SP] = translate_address(sp);
    env[tid]->__jmpbuf[JB_PC] = translate_address(pc);
    sigemptyset(&env[tid]->__saved_mask);
    states[tid] = READY;
    quantums[tid] = 0;
    ready_que.push_back(tid);
    unblock_signals();

    return tid;
}


int uthread_terminate(int tid) {
    block_signals();

    if (stacks[tid] == nullptr && tid != 0) {
        unblock_signals();
        return -1;
    }
    // 0 is terminnated
    if (tid == 0) {
        states[0] = KILL;  // Mark main thread for termination
        // terminnated by other thread
        //jmp to time_hendlaer switch to main thread
        if (currently_runing_tid != 0) {
            sigsetjmp(env[currently_runing_tid], 1);
            currently_runing_tid = 0;
            siglongjmp(env[currently_runing_tid], 1);
        }
        // If we're already in main thread, clean up and exit
        freeAll();
        exit(0);
    }

    // Handle regular thread termination
    ready_que.remove(tid);
    states[tid] = BLOCKED;
    sleep_counter[tid] = 0;

    if (tid == currently_runing_tid) {
        stack_to_delete = stacks[tid];
        stacks[tid] = nullptr;
        reset_timer();
        scheduler();  // Will never return
    } else {
        delete[] stacks[tid];
        stacks[tid] = nullptr;
        unblock_signals();
    }

    return 0;
}


int uthread_block(int tid) {
    block_signals();

    if (tid == 0 || !stacks[tid]) {
        unblock_signals();
        return -1;
    }
    if (states[tid] == BLOCKED) {
        unblock_signals();
        return 0;
    }

    if (tid == currently_runing_tid)
    {
        states[tid] = BLOCKED;
        int ret = sigsetjmp(env[tid], 1);
        if (ret == 1) {
            unblock_signals();
            return 0;
        }
        reset_timer();
        scheduler(); // לא סופרים קוואנטום כאן – זה קורה ב־scheduler
    }
    else
    {
        if(states[tid] == SLEEP || states[tid] == SLEEP_BLOCKED){
            states[tid] = SLEEP_BLOCKED;
        }else{
            states[tid] = BLOCKED;
        }


        ready_que.remove(tid);
    }
    unblock_signals();

    return 0;
}



int uthread_resume(int tid) {
    block_signals();

    if (stacks[tid] == nullptr) {
        unblock_signals();
        return -1;
    }
    if (states[tid] == BLOCKED) {
        states[tid] = READY;
        ready_que.push_back(tid);
    }
    if (states[tid] == SLEEP_BLOCKED) {
        states[tid] = SLEEP;
    }

    unblock_signals();

    return 0;

}


int uthread_sleep(int num_quantums){
    block_signals();
    int tid = currently_runing_tid;

    if (tid == 0 || num_quantums <= 0)
    {
        unblock_signals();
        return -1; // אסור ל־main להירדם
    }

    sleep_counter[tid] = num_quantums + total_quantums;
    states[tid] = SLEEP;

    int ret = sigsetjmp(env[tid], 1);
    if (ret == 1) {
        unblock_signals();
        return 0;
    }
    reset_timer();
    scheduler(); // לא נספור quantum כי זו שינה יזומה
    return 0;
}


int uthread_get_tid() {

    return currently_runing_tid;
}


int uthread_get_total_quantums() {
    return total_quantums;
}


int uthread_get_quantums(int tid) {
    if (tid < 0 || tid >= MAX_THREAD_NUM) {
        return -1;
    }
    if (!stacks[tid]  && tid != 0) {
        return -1;
    }
    return quantums[tid];
}


