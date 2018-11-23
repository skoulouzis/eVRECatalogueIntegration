#!/bin/bash


influx_id=`sudo docker ps -aqf "name=influx"`
sudo docker exec  -ti $influx_id influxd backup -portable -database mydb /tmp/mydb
sudo docker cp $influx_id:/tmp/mydb /tmp/
sudo chown -R $USER /tmp/mydb
rm -r /tmp/mydb_$HOSTNAME
mv /tmp/mydb /tmp/mydb_$HOSTNAME
cp -r /tmp/mydb_$HOSTNAME /media/$USER/surfDrive
