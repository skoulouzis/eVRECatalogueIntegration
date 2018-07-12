#! /usr/bin/env python


from rdflib  import Graph, RDF, BNode, URIRef, Namespace, ConjunctiveGraph, Literal
from pathlib import Path
import sys 
import ftplib
import tempfile
import shutil
import os
import json
import urllib
import base64
import pika
import urlparse


def get_degrees(directory_in_str):
    stats = {}
    graph = Graph()
    pathlist = Path(directory_in_str).glob('**/*.rdf')
    nodes = set()
    edges = []
    for path in pathlist:
        path_in_str = str(path)
        graph.parse(path_in_str)
    
    stats['length'] = len(graph)
    stats['num_of_nodes'] =  len(graph.all_nodes())
    #print('graph length: %d, nodes: %d' % (len(graph), len(graph.all_nodes())))
    
    #print ('all node out-degrees:')
    in_degrees = {}
    out_degrees = {}
    for node in graph.all_nodes():
        out_triple = graph.triples([node, None, None])
        out_degree = len(list(out_triple))
        node_id = urllib.quote_plus(node.encode('utf-8'))
        out_degrees[node_id] = out_degree
        
        in_triple = graph.triples([None, None, node])
        in_degree = len(list(in_triple))
        in_degrees[node_id] = in_degree
    
    stats['in_degree'] = in_degrees
    stats['out_degree'] = out_degrees
    
    return stats
    
    #graph.serialize(destination=output, format='xml')

def get_files(url):
    url = urlparse.urlparse(url)
    ftp = ftplib.FTP(url.hostname) 
    ftp.login(url.username,url.password) 
    
    filenames = ftp.nlst(url.path)
    dirpath = tempfile.mkdtemp()
        
    for filename in filenames:
        local_filename = os.path.join(dirpath, filename) 
        file = open(local_filename, 'wb')
        ftp.retrbinary('RETR '+ url.path +'/'+filename, file.write)
        file.close()
        
    ftp.quit()
    
    return dirpath


def init_chanel(args):
    global rabbitmq_host
    if len(args) > 1:
        rabbitmq_host = args[1]
        queue_name = args[2] #rdf_location
    else:
        rabbitmq_host = '127.0.0.1'
    
    connection = pika.BlockingConnection(pika.ConnectionParameters(host=rabbitmq_host))
    channel = connection.channel()
    channel.queue_declare(queue=queue_name)
    return channel


def start(channel):    
    channel.basic_qos(prefetch_count=1)
    channel.basic_consume(on_request, queue=queue_name)
    channel.start_consuming()
    
def on_request(ch, method, props, body):
    #path = get_files('ftp://user:12345@localhost/ckan_Mapping62.x3ml')
    path = get_files(body)
    stats =  get_degrees(path)
    json_stats = json.dumps(stats)
    messageDataEnc = base64.b64encode(json_stats)
    
    ch.basic_publish(exchange='',
                     routing_key=props.reply_to,
                     properties=pika.BasicProperties(correlation_id=\
                     props.correlation_id),
                     body=str(messageDataEnc))
    ch.basic_ack(delivery_tag=method.delivery_tag)      
    


    

if __name__ == "__main__":
    channel = init_chanel(sys.argv)
    global queue_name
    queue_name = sys.argv[2]
    start(channel)
    
    