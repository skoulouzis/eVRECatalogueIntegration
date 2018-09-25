#!/bin/bash

mvn install 

cd cat_worker/

mvn clean compile assembly:single
cp target/cat_worker-1.0-SNAPSHOT-jar-with-dependencies.jar ../docker/cat_worker/


cd ../rest_cat/target/
zip -r catalogue_mapper-1.0-SNAPSHOT.zip catalogue_mapper-1.0-SNAPSHOT
cp catalogue_mapper-1.0-SNAPSHOT.zip ../../docker/rest_cat/


cd ../../docker/cat_worker/
sudo docker build -t cat-worker .

sudo docker build -t rest-cat .
