class EntityType():
    def __init__(self, entity_name, prefix, entity_type):
        self._entity_name = entity_name
        self._entity_type = entity_type
        self._prefix = prefix
    
    @property
    def entity_name(self):
        return self._entity_name
        
    @property
    def entity_type(self):
        return self._entity_type     
    
    @property
    def prefix(self):
        return self._prefix        