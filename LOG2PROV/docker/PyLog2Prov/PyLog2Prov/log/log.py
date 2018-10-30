from abc import ABCMeta, abstractmethod
import six

@six.add_metaclass(ABCMeta)
class Log():

    @abstractmethod
    def must_do_it(self):
        pass        