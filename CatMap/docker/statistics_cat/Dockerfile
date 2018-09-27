FROM python:2.7.15-alpine3.7

ADD scripts/merge_rdf.py /

RUN pip install rdflib pathlib pika

RUN chmod +x /merge_rdf.py

CMD python /merge_rdf.py $RABBIT_HOST $QUEUE_NAME


# docker run -i -t -P -e RABBIT_HOST=172.17.0.5 -e RABBIT_PORT=15672 -e RABBIT_USERNAME=guest -e RABBIT_PASSWORD=guest -e RABBIT_QNAME=ckan_Mapping62.x3ml -e RABBIT_VHOST=%2F -e INFLUX_HOST=172.17.0.3 -e INFLUX_PORT=8086 -e INFLUX_DB=mydb rabbit-reporter 
