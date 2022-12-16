import pika
import time

credentials = pika.PlainCredentials('xD', 'xD')
connection_parameters = pika.ConnectionParameters('xD', virtual_host = 'wdprir', credentials = credentials)
connection = pika.BlockingConnection(connection_parameters)

channel = connection.channel()

channel.queue_declare('wdprir_queue')

def callback(channel, method, properties, body):
    print(f'Received: {body.decode() = }')

channel.basic_consume('wdprir_queue', callback, True)
channel.start_consuming()

channel.close()