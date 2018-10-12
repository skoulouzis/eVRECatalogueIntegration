FROM tomcat:9


ARG NODE_SERVICE_VERSION=0.0.3-SNAPSHOT

RUN apt-get -y update && apt-get -y upgrade && apt-get install -y git maven
WORKDIR /
RUN git clone -b canonicalprototype https://github.com/vre4eic/NodeService.git
WORKDIR NodeService

RUN mvn install -Dskiptests=true
# RUN cp -r target/NodeService-$NODE_SERVICE_VERSION /usr/local/tomcat/webapps/NodeService
# 
# CMD catalina.sh run  