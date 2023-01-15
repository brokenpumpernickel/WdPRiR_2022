#include <stdio.h>

__global__ void kernel() {
    printf("(%d, %d) - (%d, %d)\n", blockIdx.x, blockIdx.y, threadIdx.x, threadIdx.y);
}

int main() {
    dim3 grid(2, 2); // 2D grid z 2x2 blokami
    dim3 block(3, 3); // 3x3 wątki w każdym bloku

    kernel<<<grid, block>>>();
    return 0;
}