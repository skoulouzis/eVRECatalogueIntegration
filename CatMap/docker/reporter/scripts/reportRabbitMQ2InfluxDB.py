#!/usr/bin/env python
import urllib2 
import base64
import json
from base64 import b64encode
import time
import requests
import sys
import urllib


#RABBIT_HOST='localhost'
#RABBIT_PORT='15672'
#RABBIT_USERNAME='guest'
#RABBIT_PASSWORD='guest'
#RABBIT_QNAME='ckan_Mapping62.x3ml'
#RABBIT_VHOST='%2F'

#INFLUX_HOST='localhost'
#INFLUX_PORT='8086'
#INFLUX_DB='mydb'




def init(INFLUX_DB,influx_base_url): 
    create_db_influx_url = influx_base_url+'/query?q=CREATE DATABASE '+INFLUX_DB
    r = requests.post(create_db_influx_url)
    print r
    

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
    

    
    if 'message_stats' in metrics and  metrics['message_stats']:
        if 'deliver_get_details' in metrics['message_stats'] and metrics['message_stats']['deliver_get_details']:
            influx_metrics['message_stats=rate'] = metrics['message_stats']['deliver_get_details']['rate']        
        
        if 'deliver_get' in  metrics['message_stats']:
            influx_metrics['message_stats=deliver_get'] = metrics['message_stats']['deliver_get']           

        if 'ack_details' in metrics['message_stats'] and metrics['message_stats']['ack_details']:
            influx_metrics['message_stats=ack_details.rate'] = metrics['message_stats']['ack_details']['rate']      
    
        if 'ack' in  metrics['message_stats']:
            influx_metrics['message_stats=ack'] = metrics['message_stats']['ack']   
    
        if 'message_stats' in metrics['message_stats'] and metrics['message_stats']['redeliver_details']:
            influx_metrics['message_stats=redeliver_details.rate'] = metrics['message_stats']['redeliver_details']['rate']               
    
        if 'redeliver' in metrics['message_stats']:
            influx_metrics['message_stats=redeliver'] = metrics['message_stats']['redeliver']       
    
        if 'deliver_no_ack_details' in metrics['message_stats'] and 'rate' in  metrics['message_stats']['deliver_no_ack_details'] and metrics['message_stats']['deliver_no_ack_details']:        
            influx_metrics['message_stats=deliver_no_ack_details.rate'] = metrics['message_stats']['deliver_no_ack_details']['rate']
    
        if 'deliver_no_ack' in metrics['message_stats']:
            influx_metrics['message_stats=deliver_no_ack'] = metrics['message_stats']['deliver_no_ack']

        if 'deliver_details' in metrics['message_stats'] and metrics['message_stats']['deliver_details']:       
            influx_metrics['message_stats=deliver_details.rate'] = metrics['message_stats']['deliver_details']['rate']
        
        if 'deliver' in metrics['message_stats']:
            influx_metrics['message_stats=deliver'] = metrics['message_stats']['deliver']
    
        if 'get_no_ack_details' in metrics['message_stats'] and metrics['message_stats']['get_no_ack_details']:       
            influx_metrics['message_stats=get_no_ack_details.rate'] = metrics['message_stats']['get_no_ack_details']['rate']
    
        if 'get_no_ack' in metrics['message_stats']:
            influx_metrics['message_stats=get_no_ack'] = metrics['message_stats']['get_no_ack']    
    
        if 'get_details' in metrics['message_stats'] and metrics['message_stats']['get_details']:  
            influx_metrics['message_stats=get_details.rate'] = metrics['message_stats']['get_details']['rate']
    
        if 'get' in metrics['message_stats']:  
            influx_metrics['message_stats=message_stats.get'] = metrics['message_stats']['get']
    
        if metrics['message_stats']['publish_details']:  
            influx_metrics['message_stats=publish_details.rate'] = metrics['message_stats']['publish_details']['rate']
    
        if 'publish' in  metrics['message_stats']:
            influx_metrics['message_stats=publish'] = metrics['message_stats']['publish']
        
        if 'rate' in metrics['messages_details']:
            influx_metrics['root=messages_details.rate'] = metrics['messages_details']['rate']
    
            
        if metrics['messages_ready_details']:
            influx_metrics['messages_ready_details=rate'] = metrics['messages_ready_details']['rate']
        
        if 'rate' in metrics['reductions_details']:
            influx_metrics['reductions_details=rate'] = metrics['reductions_details']['rate']
        
        return influx_metrics


