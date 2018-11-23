from log.access_log import *
import optparse
from provenance.log_binding import *
from provenance.log_prov import *
import requests
from amqp.rpc_server import *
from util.util import *



    
    


def start_worker(rabbitmq_host,rabbitmq_port,rabbitmq_username,rabbitmq_password):  
    rpc_server = RPCServer(rabbitmq_host,rabbitmq_port,rabbitmq_username,rabbitmq_password)
    rpc_server.start()
    
    
if __name__ == "__main__":
    
    parser = optparse.OptionParser()
    
    parser.add_option('-r', '--rabbitmq_host', action="store", dest="rabbitmq_host", help="Worker mode: rabbitmq host. Instead of localhost you must use 127.0.0.1")
    parser.add_option('-p', '--rabbitmq_port', action="store", dest="rabbitmq_port", help="Worker mode: rabbitmq port", default="5672")
    parser.add_option('-u', '--rabbitmq_username', action="store", dest="rabbitmq_username", help="Worker mode: rabbitmq username", default="guest")
    parser.add_option('-a', '--rabbitmq_password', action="store", dest="rabbitmq_password", help="Worker mode: rabbitmq username", default="guest")
    
    parser.add_option('-f', '--file', action="store", dest="file", help="Commandline mode: input log file")
    parser.add_option('-i', '--templateID', action="store", dest="templateID", help="Commandline mode: The templateID from https://envriplus-provenance.test.fedcloud.eu/templates", default="5bbca756d6fa3376fe116ab4")
    parser.add_option("-w", action="store_true", dest="write_prov", help="Commandline mode: Should we store the PROV to https://envriplus-provenance.test.fedcloud.eu/",default=False)

    options, args = parser.parse_args()
    if options.rabbitmq_host:
        start_worker(options.rabbitmq_host,options.rabbitmq_port,options.rabbitmq_username,options.rabbitmq_password)
    elif options.file:
        all_vars = bindings(options.file)
    #    all_vars_encode = urllib.parse.quote_plus(all_vars)
        print(all_vars)
        r = requests.post("https://envriplus-provenance.test.fedcloud.eu/templates/" + 
        options.templateID + "/expand?fmt=trig&writeprov="+str(options.write_prov).lower(),
        data= ""+all_vars+""
        )
    #     
        resp = repr(r.text).replace('\\n','\n').replace('\\r','\r')
        print(resp)
    else:
        parser.print_help()