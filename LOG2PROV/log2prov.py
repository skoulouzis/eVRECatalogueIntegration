from log.access_log import *
from provenance.log_prov import *
import os

if __name__ == "__main__":
    log_files_path = 'files'
    
    directory = os.fsencode(log_files_path)

    for file in os.listdir(directory):
        filename = os.fsdecode(file)
        if filename.endswith(".txt") or filename.endswith(".log"): 
            with open(os.path.join(log_files_path, filename)) as f:
                lines = f.readlines()
            for line in lines:
#                print(line)
                access_log = AccessLog(line,'catalina')
#                print(access_log.log_line_dict)
                doc = LogProv(access_log.log_line_dict)
                print(doc.prov_doc.get_provn())
    
    
    
    
    