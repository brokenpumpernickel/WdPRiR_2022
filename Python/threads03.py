import threading
import time

condition = threading.Condition()

# def waiter():
#     condition.acquire()
#     print(f'Waiting: {threading.get_ident()}!')
#     condition.wait() # to powinno być w pętli ze sprawdzaniem warunku
#     condition.release()
#     print(f'Finished waiting: {threading.get_ident()}!')

# def not_waiter():
#     condition.acquire()
#     print(f'Waiting to give a signal: {threading.get_ident()}!')
#     time.sleep(5)
#     condition.notify_all()
#     condition.release()
#     print(f'Notified all: {threading.get_ident()}!')

def waiter():
    with condition:
        print(f'Waiting: {threading.get_ident()}!')
        condition.wait() # to powinno być w pętli ze sprawdzaniem warunku
        print(f'Finished waiting: {threading.get_ident()}!')

def not_waiter():
    with condition:
        print(f'Waiting to give a signal: {threading.get_ident()}!')
        time.sleep(5)
        condition.notify_all()
        print(f'Notified all: {threading.get_ident()}!')    

nw = threading.Thread(target = not_waiter)
w = [threading.Thread(target = waiter) for _ in range(10)]

for t in w:
    t.start()
nw.start()

nw.join()
for t in w:
    t.join()