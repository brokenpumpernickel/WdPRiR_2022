from concurrent.futures import ThreadPoolExecutor
import multiprocessing
from scipy.signal import convolve2d
import numpy as np
import time
import numba
from numba.typed import List
import gc

kernel = np.array(
    [
        [0, 1, 0],
        [1, 0, 1],
        [0, 1, 0]
    ]
)

matrix_size = 4096 * 2
bs = 64
ws = matrix_size // bs

matrices = [np.random.random((matrix_size, matrix_size)) for _ in range(10)]

print("Matrices generated")

# Sequential

results_sequential = []
start_sequential = time.time()
for matrix in matrices:
    results_sequential.append(convolve2d(matrix, kernel, 'same'))
end_sequential = time.time()
del results_sequential
gc.collect()
print(f'Sequential: {end_sequential - start_sequential}')

# Thread pool

results_pool = []
with ThreadPoolExecutor(multiprocessing.cpu_count()) as ex:
    start_pool = time.time()
    for matrix in matrices:
        matrix_padded = np.pad(matrix, 1, 'constant', constant_values = 0)
        matrix_output = np.empty_like(matrix)

        def worker(ww, wh, input, output):
            output[wh * bs:(wh + 1) * bs, ww * bs:(ww + 1) * bs] = convolve2d(input[wh * bs:(wh + 1) * bs + 2, ww * bs:(ww + 1) * bs + 2], kernel, 'valid')

        futures = [ex.submit(worker, ww, wh, matrix_padded, matrix_output) for ww in range(ws) for wh in range(ws)]
        for f in futures:
            f.result()
        results_pool.append(matrix_output)
    end_pool = time.time()
del results_pool
gc.collect()
print(f'Pool: {end_pool - start_pool}')

# Numba 1

@numba.stencil
def stencil_sequential(arr):
    return arr[-1,-1] * kernel[0, 0] + arr[-1,0] * kernel[0, 1] + arr[-1,1] * kernel[0, 2] + \
           arr[0,-1] * kernel[1, 0] + arr[0,0] * kernel[1, 1] + arr[0,1] * kernel[1, 2] + \
           arr[1,-1] * kernel[2, 0] + arr[1,0] * kernel[2, 1] + arr[1,1] * kernel[2, 2]

results_stencil_sequential = []
start_stencil_sequential = time.time()
for matrix in matrices:
    results_stencil_sequential.append(stencil_sequential(np.pad(matrix, 1, 'constant', constant_values = 0)))
end_stencil_sequential = time.time()
del results_stencil_sequential
gc.collect()
print(f'Stencil sequential 1: {end_stencil_sequential - start_stencil_sequential}')

# Numba 2

@numba.njit(cache = True)
def nconv(matrices):
    results = List()
    for matrix in matrices:
        results.append(stencil_sequential(matrix))
    return results

start_stencil_sequential2 = time.time()
matrices_padded = List([np.pad(matrix, 1, 'constant', constant_values = 0) for matrix in matrices])
results = nconv(matrices_padded)
end_stencil_sequential2 = time.time()
del results
gc.collect()
print(f'Stencil sequential 2: {end_stencil_sequential2 - start_stencil_sequential2}')

# Numba 3

@numba.njit(cache = True, parallel = True)
def nconvp(matrices):
    results = List()
    for matrix in matrices:
        results.append(stencil_sequential(matrix))
    return results

start_stencil_parallel = time.time()
matrices_padded = List(np.pad(matrix, 1, 'constant', constant_values = 0) for matrix in matrices)
results = nconvp(matrices_padded)
end_stencil_parallel = time.time()
del results
gc.collect()
print(f'Stencil parallel: {end_stencil_parallel - start_stencil_parallel}')