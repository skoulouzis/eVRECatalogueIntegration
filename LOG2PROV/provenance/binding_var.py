class LogVar():
    def __init__(self, var_name, prefix, value_type, value):
        self._var_name = var_name
        self._prefix = prefix
        self._value_type = value_type
        self._value = value;
    
    @property
    def var_name(self):
        return self._var_name
        
    @property
    def prefix(self):
        return self._prefix   
        
    @property
    def value_type(self):
        return self._value_type    
        
        
    @property
    def value(self):
        return self._value     