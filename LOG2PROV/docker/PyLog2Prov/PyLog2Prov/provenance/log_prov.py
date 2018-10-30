from prov.model import ProvDocument, Namespace, Literal, PROV, Identifier

class LogProv():
    def __init__(self,log_dic):        
        self._prov_doc = ProvDocument()
        vre_namespace = self._prov_doc.add_namespace('vre', 'https://www.vre4eic.eu/log#')
        prov_namespace = self._prov_doc.add_namespace('prov', 'http://www.w3.org/ns/prov#')
        if('request_url_username' in log_dic and log_dic['request_url_username']):
            remote_host = self._prov_doc.agent(vre_namespace['ag1'], {prov_namespace['type']: PROV["SoftwareAgent"], vre_namespace['hasIP']: log_dic['remote_host'],vre_namespace['hasUsername']:log_dic['request_url_username']})    
        else:
            remote_host = self._prov_doc.agent(vre_namespace['ag1'], {prov_namespace['type']: PROV["SoftwareAgent"], vre_namespace['hasIP']: log_dic['remote_host']})
        
        if('request_url_hostname' in log_dic and log_dic['request_url_hostname']):            
            request_hostname = self._prov_doc.agent(vre_namespace['ag2'], {
            prov_namespace['type']: PROV["SoftwareAgent"], vre_namespace['hasIP']: log_dic['remote_host']
            })
        
        request_entity = self._prov_doc.entity(vre_namespace['en1'], {vre_namespace['status']: log_dic['status'],vre_namespace['responseBytes']:log_dic['response_bytes_clf']})

        received_activity = self._prov_doc.activity(vre_namespace['ac1'], other_attributes={vre_namespace['requestURL']:log_dic['request_url'],vre_namespace['requestMethod']:log_dic['request_method'], vre_namespace['httpVersion']:log_dic['request_http_ver']})        
        self._prov_doc.generation(remote_host,activity=received_activity,time=log_dic['time_received_tz_isoformat'])
        self._prov_doc.wasAttributedTo(request_entity, received_activity)
        self._prov_doc.wasAssociatedWith(received_activity, remote_host)

    
    @property
    def prov_doc(self):
        return self._prov_doc

    @prov_doc.setter
    def prov_doc(self, value):
        self._prov_doc = value

    @prov_doc.deleter
    def prov_doc(self):
        del self._prov_doc