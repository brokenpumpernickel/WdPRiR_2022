#include <stdio.h>
#include "nvToolsExt.h"

void add_cpu(int* array_a, int* array_b, int* array_c, int elements) { // Dodawanie na CPU
    nvtxRangePushA("add_cpu"); // Otwiera zakres czasu, który potem będzie widoczny w profilerze
    for(int i = 0; i < elements; ++i)
        array_c[i] = array_a[i] + array_b[i];
    nvtxRangePop(); // Zamyka powyższy zakres
}

__global__ void add_gpu(int* array_a, int* array_b, int* array_c) { // Dodawanie na GPU
    int index = blockIdx.x * blockDim.x + threadIdx.x;
    array_c[index] = array_a[index] + array_b[index];
}

int main() {
    int elements = 1 << 28; // Rozmiar tablic

    int* host_a = (int*) malloc(sizeof(int) * elements); // Tablice z danymi po stronie hosta (CPU)
    int* host_b = (int*) malloc(sizeof(int) * elements);
    int* host_c = (int*) malloc(sizeof(int) * elements);

    for(int i = 0; i < elements; ++i) { // Wypełnianie tablic fejkowymi danymi
        host_a[i] = i;
        host_b[i] = i;
    }

    // Host

    add_cpu(host_a, host_b, host_c, elements); // Dodawanie na CPU

    for(int i = 0; i < 10; ++i)
        printf("Host %d + %d = %d\n", host_a[i], host_b[i], host_c[i]);
    memset(host_c, 0, sizeof(int) * elements); // Profilaktyczne zerowanie pamięci - będziemy jeszcze korzystać z tych tablic i lepiej się upewnić, że nie widzimy starego wyniku.

    // GPU

    int* device_a;
    int* device_b;
    int* device_c;
    cudaMalloc(&device_a, sizeof(int) * elements); // Allokacja pamięci na urządzeniu (GPU)
    cudaMalloc(&device_b, sizeof(int) * elements);
    cudaMalloc(&device_c, sizeof(int) * elements);

    cudaMemcpy(device_a, host_a, sizeof(int) * elements, cudaMemcpyHostToDevice); // Kopiowanie pamięci z hosta na urządzenie - jako ostatni parametr można dać cudaMemcpyDefault, wtedy sam się domyśli, w jakim kierunku kopiujemy
    cudaMemcpy(device_b, host_b, sizeof(int) * elements, cudaMemcpyHostToDevice);

    dim3 block(128);
    dim3 grid(elements / block.x); // Dzielimy tablice wyjściową na bloki po 128 wątków każdy

    add_gpu<<<grid, block>>>(device_a, device_b, device_c); // Dodajemy na GPU
    
    cudaMemcpy(host_c, device_c, sizeof(int) * elements, cudaMemcpyDeviceToHost); // Sprowadzamy dane z GPU na hosta.
    for(int i = 0; i < 10; ++i)
        printf("GPU 1 %d + %d = %d\n", host_a[i], host_b[i], host_c[i]);
    memset(host_c, 0, sizeof(int) * elements);

    free(host_a); // Zwalniamy tablice zarezerwowane na hoście, dalej będziemy je allokować jeszcze raz przy pomocy API CUDA
    free(host_b);
    free(host_c);

    // GPU Pinned

    cudaMallocHost(&host_a, sizeof(int) * elements); // Allokujemy pamięć na hoście przy pomocy API CUDA. W przeciwieństwie do tego, co zwraca malloc, ta pamięć nie jest stronicowana - unikamy więc pośredniczącego bufora przy kopiowaniu między hostem a urządzeniem.
    cudaMallocHost(&host_b, sizeof(int) * elements);
    cudaMallocHost(&host_c, sizeof(int) * elements);

    for(int i = 0; i < elements; ++i) {
        host_a[i] = i;
        host_b[i] = i;
    }

    cudaMemcpy(device_a, host_a, sizeof(int) * elements, cudaMemcpyHostToDevice);
    cudaMemcpy(device_b, host_b, sizeof(int) * elements, cudaMemcpyHostToDevice);

    add_gpu<<<grid, block>>>(device_a, device_b, device_c);
    
    cudaMemcpy(host_c, device_c, sizeof(int) * elements, cudaMemcpyDeviceToHost);
    for(int i = 0; i < 10; ++i)
        printf("GPU Pinned %d + %d = %d\n", host_a[i], host_b[i], host_c[i]);
    memset(host_c, 0, sizeof(int) * elements);

    cudaFreeHost(host_a); // Pamięć po stronie hosta teraz też musimy zwolnić przy pomocy API CUDA.
    cudaFreeHost(host_b);
    cudaFreeHost(host_c);    
    
    cudaFree(device_a);
    cudaFree(device_b);
    cudaFree(device_c);

    // GPU Mapped

    cudaHostAlloc(&host_a, sizeof(int) * elements, cudaHostAllocMapped); // Allokujemy po stronie hosta mapowany obszar pamięci. Warning: działa wolno!
    cudaHostAlloc(&host_b, sizeof(int) * elements, cudaHostAllocMapped);
    cudaHostAlloc(&host_c, sizeof(int) * elements, cudaHostAllocMapped);

    for(int i = 0; i < elements; ++i) {
        host_a[i] = i;
        host_b[i] = i;
    }

    add_cpu(host_a, host_b, host_c, elements);

    for(int i = 0; i < 10; ++i)
        printf("Host Mapped: %d + %d = %d\n", host_a[i], host_b[i], host_c[i]);
    memset(host_c, 0, sizeof(int) * elements);
    
    add_gpu<<<grid, block>>>(host_a, host_b, host_c); // Ponieważ używamy mapowanej pamięci, to te same wskaźniki działają dla hosta i GPU.
    cudaDeviceSynchronize();

    for(int i = 0; i < 10; ++i)
        printf("GPU Mapped %d + %d = %d\n", host_a[i], host_b[i], host_c[i]);

    cudaFreeHost(host_a); // Zwalniamy mapowaną pamięć - musimy znowu użyć tej funkcji.
    cudaFreeHost(host_b);
    cudaFreeHost(host_c);   

    // GPU Managed

    cudaMallocManaged(&host_a, sizeof(int) * elements); // Dla odmiany allokujemy pamięć zarządzaną przez framework CUDA.
    cudaMallocManaged(&host_b, sizeof(int) * elements);
    cudaMallocManaged(&host_c, sizeof(int) * elements);

    for(int i = 0; i < elements; ++i) {
        host_a[i] = i;
        host_b[i] = i;
    }

    add_cpu(host_a, host_b, host_c, elements);

    for(int i = 0; i < 10; ++i)
        printf("Host Managed: %d + %d = %d\n", host_a[i], host_b[i], host_c[i]);
    memset(host_c, 0, sizeof(int) * elements);
    
    add_gpu<<<grid, block>>>(host_a, host_b, host_c); // Znowu - te same wskaźniki na hoście i urządzeniu
    cudaDeviceSynchronize();

    for(int i = 0; i < 10; ++i)
        printf("GPU Managed %d + %d = %d\n", host_a[i], host_b[i], host_c[i]);

    cudaFree(host_a); // Musimy tę pamięć zwolnić przy pomocy cudaFree
    cudaFree(host_b);
    cudaFree(host_c);   

    return 0;
}