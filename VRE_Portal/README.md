# Prerequisites  
- Docker 
- Docker stack 

For debian systems run:
```
apt-get install docker-ce
```

# Build 
Simply run the ``build.sh`` script to build all the docker images 


# Run 
Deploy the stack using:
```
docker stack deploy -c docker-compose.yml portal
```
