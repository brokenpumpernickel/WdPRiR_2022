import pika
import time

credentials = pika.PlainCredentials('xD', 'xD')
connection_parameters = pika.ConnectionParameters('xD', virtual_host = 'wdprir', credentials = credentials)
connection = pika.BlockingConnection(connection_parameters)

channel = connection.channel()

channel.exchange_declare('wdprir_direct', 'direct')
queue = channel.queue_declare('', exclusive = True)
channel.queue_bind(queue.method.queue, 'wdprir_direct', 'red')

def callback(channel, method, properties, body):
    print(f'Received: {body.decode() = }')

channel.basic_consume(queue.method.queue, callback, True)
channel.start_consuming()

channel.close()