def build_influx_line(influx_metrics,rabbit_q_name):
    data_string = ''
    if influx_metrics: 
        for key in influx_metrics:
            if(influx_metrics[key]!= None):
                data_string += 'rabbit,queue='+rabbit_q_name+','+key+' value='+str(influx_metrics[key]) +'\n'
    return data_string

    
def report(req,INFLUX_DB,influx_base_url,rabbit_q_name):
    metrics = json.load(urllib2.urlopen(req, timeout=100))
    influx_metrics = build_influx_metrics(metrics)
    influx_db_string = build_influx_line(influx_metrics,rabbit_q_name)
    r = requests.post(influx_base_url+'/write?consistency=one&precision=ms&db='+INFLUX_DB, data=influx_db_string)
    print r
    


def get_rabbit_definitions(rabbit_base_url,RABBIT_USERNAME,RABBIT_PASSWORD):
    definitions = []
    rabbit_hosts_url = rabbit_base_url+'/api/definitions'
    print rabbit_hosts_url
    definitions = json.loads(requests.get(rabbit_hosts_url, auth=(RABBIT_USERNAME, RABBIT_PASSWORD)).content)
    return definitions



#python reportRabbitMQ2InfluxDB.py localhost 15672 guest guest localhost 8086 mydb 

if __name__ == "__main__":
    RABBIT_HOST = sys.argv[1] 
    RABBIT_PORT = sys.argv[2]
    RABBIT_USERNAME = sys.argv[3]
    RABBIT_PASSWORD = sys.argv[4]
    INFLUX_HOST = sys.argv[5]
    INFLUX_PORT = sys.argv[6]
    INFLUX_DB = sys.argv[7] 
    interval = sys.argv[8] 
    print('RABBIT_HOST: '+RABBIT_HOST+' RABBIT_PORT: '+RABBIT_PORT+' RABBIT_USERNAME: '+RABBIT_PASSWORD+' RABBIT_VHOST: '+INFLUX_HOST+' INFLUX_PORT: '+INFLUX_DB)
        
    influx_base_url = 'http://'+INFLUX_HOST+':'+INFLUX_PORT
    
    init(INFLUX_DB,influx_base_url);
    
    
    rabbit_base_url = 'http://'+RABBIT_HOST+':'+RABBIT_PORT
    while True:
        definitions = get_rabbit_definitions(rabbit_base_url,RABBIT_USERNAME,RABBIT_PASSWORD)
        urls = set()
        for queues in definitions['queues']:
            for queue in queues:
                vhost = urllib.quote_plus(queues['vhost'])
                queue_name = urllib.quote_plus(queues['name'])
                rabbit_url = 'http://'+RABBIT_HOST+':'+RABBIT_PORT+'/api/queues/'+vhost+'/'+queue_name
                urls.add(rabbit_url)
                
        
        for url in urls:
            req = urllib2.Request(url)
            authorization = 'Basic     ' + b64encode('%s:%s' % (RABBIT_USERNAME, RABBIT_PASSWORD))
            req.add_header('Authorization', authorization)
            print('Reporting: '+url);
            rabbit_q_name = url.rsplit('/', 1)[-1]
            report(req,INFLUX_DB,influx_base_url,rabbit_q_name)
        time.sleep(float(interval))
    
    
    #rabbit_url = 'http://'+RABBIT_HOST+':'+RABBIT_PORT+'/api/queues/'+RABBIT_VHOST+'/'+RABBIT_QNAME
    #req = urllib2.Request(rabbit_url)
    #authorization = 'Basic ' + b64encode('%s:%s' % (RABBIT_USERNAME, RABBIT_PASSWORD))
    #req.add_header('Authorization', authorization)
    
    
    
    
    #report(req,INFLUX_DB,influx_base_url)




