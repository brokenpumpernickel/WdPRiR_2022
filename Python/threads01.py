import threading

class MyThread(threading.Thread):
    def run(self):
        print('Hello Multithreaded World!')

t = MyThread()
t.start()
t.join()