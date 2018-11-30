# FROM visity/webdav
FROM httpd:2.4


RUN apt-get -y update && apt-get -y install wget unzip dnsutils
WORKDIR /usr/local/apache2/htdocs/
RUN wget https://github.com/QCAPI-DRIP/eVRE-ENVRI/releases/download/aplha-gis/vre4eic_repo.zip
RUN unzip vre4eic_repo.zip
RUN rm vre4eic_repo.zip
ADD gis-plugin.xml /usr/local/apache2/htdocs/gis-plugin.xml
ADD pluginlist.xml /usr/local/apache2/htdocs/pluginlist.xml
RUN rm /usr/local/apache2/htdocs/index.html 

# CMD myip="$(dig +short myip.opendns.com @resolver1.opendns.com)" && sed "s/REPOSETORY_IP/http:\/\/$myip:8084\/vre4eic_repo/g" -i  /var/www/gis-plugin.xml && /set_htpasswd.sh && nginx -g "daemon off;" 

CMD myip="$(dig +short myip.opendns.com @resolver1.opendns.com)" && sed "s/REPOSETORY_IP/http:\/\/$myip:8084\/vre4eic_repo/g" -i /usr/local/apache2/htdocs/gis-plugin.xml && httpd-foreground

#docker build -t taverna-gis-reposetory .
