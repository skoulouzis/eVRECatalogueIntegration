#!/usr/bin/env python
import urllib2
import base64
import json
from base64 import b64encode
import time
import requests
import sys


#RABBIT_HOST='localhost'
#RABBIT_PORT='15672'
#RABBIT_USERNAME='guest'
#RABBIT_PASSWORD='guest'
#RABBIT_QNAME='ckan_Mapping62.x3ml'
#RABBIT_VHOST='%2F'

#INFLUX_HOST='localhost'
#INFLUX_PORT='8086'
#INFLUX_DB='mydb'




def init(INFLUX_HOST,INFLUX_PORT,INFLUX_DB,influx_base_url): 
    create_db_influx_url = influx_base_url+'/query?q=CREATE DATABASE '+INFLUX_DB
    r = requests.post(create_db_influx_url)
    

def build_influx_metrics(metrics):
    influx_metrics = {}
    
    if metrics['backing_queue_status']:
        influx_metrics['backing_queue_status=avg_ack_egress_rate'] = metrics['backing_queue_status']['avg_ack_egress_rate']
        influx_metrics['backing_queue_status=avg_ack_ingress_rate'] =  metrics['backing_queue_status']['avg_ack_ingress_rate']
        influx_metrics['backing_queue_status=avg_egress_rate'] = metrics['backing_queue_status']['avg_egress_rate']
        influx_metrics['backing_queue_status=avg_egress_rate'] = metrics['backing_queue_status']['avg_ingress_rate']
        influx_metrics['backing_queue_status=len'] = metrics['backing_queue_status']['len']
    
    
    
    influx_metrics['root=consumer_utilisation'] = metrics['consumer_utilisation']
    influx_metrics['root=consumers'] = metrics['consumers']
    influx_metrics['root=memory'] =  metrics['memory']      
    influx_metrics['root=message_bytes'] = metrics['message_bytes']   
    influx_metrics['root=message_bytes_paged_out'] = metrics['message_bytes_paged_out']    
    influx_metrics['root=message_bytes_persistent'] = metrics['message_bytes_persistent']           
    influx_metrics['root=message_bytes_ram'] = metrics['message_bytes_ram']        
    influx_metrics['root=message_bytes_ready'] = metrics['message_bytes_ready']       
    influx_metrics['root=message_bytes_unacknowledged'] = metrics['message_bytes_unacknowledged']     
    influx_metrics['root=messages_paged_out'] = metrics['messages_paged_out']
    influx_metrics['root=messages_persistent'] = metrics['messages_persistent']
    influx_metrics['root=messages_ram'] = metrics['messages_ram']
    influx_metrics['root=messages_ready'] = metrics['messages_ready']
    influx_metrics['root=messages_ready'] = metrics['messages_ready']        
    influx_metrics['root=messages_ready_ram'] = metrics['messages_ready_ram']
    influx_metrics['root=messages_unacknowledged'] = metrics['messages_unacknowledged']    
    influx_metrics['root=messages_unacknowledged_ram'] = metrics['messages_unacknowledged_ram']
    influx_metrics['root=reductions'] = metrics['reductions']
    
    if metrics['garbage_collection']:
        influx_metrics['garbage_collection=minor_gcs'] = metrics['garbage_collection']['minor_gcs']
        influx_metrics['garbage_collection=fullsweep_after'] = metrics['garbage_collection']['fullsweep_after']    
        influx_metrics['garbage_collection=min_heap_size'] = metrics['garbage_collection']['min_heap_size']    
        influx_metrics['garbage_collection=min_heap_size'] = metrics['garbage_collection']['min_bin_vheap_size']        
        influx_metrics['garbage_collection=min_heap_size'] = metrics['garbage_collection']['max_heap_size']    
        influx_metrics['garbage_collection=min_heap_size'] = metrics['garbage_collection']['max_heap_size']        
    

    
    if metrics['message_stats']:
        if metrics['message_stats']['deliver_get_details']:
            influx_metrics['message_stats=.rate'] = metrics['message_stats']['deliver_get_details']['rate']        
        influx_metrics['message_stats=deliver_get'] = metrics['message_stats']['deliver_get']           

        if metrics['message_stats']['ack_details']:
            influx_metrics['message_stats=ack_details.rate'] = metrics['message_stats']['ack_details']['rate']      
    
        influx_metrics['message_stats=ack'] = metrics['message_stats']['ack']   
    
        if metrics['message_stats']['redeliver_details']:
            influx_metrics['message_stats=redeliver_details.rate'] = metrics['message_stats']['redeliver_details']['rate']               
    
        influx_metrics['message_stats=redeliver'] = metrics['message_stats']['redeliver']       
    
        if metrics['message_stats']['deliver_no_ack_details']:        
            influx_metrics['message_stats=deliver_no_ack_details.rate'] = metrics['message_stats']['deliver_no_ack_details']['rate']
    
        influx_metrics['message_stats=deliver_no_ack'] = metrics['message_stats']['deliver_no_ack']

        if metrics['message_stats']['deliver_details']:       
            influx_metrics['message_stats=deliver_details.rate'] = metrics['message_stats']['deliver_details']['rate']
        
        influx_metrics['message_stats=deliver'] = metrics['message_stats']['deliver']
    
        if metrics['message_stats']['get_no_ack_details']:       
            influx_metrics['message_stats=get_no_ack_details.rate'] = metrics['message_stats']['get_no_ack_details']['rate']
    
        influx_metrics['message_stats=get_no_ack'] = metrics['message_stats']['get_no_ack']    
    
        if metrics['message_stats']['get_details']:  
            influx_metrics['message_stats=get_details.rate'] = metrics['message_stats']['get_details']['rate']
    
        if metrics['message_stats']:  
            influx_metrics['message_stats=message_stats.get'] = metrics['message_stats']['get']
    
        if metrics['message_stats']['publish_details']:  
            influx_metrics['message_stats=publish_details.rate'] = metrics['message_stats']['publish_details']['rate']
    
        influx_metrics['message_stats=publish'] = metrics['message_stats']['publish']
        
        influx_metrics['root=messages_details.rate'] = metrics['messages_details']['rate']
    
            
        if metrics['messages_ready_details']:
            influx_metrics['messages_ready_details=rate'] = metrics['messages_ready_details']['rate']
        
        influx_metrics['reductions_details=rate'] = metrics['reductions_details']['rate']
        
        return influx_metrics


