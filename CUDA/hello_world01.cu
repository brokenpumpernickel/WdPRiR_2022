#include <stdio.h>

__global__ void kernel() {
    printf("Hello CUDA!\n");
}

int main() {
    kernel<<<1, 32>>>(); // Odpalamy kernel z 1 blokiem i 32 wątkami w tym bloku
    return 0;
}