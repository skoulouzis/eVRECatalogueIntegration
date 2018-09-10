FROM openjdk:8

ADD cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar /
# RUN echo "log4j.logger.org.apache.zookeeper=ERROR" > log4j.properties
# RUN echo "log4j.logger.nl.uva.sne.vre4eic=ALL" >> log4j.properties
# RUN echo "java.util.logging.ConsoleHandler.level = INFO" >> log4j.properties

CMD java -Dlog4j.configurationFile=log4j.properties -jar /cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar -r ${RABBIT_HOST} -w ${WEBDAV_HOST} -p ${WEBDAV_PASSWORD} -u ${WEBDAV_USERNAME}