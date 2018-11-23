#!/bin/bash


influx_id=`sudo docker ps -aqf "name=influx"`
sudo docker exec  -ti $influx_id influxd backup -portable -database mydb /tmp/mydb
sudo docker cp $influx_id:/tmp/mydb /tmp/
sudo chown -R $USER /tmp/mydb
mv /tmp/mydb /tmp/mydb_$HOSTNAME
