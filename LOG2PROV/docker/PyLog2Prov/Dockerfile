FROM python:3

RUN apt-get update -y && apt-get install -y python3-pip

ADD PyLog2Prov /

RUN pip install apache-log-parser requests six provpy prov pika
# RUN pip3 install requests six apache-log-parse provpy prov
# RUN python3 setup.py install

CMD python3 log2prov.py -r $RABBIT_HOST -p $RABBIT_PORT -u $RABBIT_USERNAME -a $RABBIT_PASSWORD

