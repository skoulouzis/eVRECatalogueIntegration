FROM tomcat:8

ADD catalogue_mapper-1.0-SNAPSHOT.zip /usr/local/tomcat/webapps/
WORKDIR /usr/local/tomcat/webapps 
RUN unzip catalogue_mapper-1.0-SNAPSHOT
RUN mv catalogue_mapper-1.0-SNAPSHOT catalogue_mapper


CMD echo "spring.rabbitmq.host=$RABBIT_HOST" > catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "spring.rabbitmq.port=$RABBIT_PORT" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "spring.rabbitmq.username=$RABBIT_USERNAME" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "spring.rabbitmq.password=$RABBIT_PASSWORD" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.metrics.export.influx.uri=$INFLUX_URI" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.metrics.web.server.auto-time-requests=true" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.endpoint.metrics.enabled=true" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.endpoints.web.expose=*" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.metrics.enable.jvm=true" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.metrics.enable.process=false" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.metrics.enable.system=false" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.metrics.export.influx.enabled=true" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.metrics.binders.integration.enabled=true" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    echo "management.metrics.binders.uptime.enabled=true" >> catalogue_mapper/WEB-INF/classes/application.properties && \
    cat catalogue_mapper/WEB-INF/classes/application.properties && \
    catalina.sh run
    
    
# docker run -i -t -P -e RABBIT_HOST=172.17.0.5 -e RABBIT_PORT=15672 -e RABBIT_USERNAME=guest -e RABBIT_PASSWORD=guest -e INFLUX_URI=http://172.17.0.3:8086 -e ZOOKEEPER_CONNECT_STRING=172.17.0.2:2181 rest-cat
