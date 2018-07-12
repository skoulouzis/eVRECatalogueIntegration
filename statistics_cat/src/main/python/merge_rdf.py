#! /usr/bin/env python


from rdflib  import Graph, RDF, BNode, URIRef, Namespace, ConjunctiveGraph, Literal
from pathlib import Path
import sys 

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
    
    stats['in_degree'] = in_degreess
    stats['out_degree'] = out_degrees
    
    return stats
    
    #graph.serialize(destination=output, format='xml')


if __name__ == "__main__":
    stats =  get_degrees(sys.argv[1], sys.argv[2])
    #print(' in: '+ str(in_degrees) +' out: '+ str(out_degrees)+' all:')        
    
    