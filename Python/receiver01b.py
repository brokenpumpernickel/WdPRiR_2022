import pika
import time

credentials = pika.PlainCredentials('xD', 'xD')
connection_parameters = pika.ConnectionParameters('xD', virtual_host = 'wdprir', credentials = credentials)
connection = pika.BlockingConnection(connection_parameters)

channel = connection.channel()
channel.basic_qos(prefetch_count = 1)

channel.queue_declare('wdprir_queue_durable', durable = True)

def callback(channel, method, properties, body):
    print(f'Received: {body.decode() = }')
    time.sleep(0.5)
    channel.basic_ack(method.delivery_tag)

channel.basic_consume('wdprir_queue_durable', callback)
channel.start_consuming()

channel.close()