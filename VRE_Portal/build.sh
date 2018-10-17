#!/bin/bash

cd EVREMetadataServices
docker build -t evre_metadata_service .

cd ../NodeService
docker build -t node_service .

cd ../vreportal
docker build -t vre_portal . 

cd ../

