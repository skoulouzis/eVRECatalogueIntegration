FROM tomcat:8


ARG EVRE_METADATA_SERVICES_VERSION=1.0-SNAPSHOT

RUN apt-get -y update && apt-get -y upgrade && apt-get install -y git maven openjdk-8-jdk unzip
WORKDIR /
# RUN git clone https://github.com/skoulouzis/EVREMetadataServices.git

RUN mkdir -p /EVREMetadataServices/target
RUN mkdir -p /EVREMetadataServices/libs
WORKDIR EVREMetadataServices

# RUN mvn install:install-file -Dfile=libs/NodeService-0.0.1-SNAPSHOT-classes.jar -DartifactId=NodeService -DgroupId=eu.vre4eic.evre -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DpomFile=pom.xml && mvn install:install-file -Dfile="libs/NodeService-0.0.3-SNAPSHOT-classes.jar" -DartifactId=NodeService -DgroupId=eu.vre4eic.evre -Dversion=0.0.3-SNAPSHOT -Dpackaging=jar -DpomFile=pom.xml && mvn install:install-file -Dfile="libs/BlazegraphOps-1.0-SNAPSHOT.jar" -DartifactId=BlazegraphOps -DgroupId=forth.ics -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DpomFile=pom.xml && mvn install:install-file -Dfile="libs/VirtuosoOpsImpl-1.0-SNAPSHOT.jar" -DartifactId=VirtuosoOpsImpl -DgroupId=forth.ics -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DpomFile=pom.xml

ADD libs/virtjdbc3.jar libs
ADD libs/virtjdbc4.jar libs
ADD libs/virt_jena.jar libs
ADD libs/virt_sesame2.jar libs
ADD libs/BlazegraphOps-1.0-SNAPSHOT.jar libs
ADD libs/NodeService-0.0.3-SNAPSHOT-classes.jar libs
ADD libs/VirtuosoOpsImpl-1.0-SNAPSHOT.jar libs
# ADD libs/jta-1.0.1b.jar libs
ADD EVREMetadataServices-1.0-SNAPSHOT.zip /EVREMetadataServices/target/
WORKDIR /EVREMetadataServices/target/
RUN unzip EVREMetadataServices-1.0-SNAPSHOT.zip

WORKDIR /EVREMetadataServices
# RUN mvn install:install-file -Dfile=libs/BlazegraphOps-1.0-SNAPSHOT.jar -DgroupId=forth.ics -DartifactId=BlazegraphOps -Dversion=1.0-SNAPSHOT -Dpackaging=jar && mvn install:install-file -Dfile=libs/NodeService-0.0.3-SNAPSHOT-classes.jar -DgroupId=eu.vre4eic.evre -DartifactId=NodeService -Dversion=0.0.3-SNAPSHOT -Dpackaging=jar && mvn install:install-file -Dfile=libs/VirtuosoOpsImpl-1.0-SNAPSHOT.jar -DgroupId=forth.ics -DartifactId=VirtuosoOpsImpl -Dversion=1.0-SNAPSHOT -Dpackaging=jar &&  mvn install:install-file -Dfile="libs\virtjdbc4.jar" -DartifactId=virtjdbc4 -DgroupId=gr.forth.ics -Dversion=7-20140918 -Dpackaging=jar && mvn install:install-file -Dfile="libs\virtjdbc3.jar" -DartifactId=virtjdbc3 -DgroupId=gr.forth.ics -Dversion=7-20140918 -Dpackaging=jar && mvn install:install-file -Dfile="libs\virt_sesame2.jar" -DartifactId=virtsesame -DgroupId=gr.forth.ics -Dversion=2.0 -Dpackaging=jar && mvn install:install-file -Dfile="libs\virt_jena.jar" -DartifactId=virtjena -DgroupId=gr.forth.ics -Dversion=7-20140918 -Dpackaging=jar 

# RUN mvn install:install-file -Dfile=libs/jta-1.0.1b.jar -DgroupId=javax.transaction -DartifactId=jta -Dversion=1.0.1B -Dpackaging=jar 




WORKDIR /EVREMetadataServices
# RUN JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn install

RUN cp -r target/EVREMetadataServices-$EVRE_METADATA_SERVICES_VERSION /usr/local/tomcat/webapps/EVREMetadataServices

CMD echo "triplestore.url = $TRIPLESTORE_URL" > /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "triplestore.namespace = $TRIPLESTORE_NAMESPACE" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "virtuoso.url = $VIRTUOSO_URL" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "virtuoso.username = $VIRTUOSO_USERNAME" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "virtuoso.password = $VIRTUOSO_PASSWORD" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "virtuoso.port = $VIRTUOSO_PORT" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "virtuoso.rest.url = $VIRTUOSO_REST_URL" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \     
    echo "metadata.endpoint = $METADATA_ENDPOINT" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "nodelink.url = $NODELINK_URL" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "nodeservice.endpoint = $NODELINK_ENDPOINT" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "workflows.graph = $WORKFLOWS_GRAPH" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \  
    echo "workflows.graph.label = $WORKFLOWS_GRAPH_LABEL" >> /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \ 
    cat /usr/local/tomcat/webapps/EVREMetadataServices/WEB-INF/classes/config.properties && \
    catalina.sh run  

#sudo docker build -t evre_metadata_service .
#

