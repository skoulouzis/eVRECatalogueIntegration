#! /usr/bin/env python


from rdflib  import Graph, RDF, BNode, URIRef, Namespace, ConjunctiveGraph, Literal
from pathlib import Path
import sys 
import ftplib
import tempfile
import shutil
import os



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
        out_degrees[node] = out_degree
        
        in_triple = graph.triples([None, None, node])
        in_degree = len(list(in_triple))
        in_degrees[node] = in_degree
    
    stats['in_degree'] = in_degrees
    stats['out_degree'] = out_degrees
    
    return stats
    
    #graph.serialize(destination=output, format='xml')

def get_files(host,path):
    ftp = ftplib.FTP(host) 
    ftp.login('user','12345') 
    
    filenames = ftp.nlst(path)
    dirpath = tempfile.mkdtemp()
        
    for filename in filenames:
        local_filename = os.path.join(dirpath, filename) 
        file = open(local_filename, 'wb')
        ftp.retrbinary('RETR '+ path +'/'+filename, file.write)
        file.close()
        
    ftp.quit()
    
    return dirpath
    

if __name__ == "__main__":
    path = get_files('localhost','ckan_Mapping62.x3ml')

    stats =  get_degrees(path)
    
    print stats
    
    