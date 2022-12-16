import pika
import time
import threading

credentials = pika.PlainCredentials('xD', 'xD')
connection_parameters = pika.ConnectionParameters('xD', virtual_host = 'wdprir', credentials = credentials)
connection = pika.BlockingConnection(connection_parameters)
channel = connection.channel()

feedback_connection = pika.BlockingConnection(connection_parameters)
feedback_channel = feedback_connection.channel()
feedback_queue = feedback_channel.queue_declare('', exclusive = True)

def feedback():
    def callback(channel, method, properties, body):
        print(f'Received: {properties.correlation_id} {body.decode() = }')
    
    feedback_channel.basic_consume(feedback_queue.method.queue, callback)
    feedback_channel.start_consuming()
    feedback_channel.close()

tf = threading.Thread(target = feedback)
tf.start()

channel.queue_declare('wdprir_queue_durable', durable = True)

index = 0
while True:
    channel.basic_publish(exchange = '',
                          routing_key = 'wdprir_queue_durable',
                          body = f'{index}',
                          properties = pika.BasicProperties(reply_to = feedback_queue.method.queue, correlation_id = f'{index}'))
    print(f'Sent: {index}')
    time.sleep(1)
    index += 1

channel.close()