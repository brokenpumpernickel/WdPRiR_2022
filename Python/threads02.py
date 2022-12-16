import threading

counter = 0
lock = threading.RLock()

def increase_value():
    global counter
    for _ in range(1000000):
        lock.acquire()
        counter += 1
        lock.release()

t1 = threading.Thread(target = increase_value)
t2 = threading.Thread(target = increase_value)

t1.start()
t2.start()

t1.join()
t2.join()

print(f'{counter = }')
