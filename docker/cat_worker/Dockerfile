# FROM java:8
FROM airdock/oracle-jdk

ADD cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar /

RUN echo "log4j.logger.org.apache.zookeeper=ERROR" > log4j.properties
RUN echo "log4j.logger.nl.uva.sne.vre4eic=ALL" >> log4j.properties

CMD java -jar -Dlog4j.configuration=log4j.properties /cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar -r ${RABBIT_HOST} -z ${ZOOKEEPER_HOST} -f ${FTP_HOST} -p ${FTP_USER_PASS} -u ${FTP_USER_NAME}
    
# docker run -i -t -P -e RABBIT_HOST=172.17.0.3 -e ZOOKEEPER_HOST=172.17.0.2 cat-worker