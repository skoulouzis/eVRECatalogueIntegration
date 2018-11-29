FROM visity/webdav




ADD gis-plugin.xml /var/www/
ADD pluginlist.xml /var/www/
ADD vre4eic_repo.zip /var/www/vre4eic_repo.zip
WORKDIR  /var/www/
RUN unzip vre4eic_repo.zip
RUN rm vre4eic_repo.zip
RUN rm -r /var/www/html

#docker build -t taverna-gis-reposetory .
