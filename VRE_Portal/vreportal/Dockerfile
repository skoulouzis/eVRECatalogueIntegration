FROM java:8

RUN apt-get -y update && apt-get -y upgrade && apt-get install -y git maven

WORKDIR / 
RUN git clone https://github.com/vre4eic/vreportal.git
WORKDIR vreportal
RUN JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn install

ADD evre.mv.db /root

CMD echo "spring.datasource.url=$SPRING_DATASOURCE_URL" > /vreportal/src/main/resources/application.properties && \
    echo "H2Manager.datasource.url=$H2MANAGER_DATASOURCE_URL" >> /vreportal/src/main/resources/application.properties && \
    echo "spring.datasource.username=$SPRING_DATASOURCE_USERNAME" >> /vreportal/src/main/resources/application.properties && \
    echo "spring.datasource.password=$SPRING_DATASOURCE_PASSWORD" >> /vreportal/src/main/resources/application.properties && \
    echo "spring.http.multipart.max-file-size=$SPRING_HTTP_MULTIPART_MAX_FILE_SIZE" >> /vreportal/src/main/resources/application.properties && \
    echo "spring.http.multipart.max-request-size=$SPRING_HTTP_MULTIPART_MAX_REQUEST_SIZE" >> /vreportal/src/main/resources/application.properties && \
    echo "spring.datasource.driver-class-name=$SPRING_DATASOURCE_DRIVER_CLASS_NAME" >> /vreportal/src/main/resources/application.properties && \    
    cat /vreportal/src/main/resources/application.properties && \
    echo "triplestore.namespace=$TRIPLESORE_NAMESPACE" > /vreportal/src/main/resources/config.properties && \    
    echo "service.url=$SERVICE_URL" >> /vreportal/src/main/resources/config.properties && \    
    echo "service.timeout=$SERVICE_TIMEOUT" >> /vreportal/src/main/resources/config.properties && \    
    echo "service.max.result.count=$SERVICE_MAX_RESULT_COUNT" >> /vreportal/src/main/resources/config.properties && \ 
    echo "uri.prefix=$URI_PREFIX" >> /vreportal/src/main/resources/config.properties && \ 
    echo "portal.state=$PORTAL_STATE" >> /vreportal/src/main/resources/config.properties && \
    cat /vreportal/src/main/resources/config.properties && \
    JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn "-Dexec.args=-classpath %classpath forth.ics.isl.runnable.H2Manager" -Dexec.executable=/usr/lib/jvm/java-8-openjdk-amd64/bin/java -Dexec.classpathScope=runtime org.codehaus.mojo:exec-maven-plugin:1.2.1:exec && \
    JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 mvn "-Dexec.args=-classpath %classpath forth.ics.isl.Application --server.port=8080" -Dexec.executable=/usr/lib/jvm/java-8-openjdk-amd64/bin/java org.codehaus.mojo:exec-maven-plugin:1.2.1:exec
    

# sudo docker build -t vre_portal . 
