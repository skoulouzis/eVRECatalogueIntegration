#!/bin/bash

RABBIT_HOST=localhost
RABBIT_PORT=15672
RABBIT_USERNAME=guest
RABBIT_PASSWORD=guest
RABBIT_QNAME=ckan_task_queue
RABBIT_VHOST="%2F"

INFLUX_HOST=localhost
INFLUX_PORT=8086
INFLUX_DB=mydb



# curl -i -XPOST http://$INFLUX_HOST:$INFLUX_PORT/query --data-urlencode "q=CREATE DATABASE $INFLUX_DB"



while true; do
    curl -u guest:guest http://$RABBIT_HOST:$RABBIT_PORT/api/queues/$RABBIT_VHOST/$RABBIT_QNAME > q_metrics.json 
    
    avg_ack_egress_rate=`cat q_metrics.json | jq .backing_queue_status.avg_ack_egress_rate`
    avg_ack_ingress_rate=`cat q_metrics.json | jq .backing_queue_status.avg_ack_ingress_rate`
    avg_egress_rate=`cat q_metrics.json | jq .backing_queue_status.avg_egress_rate`
    avg_ingress_rate=`cat q_metrics.json | jq .backing_queue_status.avg_ingress_rate`
    len=`cat q_metrics.json | jq .backing_queue_status.len`    
    
    consumer_utilisation=`cat q_metrics.json | jq .consumer_utilisation`
    consumers=`cat q_metrics.json | jq .consumers`
    
    minor_gcs=`cat q_metrics.json | jq .garbage_collection.minor_gcs`
    fullsweep_after=`cat q_metrics.json | jq .garbage_collection.fullsweep_after`
    min_heap_size=`cat q_metrics.json | jq .garbage_collection.min_heap_size`
    min_bin_vheap_size=`cat q_metrics.json | jq .garbage_collection.min_bin_vheap_size`
    max_heap_size=`cat q_metrics.json | jq .garbage_collection.max_heap_size`
    memory=`cat q_metrics.json | jq .memory`
    message_bytes=`cat q_metrics.json | jq .message_bytes`
    message_bytes_paged_out=`cat q_metrics.json | jq .message_bytes_paged_out`
    message_bytes_persistent=`cat q_metrics.json | jq .message_bytes_persistent`
    message_bytes_ram=`cat q_metrics.json | jq .message_bytes_ram`
    message_bytes_ready=`cat q_metrics.json | jq .message_bytes_ready`
    
    curl -XPOST http://$INFLUX_HOST:$INFLUX_PORT/write?db=$INFLUX_DB --data-binary "rabbit,queue=$RABBIT_QNAME,metric=avg_ack_egress_rate value=$avg_ack_egress_rate
    rabbit,queue=$RABBIT_QNAME,backing_queue_status=avg_ack_ingress_rate value=$avg_ack_ingress_rate
    rabbit,queue=$RABBIT_QNAME,backing_queue_status=avg_egress_rate value=$avg_egress_rate
    rabbit,queue=$RABBIT_QNAME,backing_queue_status=avg_ingress_rate value=$avg_ingress_rate
    rabbit,queue=$RABBIT_QNAME,backing_queue_status=len value=$len
    rabbit,queue=$RABBIT_QNAME,root=consumer_utilisation value=$consumer_utilisation
    rabbit,queue=$RABBIT_QNAME,root=consumers value=$consumer_utilisation
    rabbit,queue=$RABBIT_QNAME,garbage_collection=fullsweep_after value=$fullsweep_after
    rabbit,queue=$RABBIT_QNAME,garbage_collection=min_heap_size value=$min_heap_size
    rabbit,queue=$RABBIT_QNAME,garbage_collection=min_bin_vheap_size value=$min_bin_vheap_size
    rabbit,queue=$RABBIT_QNAME,root=memory value=$memory
    rabbit,queue=$RABBIT_QNAME,root=message_bytes value=$message_bytes
    rabbit,queue=$RABBIT_QNAME,root=message_bytes_persistent value=$message_bytes_persistent
    rabbit,queue=$RABBIT_QNAME,root=message_bytes_ram value=$message_bytes_ram
    rabbit,queue=$RABBIT_QNAME,root=message_bytes_ready value=$message_bytes_ready"
    

    
    
    sleep 30
done















# [
#   "",
#   "",
#   "",
# 
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "",
#   "message_bytes_unacknowledged",
#   "message_stats",
#   "messages",
#   "messages_details",
#   "messages_paged_out",
#   "messages_persistent",
#   "messages_ram",
#   "messages_ready",
#   "messages_ready_details",
#   "messages_ready_ram",
#   "messages_unacknowledged",
#   "messages_unacknowledged_details",
#   "messages_unacknowledged_ram",
#   "name",
#   "node",
#   "operator_policy",
#   "policy",
#   "recoverable_slaves",
#   "reductions",
#   "reductions_details",
#   "state",
#   "vhost"
# ]
