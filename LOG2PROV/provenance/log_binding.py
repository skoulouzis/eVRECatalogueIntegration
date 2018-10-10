from .binding_var import *
from .entity_type import *


class LogBinding():
    def __init__(self, log_dic, value_index):
        self._binding_dict = {}
        namespaces = [{'prov':'http://www.w3.org/ns/prov#'}, {'tmpl':'http://openprovenance.org/tmpl#'}, {'var':'http://openprovenance.org/var#'}, {'vre':'https://www.vre4eic.eu/log#'}]
        self._binding_dict['namespaces'] = namespaces
        entity_types = []
        binding_vars = []
        if('request_url_username' in log_dic and log_dic['request_url_username']):
            binding_vars.append(LogVar('request_url_username', 'tmpl', '2dvalue_' + str(value_index) + '_0', '"' + log_dic['request_url_username'] + '"'))
            entity_types.append(EntityType('request_url_username', 'prov', 'Entity'))
            
        binding_vars.append(LogVar('agent', 'tmpl', 'value_' + str(value_index), 'vre:ag' + str(value_index)))
        entity_types.append(EntityType('agent', 'prov', 'Entity'))
        binding_vars.append(LogVar('ip', 'tmpl', '2dvalue_' + str(value_index) + '_0', '"' + log_dic['remote_host'] + '"'))
        entity_types.append(EntityType('ip', 'prov', 'Entity'))
        binding_vars.append(LogVar('status', 'tmpl', '2dvalue_' + str(value_index) + '_0', '"' + log_dic['status'] + '"'))
        entity_types.append(EntityType('status', 'prov', 'Entity'))
        binding_vars.append(LogVar('bytes', 'tmpl', '2dvalue_' + str(value_index) + '_0', '"' + log_dic['response_bytes_clf'] + '"'))
        entity_types.append(EntityType('bytes', 'prov', 'Entity'))
        binding_vars.append(LogVar('url', 'tmpl', '2dvalue_' + str(value_index) + '_0', '"' + log_dic['request_url'] + '"'))
        entity_types.append(EntityType('url', 'prov', 'Entity'))
        binding_vars.append(LogVar('method', 'tmpl', '2dvalue_' + str(value_index) + '_0', '"' + log_dic['request_method'] + '"'))
        entity_types.append(EntityType('method', 'prov', 'Entity'))
        binding_vars.append(LogVar('version', 'tmpl', '2dvalue_' + str(value_index) + '_0', '"' + log_dic['request_http_ver'] + '"'))
        entity_types.append(EntityType('version', 'prov', 'Entity'))
        binding_vars.append(LogVar('time', 'tmpl', 'value_' + str(value_index), '"' + log_dic['time_received_tz_isoformat'] + '"'))
        entity_types.append(EntityType('time', 'prov', 'Entity'))
        
        self._binding_dict['vars'] = binding_vars
        self._binding_dict['entity_types'] = entity_types
        

    
    @property
    def binding_dict(self):
        return self._binding_dict

    @binding_dict.setter
    def binding_dict(self, value):
        self._binding_dict = value

    @binding_dict.deleter
    def binding_dict(self):
        del self._binding_dict
            
    @property
    def binding_vars(self):
        return self._binding_dict['vars'] 
    
    @property
    def namespaces(self):
        return self._binding_dict['namespaces']
    
    @property
    def entity_types(self):
        return self._binding_dict['entity_types']    