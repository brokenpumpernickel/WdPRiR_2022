from concurrent.futures import ThreadPoolExecutor, ProcessPoolExecutor
import multiprocessing
import time

def fibon(n):
    if n < 2:
        return 2

    return fibon(n - 1) + fibon(n - 2)

num_fibons = 45

if __name__ == '__main__':
    start_sequential = time.time()
    for n in range(num_fibons):
        fib = fibon(n)
    end_sequential = time.time()
    print(f'Sequential: {end_sequential - start_sequential}')

    start_thread_pool = time.time()
    with ThreadPoolExecutor(multiprocessing.cpu_count()) as ex:
        futures = [ex.submit(fibon, n) for n in range(num_fibons)]
        results = [f.result() for f in futures]
    end_thread_pool = time.time()
    print(f'Thread pool: {end_thread_pool - start_thread_pool}')

    start_process_pool = time.time()
    with ProcessPoolExecutor(multiprocessing.cpu_count()) as ex:
        futures = [ex.submit(fibon, n) for n in range(num_fibons)]
        results = [f.result() for f in futures]
    end_process_pool = time.time()
    print(f'Process pool: {end_process_pool - start_process_pool}')    
