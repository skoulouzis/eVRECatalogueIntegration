import base64
import json
import os
import os.path
import pika
import tempfile
import time
import json
from util.util import *


class RPCServer():

    def __init__(self, rabbitmq_host, rabbitmq_port, rabbitmq_username, rabbitmq_password):
        self.queue_name = 'log2prov'
        credentials = pika.PlainCredentials(rabbitmq_username, rabbitmq_password)
        parameters = pika.ConnectionParameters(rabbitmq_host, int(rabbitmq_port), '/', credentials)
        connection = pika.BlockingConnection(parameters)
        self.channel = connection.channel()
        self.channel.queue_declare(queue=self.queue_name)
        
        
    def start(self):
        self.channel.basic_qos(prefetch_count=1)
        self.channel.basic_consume(self.on_request, queue=self.queue_name)
        self.channel.start_consuming()
        
    def on_request(self, ch, method, props, body):
        response = self.handle_delivery(body)    
        ch.basic_publish(exchange='',routing_key=props.reply_to, properties=pika.BasicProperties(correlation_id=props.correlation_id), body=str(response))
        ch.basic_ack(delivery_tag=method.delivery_tag)    
        
    def handle_delivery(self,message):
        decoded_message = base64.b64decode(message)
        
        parsed_json_message = json.loads(decoded_message)
        payload = parsed_json_message['payload']
        decoded_payload = base64.b64decode(payload).decode("utf-8") 
       
        log_file_name = parsed_json_message['filename']
        current_milli_time = lambda: int(round(time.time() * 1000))
        try:
            log_folder_path = os.path.join(tempfile.gettempdir(),"log_files",str(current_milli_time()))
#            log_file_path = tempfile.gettempdir() + "/log_files/" + str(current_milli_time()) + "/"
        except NameError:        
            import sys
            log_folder_path = os.path.join(os.path.dirname(os.path.abspath(sys.argv[0])), "log_files", str(current_milli_time()))

        if not os.path.exists(log_folder_path):
            os.makedirs(log_folder_path)    
            
        log_file_path = os.path.join(log_folder_path , log_file_name)
        with open(log_file_path, 'w') as outfile:
            outfile.write(decoded_payload)  
            
        all_vars = bindings(log_file_path)
        return base64.b64encode(bytes(all_vars, 'utf-8')).decode("utf-8") 
            