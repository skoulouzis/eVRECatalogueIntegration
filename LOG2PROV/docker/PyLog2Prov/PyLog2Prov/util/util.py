from log.access_log import *
import os
from provenance.log_binding import *
from provenance.log_prov import *


def bindings(log_files_path):    
    namespaces = {}
    entities_dict = {}
    binding=''
    vars_str=''
        
    with open(os.path.join(log_files_path)) as f:
        lines = f.readlines()
    value_index=0;
    for line in lines:
        access_log = AccessLog(line,'catalina')
#                doc = LogProv(access_log.log_line_dict)
        log_binding = LogBinding(access_log.log_line_dict,value_index)
        for namespace in log_binding.namespaces:
            namespaces.update(namespace)

        for entity in log_binding.entity_types:
            entities_dict[entity.entity_name] = entity

        for var in log_binding.binding_vars:
            vars_str+=var2str(var)+'\n'
        value_index += 1
        vars_str+='\n'                  
    binding+=namespace2str(namespaces)
    binding+='\n'
    binding+='\n'
    binding+= entiti_types2str(entities_dict.values())
    binding+='\n'
    binding+='\n'
    binding+=vars_str
    return binding


def var2str(var):        
    return 'var:'+var.var_name+' '+var.prefix+':'+var.value_type+' '+var.value+' .'


def namespace2str(namespaces):
    namespace_str = ''
    for key in namespaces:
        namespace_str+='@prefix '+key+': <'+namespaces[key]+'> .\n'
    return namespace_str.rstrip()    

def entiti_types2str(entities):
    entities_str = ''
    for entity in entities:
        entities_str+='var:'+entity.entity_name+' a '+entity.prefix+':'+entity.entity_type+' .\n'
    return entities_str.rstrip()   
