import pika
import time

credentials = pika.PlainCredentials('xD', 'xD')
connection_parameters = pika.ConnectionParameters('xD', virtual_host = 'wdprir', credentials = credentials)
connection = pika.BlockingConnection(connection_parameters)

channel = connection.channel()

channel.exchange_declare('wdprir_direct', 'direct')

keys = ['red', 'blue']

index = 0
while True:
    channel.basic_publish(exchange = 'wdprir_direct',
                          routing_key = keys[index % len(keys)],
                          body = f'I love WdPRiR! {index} {keys[index % len(keys)]}')
    time.sleep(1)
    index += 1

channel.close()