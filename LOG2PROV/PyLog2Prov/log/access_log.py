from .log import Log
import apache_log_parser

class AccessLog(Log):
    def __init__(self, line, server):
        self._log_line_dict = None
        if(server == 'catalina'):
            line_parser = apache_log_parser.make_parser("%h - - %t \"%r\" %>s %b")
        elif (server == 'apache'):
            line_parser = apache_log_parser.make_parser("%h <<%P>> %t %Dus \"%r\" %>s %b  \"%{Referer}i\" \"%{User-Agent}i\" %l %u")
        else:
            line_parser = apache_log_parser.make_parser("%h - - %t \"%r\" %>s %b")
        self._log_line_dict = line_parser(line)
        
    def must_do_it(self):
        super().must_do_it()
    
    @property
    def log_line_dict(self):
        return self._log_line_dict

    @log_line_dict.setter
    def log_line_dict(self, value):
        self._log_line_dict = value

    @log_line_dict.deleter
    def log_line_dict(self):
        del self._log_line_dict