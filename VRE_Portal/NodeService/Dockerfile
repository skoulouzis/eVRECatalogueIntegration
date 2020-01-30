FROM tomcat:8


ARG NODE_SERVICE_VERSION=0.0.3-SNAPSHOT

RUN apt-get -y update && apt-get -y upgrade && apt-get install -y git maven openjdk-8-jdk
WORKDIR /
RUN git clone -b canonicalprototype https://github.com/vre4eic/NodeService.git
WORKDIR NodeService
RUN mkdir -p /NodeService/doc

ADD pom.xml .

RUN mvn clean install -Dmaven.test.skip=true
RUN cp -r target/NodeService-$NODE_SERVICE_VERSION /usr/local/tomcat/webapps/NodeService

CMD echo "/evre/settings/version=$EVRE_SETTINGS_VERSION" > /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "/evre/settings/authentication/token_timeout=$EVRE_SETTINGS_AUTHENTICATION_TOKEN_TIMEOUT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "/evre/settings/authentication/code_timeout=$EVRE_SETTINGS_AUTHENTICATION_CODE_TIMEOUT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "/evre/services/MessageBroker/URL=$EVRE_SERVICES_MESSAGE_BROKER" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "/evre/services/ProfileStorage/URL=$EVRE_SERVICES_PROFILE_STORAGE_URL" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "/evre/services/ProfileStorage/Port=$EVRE_SERVICES_PROFILE_STORAGE_PORT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "NOTIFICATION_API_KEY=$NOTIFICATION_API_KEY" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "ADMIN_USER=$ADMIN_USER" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "ADMIN_PASSWORD=$ADMIN_PASSWORD" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "VERSION=$VERSION" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "LICENSE_TYPE=$LICENSE_TYPE" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "LICENSE_URL=$LICENSE_URL" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    echo "NOTIFICATION_API_KEY=$NOTIFICATION_API_KEY" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
     
    
    cat /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Nodeservice.properties && \
    
    echo "ZooKeeper_default=$ZOOKEEPER_DEFAULT" > /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "Version_path=/evre/settings/version" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \ 
    echo "Version_default=$EVRE_SETTINGS_VERSION" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "TokenTimeout_path=/evre/settings/authentication/token_timeout" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "TokenTimeout_default=$EVRE_SETTINGS_AUTHENTICATION_TOKEN_TIMEOUT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "TokenSecret_path=/evre/services/authentication/token_secret" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "TokenSecret_default=$TOKEN_SECRET_DEFAULT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "CodeTimeout_path=/evre/settings/authentication/code_timeout" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "CodeTimeout_default=$EVRE_SETTINGS_AUTHENTICATION_CODE_TIMEOUT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "MessageBroker_path=/evre/services/MessageBroker/URL" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "MessageBroker_default=$EVRE_SERVICES_MESSAGE_BROKER" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "ProfileStorage_path=/evre/services/ProfileStorage/URL" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "ProfileStorage_default=$EVRE_SERVICES_PROFILE_STORAGE_URL" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "ProfileStoragePort_path=/evre/services/ProfileStorage/Port" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "ProfileStoragePort_default=$EVRE_SERVICES_PROFILE_STORAGE_PORT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "AAAI_pwd=/evre/services/ProfileStorage/pwd" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "AAAI_pwd_default=$AAAI_PWD_DEFAULT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "AAAI_login=/evre/services/ProfileStorage/login" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    echo "AAAI_login_default=$AAAI_LOGIN_DEFAULT" >> /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    
    
    cat /usr/local/tomcat/webapps/NodeService/WEB-INF/classes/Settings.properties && \
    catalina.sh run  
    
    
    
# docker build -t node_service .    
# docker run -e EVRE_SETTINGS_VERSION="0.3" -e EVRE_SETTINGS_AUTHENTICATION_TOKEN_TIMEOUT="60" -e EVRE_SETTINGS_AUTHENTICATION_CODE_TIMEOUT="60" -e EVRE_SERVICES_MESSAGE_BROKER="tcp://172.17.0.2:61616" -e EVRE_SERVICES_PROFILE_STORAGE_URL="172.17.0.3" -e EVRE_SERVICES_PROFILE_STORAGE_PORT="27017" -e ADMIN_USER="administrator@fakeemail.com" -e ADMIN_PASSWORD="johnjohn78" -e VERSION="0.0.3" -e LICENSE_TYPE="TBD" -e LICENSE_URL="licensedoc" -e NOTIFICATION_API_KEY="aaaaaaaaaaaaa" -e ZOOKEEPER_DEFAULT="localhost:2181" -e TOKEN_SECRET_DEFAULT="fvsecret" -e AAAI_LOGIN_DEFAULT="admin" -e AAAI_PWD_DEFAULT="admin" -e AAAI_LOGIN="AAAI_LOGIN" -p 8081:8080 -it node_service     
