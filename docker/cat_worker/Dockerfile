FROM java:8

ADD cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar /


CMD java -jar /cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar -r $RABBIT_HOST -z $ZOOKEEPER_HOST
    
    
# docker run -i -t -P -e RABBIT_HOST=172.17.0.3 -e ZOOKEEPER_HOST=172.17.0.2 cat-worker