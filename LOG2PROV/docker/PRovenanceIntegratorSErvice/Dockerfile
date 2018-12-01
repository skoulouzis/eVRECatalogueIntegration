FROM openjdk:8

ADD ProvenanceIntegrationServcie-0.0.1-SNAPSHOT.jar /



CMD echo "spring.rabbitmq.host=$RABBIT_HOST" > application.properties && \ 
    echo "spring.rabbitmq.port=$RABBIT_PORT" >> application.properties && \ 
    echo "spring.rabbitmq.username=$RABBIT_USERNAME" >> application.properties && \ 
    echo "spring.rabbitmq.password=$RABBIT_PASSWORD" >> application.properties && \ 
    echo "management.metrics.export.influx.uri=$INFLUX_URI" >> application.properties && \ 
    echo "management.metrics.web.server.auto-time-requests=true" >> application.properties && \ 
    echo "management.endpoint.metrics.enabled=true" >> application.properties && \ 
    echo "management.endpoints.web.expose=*" >> application.properties && \ 
    echo "management.metrics.enable.jvm=true" >> application.properties && \ 
    echo "management.metrics.enable.process=false" >> application.properties && \ 
    echo "management.metrics.enable.system=false" >> application.properties && \ 
    echo "management.metrics.export.influx.enabled=true" >> application.properties && \ 
    echo "management.metrics.binders.integration.enabled=true" >> application.properties && \ 
    echo "management.metrics.binders.uptime.enabled=true" >> application.properties && \ 
    echo "log.reposetory.uri=$LOG_REPO_URL" >> application.properties && \ 
    echo "prov.reposetory.uri=$PROV_REPO_URL" >> application.properties && \ 
    echo "wf.reposetory.uri=$WF_REPO_URL" >> application.properties && \ 
    java -jar /ProvenanceIntegrationServcie-0.0.1-SNAPSHOT.jar --spring.config.location=file:./application.properties 
