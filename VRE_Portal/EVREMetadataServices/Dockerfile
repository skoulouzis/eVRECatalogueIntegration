FROM tomcat:9


ARG EVRE_METADATA_SERVICES_VERSION=1.0-SNAPSHOT

RUN apt-get -y update && apt-get -y update && apt-get install -y git maven
WORKDIR /
RUN git clone https://github.com/skoulouzis/EVREMetadataServices.git
WORKDIR EVREMetadataServices


# RUN mvn install:install-file -Dfile=libs/NodeService-0.0.1-SNAPSHOT-classes.jar -DartifactId=NodeService -DgroupId=eu.vre4eic.evre -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DpomFile=pom.xml && mvn install:install-file -Dfile="libs/NodeService-0.0.3-SNAPSHOT-classes.jar" -DartifactId=NodeService -DgroupId=eu.vre4eic.evre -Dversion=0.0.3-SNAPSHOT -Dpackaging=jar -DpomFile=pom.xml && mvn install:install-file -Dfile="libs/BlazegraphOps-1.0-SNAPSHOT.jar" -DartifactId=BlazegraphOps -DgroupId=forth.ics -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DpomFile=pom.xml && mvn install:install-file -Dfile="libs/VirtuosoOpsImpl-1.0-SNAPSHOT.jar" -DartifactId=VirtuosoOpsImpl -DgroupId=forth.ics -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar -DpomFile=pom.xml

RUN mvn install:install-file -Dfile=libs/BlazegraphOps-1.0-SNAPSHOT.jar -DgroupId=forth.ics -DartifactId=BlazegraphOps -Dversion=1.0-SNAPSHOT -Dpackaging=jar && mvn install:install-file -Dfile=libs/NodeService-0.0.3-SNAPSHOT-classes.jar -DgroupId=eu.vre4eic.evre -DartifactId=NodeService -Dversion=0.0.3-SNAPSHOT -Dpackaging=jar && mvn install:install-file -Dfile=libs/VirtuosoOpsImpl-1.0-SNAPSHOT.jar -DgroupId=forth.ics -DartifactId=VirtuosoOpsImpl -Dversion=1.0-SNAPSHOT -Dpackaging=jar


WORKDIR /EVREMetadataServices
RUN mvn install
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