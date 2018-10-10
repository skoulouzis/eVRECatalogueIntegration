from log.access_log import *
from provenance.log_prov import *
from provenance.log_binding import *
import os
import urllib.parse
import requests


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
    
    
def bindings(log_files_path):    
    directory = os.fsencode(log_files_path)
    namespaces = {}
    entities_dict = {}
    binding=''
    vars_str=''
    for file in os.listdir(directory):
        filename = os.fsdecode(file)
        if filename.endswith(".txt") or filename.endswith(".log"): 
            with open(os.path.join(log_files_path, filename)) as f:
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


    
if __name__ == "__main__":
    log_files_path = 'files'
    all_vars = bindings(log_files_path)
    all_vars_encode = urllib.parse.quote_plus(all_vars)
#    print(all_vars)
    templateID='5baa34e8d6fa333791066dcf'
    r = requests.post("https://envriplus-provenance.test.fedcloud.eu/templates/" + 
    templateID + "/expand?fmt=trig&writeprov=false",
    data= """
        @prefix var: <http://openprovenance.org/var#> .
        @prefix vre: <https://www.vre4eic.eu/log#> .
        @prefix prov: <http://www.w3.org/ns/prov#> .
        @prefix tmpl: <http://openprovenance.org/tmpl#> .

        var:bytes a prov:Entity .
        var:time a prov:Entity .
        var:agent a prov:Entity .
        var:method a prov:Entity .
        var:url a prov:Entity .
        var:version a prov:Entity .
        var:ip a prov:Entity .
        var:status a prov:Entity .

        var:agent tmpl:value_0 vre:ag0 .
        var:ip tmpl:2dvalue_0_0 "10.255.0.2" .
        var:status tmpl:2dvalue_0_0 "200" .
        var:bytes tmpl:2dvalue_0_0 "1231" .
        var:url tmpl:2dvalue_0_0 "/cue/rest/?wadl" .
        var:method tmpl:2dvalue_0_0 "GET" .
        var:version tmpl:2dvalue_0_0 "1.1" .
        var:time tmpl:value_0 "2018-09-13T12:51:04+00:00" .

        var:agent tmpl:value_1 vre:ag1 .
        var:ip tmpl:2dvalue_1_0 "10.255.0.2" .
        var:status tmpl:2dvalue_1_0 "200" .
        var:bytes tmpl:2dvalue_1_0 "28172" .
        var:url tmpl:2dvalue_1_0 "/cue/rest/argo/get?geospatial_lat_min=31.000&geospatial_lat_max=38.200&geospatial_lon_min=147.000&geospatial_lon_max=147.100&flowlabel=" .
        var:method tmpl:2dvalue_1_0 "GET" .
        var:version tmpl:2dvalue_1_0 "1.1" .
        var:time tmpl:value_1 "2018-09-13T12:51:13+00:00" .

        var:agent tmpl:value_2 vre:ag2 .
        var:ip tmpl:2dvalue_2_0 "10.255.0.2" .
        var:status tmpl:2dvalue_2_0 "200" .
        var:bytes tmpl:2dvalue_2_0 "1103475" .
        var:url tmpl:2dvalue_2_0 "/cue/rest/argo/get?geospatial_lat_min=31.000" .
        var:method tmpl:2dvalue_2_0 "GET" .
        var:version tmpl:2dvalue_2_0 "1.1" .
        var:time tmpl:value_2 "2018-09-13T13:12:56+00:00" .

        var:agent tmpl:value_3 vre:ag3 .
        var:ip tmpl:2dvalue_3_0 "10.255.0.2" .
        var:status tmpl:2dvalue_3_0 "200" .
        var:bytes tmpl:2dvalue_3_0 "319806" .
        var:url tmpl:2dvalue_3_0 "/cue/rest/argo/get?geospatial_lat_min=31.000" .
        var:method tmpl:2dvalue_3_0 "GET" .
        var:version tmpl:2dvalue_3_0 "1.1" .
        var:time tmpl:value_3 "2018-09-13T13:12:58+00:00" .

        var:agent tmpl:value_4 vre:ag4 .
        var:ip tmpl:2dvalue_4_0 "10.255.0.2" .
        var:status tmpl:2dvalue_4_0 "200" .
        var:bytes tmpl:2dvalue_4_0 "28172" .
        var:url tmpl:2dvalue_4_0 "/cue/rest/argo/get?geospatial_lat_min=31.000&geospatial_lat_max=38.200&geospatial_lon_min=147.000&geospatial_lon_max=147.100" .
        var:method tmpl:2dvalue_4_0 "GET" .
        var:version tmpl:2dvalue_4_0 "1.1" .
        var:time tmpl:value_4 "2018-09-13T13:13:50+00:00" .

        var:agent tmpl:value_5 vre:ag5 .
        var:ip tmpl:2dvalue_5_0 "10.255.0.2" .
        var:status tmpl:2dvalue_5_0 "200" .
        var:bytes tmpl:2dvalue_5_0 "28172" .
        var:url tmpl:2dvalue_5_0 "/cue/rest/argo/get?geospatial_lat_min=31.000&geospatial_lat_max=38.200&geospatial_lon_min=147.000&geospatial_lon_max=147.100" .
        var:method tmpl:2dvalue_5_0 "GET" .
        var:version tmpl:2dvalue_5_0 "1.1" .
        var:time tmpl:value_5 "2018-09-13T13:13:51+00:00" .
    """
    )
     

    print(repr(r.text))
#    print(repr(r.headers))