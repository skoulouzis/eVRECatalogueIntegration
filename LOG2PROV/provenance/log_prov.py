from prov.model import ProvDocument, Namespace, Literal, PROV, Identifier

class LogProv():
    def __init__(self,log_dic):        
        self._prov_doc = ProvDocument()
        self._prov_doc.add_namespace('remote_host', 'https://www.vre4eic.eu/remote_host/#')
        remote_host = self._prov_doc.agent('remote_host:'+log_dic['remote_host'])
        
        if('request_url_hostname' in log_dic and log_dic['request_url_hostname']):
            self._prov_doc.add_namespace('request_hostname', 'https://www.vre4eic.eu/request_hostname/#')
            request_hostname= self._prov_doc.agent('request_hostname:'+log_dic['request_url_hostname'])

        self._prov_doc.add_namespace('response_bytes', 'https://www.vre4eic.eu/response_bytes/#')
        response_bytes = self._prov_doc.entity('response_bytes:'+log_dic['response_bytes_clf'])

        self._prov_doc.add_namespace('http_status', 'https://www.vre4eic.eu/http_status/#')
        http_status = self._prov_doc.entity('http_status:'+log_dic['status'])

        if('request_url_username' in log_dic and log_dic['request_url_username']):
            self._prov_doc.add_namespace('request_url_username', 'https://www.vre4eic.eu/request_url_username/#')
            request_url_username = self._prov_doc.entity('request_url_username:'+log_dic['request_url_username'])

        self._prov_doc.add_namespace('request_url', 'https://www.vre4eic.eu/request_url/#')
        request_url = self._prov_doc.entity('request_url:'+log_dic['request_url'])

        self._prov_doc.add_namespace('http_staus_code', 'https://www.vre4eic.eu/http_staus_code/#')
        http_staus_code = self._prov_doc.entity('http_staus_code:'+log_dic['status'])

        self._prov_doc.add_namespace('request_method', 'https://www.vre4eic.eu/request_method/#')
        request_method = self._prov_doc.entity('request_method:'+log_dic['request_method'])

        self._prov_doc.add_namespace('request_http_ver', 'https://www.vre4eic.eu/request_http_ver/#')
        request_http_ver = self._prov_doc.entity('request_http_ver:'+log_dic['request_http_ver'])


        self._prov_doc.add_namespace('is', 'https://www.vre4eic.eu/is#')
        received_activity = self._prov_doc.activity('is:received')

    #    self._prov_doc.add_namespace('logger', 'https://www.vre4eic.eu/logger/#')
    #    logger = self._prov_doc.entity('logger:catalina_logger')
        self._prov_doc.generation(remote_host,activity=received_activity,time='2018-09-13T12:51:13+00:00')


    #    self._prov_doc.add_namespace('is', 'https://www.vre4eic.eu/is#')
    #    response_activity = self._prov_doc.activity('is:response')    
    
    @property
    def prov_doc(self):
        return self._prov_doc

    @prov_doc.setter
    def prov_doc(self, value):
        self._prov_doc = value

    @prov_doc.deleter
    def prov_doc(self):
        del self._prov_doc