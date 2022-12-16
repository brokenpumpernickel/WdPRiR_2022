import pika
import time

credentials = pika.PlainCredentials('xD', 'xD')
connection_parameters = pika.ConnectionParameters('xD', virtual_host = 'wdprir', credentials = credentials)
connection = pika.BlockingConnection(connection_parameters)

channel = connection.channel()

channel.queue_declare('wdprir_queue_durable', durable = True)

index = 0
while True:
    channel.basic_publish(exchange = '',
                          routing_key = 'wdprir_queue_durable',
                          body = f'I love WdPRiR! {index}',
                          properties = pika.BasicProperties(delivery_mode = pika.spec.PERSISTENT_DELIVERY_MODE))
    time.sleep(1)
    index += 1

channel.close()