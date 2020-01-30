#!/bin/bash

sudo docker tag cat-worker alogo53/cat-worker
sudo docker push alogo53/cat-worker

sudo docker tag rest-cat alogo53/rest-cat
sudo docker push alogo53/rest-cat
