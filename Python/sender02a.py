import pika
import time

credentials = pika.PlainCredentials('xD', 'xD')
connection_parameters = pika.ConnectionParameters('xD', virtual_host = 'wdprir', credentials = credentials)
connection = pika.BlockingConnection(connection_parameters)

channel = connection.channel()

channel.exchange_declare('wdprir_fanout', 'fanout')

index = 0
while True:
    channel.basic_publish(exchange = 'wdprir_fanout',
                          routing_key = '',
                          body = f'I love WdPRiR! {index}')
    time.sleep(1)
    index += 1

channel.close()