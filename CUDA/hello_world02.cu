#include <stdio.h>

__global__ void kernel() {
    printf("%d - %d\n", blockIdx.x, threadIdx.x); // blockIdx.x - numer bloku, threadIdx.x - numer wątku w bloku
}

int main() {
    kernel<<<3, 5>>>(); // Trzy bloki po 5 wątków w każdym
    return 0;
}