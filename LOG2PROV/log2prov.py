from log.access_log import *
from provenance.log_prov import *
from provenance.log_binding import *
import os
import urllib.parse



def namespace2str(namespaces):
    namespace_str = ''
    for key in namespaces:
        namespace_str+='@prefix '+key+': <'+namespaces[key]+'> .\n'
    namespace_str = namespace_str.rstrip()
    return namespace_str.rstrip()    
    
    
def vars(log_files_path):    
    directory = os.fsencode(log_files_path)
    namespaces = {}
    vars = ''
    binding=''
    for file in os.listdir(directory):
        filename = os.fsdecode(file)
        if filename.endswith(".txt") or filename.endswith(".log"): 
            with open(os.path.join(log_files_path, filename)) as f:
                lines = f.readlines()
            for line in lines:
                access_log = AccessLog(line,'catalina')
#                print(access_log.log_line_dict)
#                doc = LogProv(access_log.log_line_dict)
                log_binding = LogBinding(access_log.log_line_dict)
                for namespace in log_binding.namespaces:
                    namespaces.update(namespace)
                vars+=log_binding.vars_str
    binding+=namespace2str(namespaces)+'\n'+vars
    return binding


    
if __name__ == "__main__":
    log_files_path = 'files'
    all_vars = vars(log_files_path)
    all_vars_encode = urllib.parse.quote_plus(all_vars)
    print('https://envriplus-provenance.test.fedcloud.eu/templates/5bb768bad6fa335484d16d6f/expand?fmt=trig&bindings='+all_vars_encode)
    