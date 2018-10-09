class LogVar():
    def __init__(self, var_name, prov_type, template_value_type,value):
        self._var_name = var_name
        self._prov_type = prov_type
        self._template_value_type = template_value_type
        self._value = value;
        

    
    @property
    def var_name(self):
        return self._var_name

    @var_name.setter
    def var_name(self, value):
        self._var_name = value

    @var_name.deleter
    def var_name(self):
        del self._var_name
        
    @property
    def prov_type(self):
        return self._prov_type

    @prov_type.setter
    def prov_type(self, value):
        self._prov_type = value

    @prov_type.deleter
    def prov_type(self):
        del self._prov_type        
        
    @property
    def template_value_type(self):
        return self._template_value_type

    @template_value_type.setter
    def template_value_type(self, value):
        self._template_value_type = value

    @template_value_type.deleter
    def template_value_type(self):
        del self._template_value_type      
        
        
    @property
    def value(self):
        return self._value

    @value.setter
    def value(self, value):
        self._value = value

    @value.deleter
    def value(self):
        del self._value           