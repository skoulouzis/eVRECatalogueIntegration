#!/bin/bash

<<<<<<< HEAD:CatMap/build.sh
<<<<<<< HEAD:build.sh
mvn install  &&  cd cat_worker/ && mvn compile assembly:single
=======
=======
>>>>>>> 87e9c74d2a56e5afdf6352d9db2214e8871d9372:build.sh
# mvn install 
JAVA_HOME=/usr/lib/jvm/java-8-oracle mvn install

cd cat_worker/

# mvn clean compile assembly:single
<<<<<<< HEAD:CatMap/build.sh
mvn compile assembly:single
>>>>>>> 9c5fd40faeb33756f972d41f5513ae5410286d8d:CatMap/build.sh
cp target/cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar ../docker/cat_worker/
=======
mvn compile assembly:single && cp target/cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar ../docker/cat_worker/
>>>>>>> 87e9c74d2a56e5afdf6352d9db2214e8871d9372:build.sh


cd ../rest_cat/target/
zip -r catalogue_mapper-1.0-SNAPSHOT.zip catalogue_mapper-1.0-SNAPSHOT
cp catalogue_mapper-1.0-SNAPSHOT.zip ../../docker/rest_cat/


cd ../../docker/cat_worker/
sudo docker build -t cat-worker .

cd ../rest_cat/
sudo docker build -t rest-cat .
