from concurrent.futures import ThreadPoolExecutor, as_completed
import numpy as np
import time
from tqdm import tqdm
import numba as nb
import gc

matrix_size = 2048

Xs = [np.random.random((matrix_size, matrix_size)) for _ in range(100)]
Ys = [np.random.random((matrix_size, matrix_size)) for _ in range(100)]

print('Matrices generated')

# Sequential

start_sequential = time.time()
results_sequential = []
for A, B in tqdm(zip(Xs, Ys), total = len(Xs)):
    results_sequential.append(A @ B)
del results_sequential
gc.collect()
stop_sequential = time.time()

print(f'Time sequential = {stop_sequential - start_sequential}')

# Thread pool

# with ThreadPoolExecutor() as pool:
#     start_pool = time.time()
#     futures = [pool.submit(lambda A, B: A @ B, A, B) for A, B in zip(Xs, Ys)]
#     results_thread = []
#     for future in tqdm(as_completed(futures), total = len(futures)):
#         results_thread.append(future.result())
#     stop_pool = time.time()

# print(f'Time pool = {stop_pool - start_pool}')

# Numba

@nb.njit(cache = True)
def nmult(A, B):
    return A @ B

start_numba = time.time()
results_numba = []
for A, B in tqdm(zip(Xs, Ys), total = len(Xs)):
    results_numba.append(nmult(A, B))
stop_numba = time.time()

print(f'Time numba = {stop_numba - start_numba}')