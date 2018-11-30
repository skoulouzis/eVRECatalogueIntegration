FROM visity/webdav



RUN apt-get -y update && apt-get -y install wget unzip dnsutils
RUN wget https://github.com/QCAPI-DRIP/eVRE-ENVRI/releases/download/aplha-gis/vre4eic_repo.zip
RUN unzip vre4eic_repo.zip
RUN mv vre4eic_repo /var/www/
RUN rm vre4eic_repo.zip
ADD gis-plugin.xml /var/www/gis-plugin.xml
ADD pluginlist.xml /var/www/pluginlist.xml


CMD myip="$(dig +short myip.opendns.com @resolver1.opendns.com)" && sed "s/REPOSETORY_IP/http:\/\/$myip:8084\/vre4eic_repo/g" -i  /var/www/gis-plugin.xml && /set_htpasswd.sh && nginx -g "daemon off;" 

#docker build -t taverna-gis-reposetory .