def build_influx_line(influx_metrics):
    data_string = ''
    for key in influx_metrics:
        if(influx_metrics[key]!= None):
            data_string += 'rabbit,queue='+RABBIT_QNAME+','+key+' value='+str(influx_metrics[key]) +'\n'
    return data_string

    
def report(RABBIT_HOST,RABBIT_PORT,RABBIT_VHOST,RABBIT_QNAME,RABBIT_USERNAME,RABBIT_PASSWORD,INFLUX_DB,influx_base_url):
    rabbit_url = 'http://'+RABBIT_HOST+':'+RABBIT_PORT+'/api/queues/'+RABBIT_VHOST+'/'+RABBIT_QNAME
    print rabbit_url
    req = urllib2.Request(rabbit_url)
    authorization = 'Basic ' + b64encode('%s:%s' % (RABBIT_USERNAME, RABBIT_PASSWORD))
    req.add_header('Authorization', authorization)
    
    while True:
        metrics = json.load(urllib2.urlopen(req, timeout=100))
        influx_metrics = build_influx_metrics(metrics)

        influx_db_string = build_influx_line(influx_metrics)
        print influx_db_string
        r = requests.post(influx_base_url+'/write?consistency=one&precision=ms&db='+INFLUX_DB, data=influx_db_string)  
        print r
        time.sleep(60)
    





#python reportRabbitMQ2InfluxDB.py  localhost 15672 guest guest ckan_Mapping62.x3ml %2F  localhost 8086 mydb
if __name__ == "__main__":
    RABBIT_HOST = sys.argv[1] 
    RABBIT_PORT = sys.argv[2]
    RABBIT_USERNAME = sys.argv[3]
    RABBIT_PASSWORD = sys.argv[4]
    RABBIT_QNAME = sys.argv[5]
    RABBIT_VHOST = sys.argv[6]
    INFLUX_HOST = sys.argv[7]
    INFLUX_PORT = sys.argv[8]
    INFLUX_DB = sys.argv[9] 
        
    influx_base_url = 'http://'+INFLUX_HOST+':'+INFLUX_PORT
    
    init(INFLUX_HOST,INFLUX_PORT,INFLUX_DB,influx_base_url);
    report(RABBIT_HOST,RABBIT_PORT,RABBIT_VHOST,RABBIT_QNAME,RABBIT_USERNAME,RABBIT_PASSWORD,INFLUX_DB,influx_base_url)




