
docker network create gemfire-cache --driver bridge

# Run Locator
#docker run -d -e 'ACCEPT_TERMS=y' --rm --name gf-locator --network=gemfire-cache -p 10334:10334 -p 7070:7070 gemfire/gemfire:10.1.2-jdk17 gfsh start locator --name=locator1
docker run  -d -e 'ACCEPT_TERMS=y' --rm --name gf-locator --network=gemfire-cache -p 10334:10334  -p 1099:1099 -p 7070:7070  gemfire/gemfire:10.1.2-jdk17 gfsh start locator --name=locator1 --jmx-manager-hostname-for-clients=127.0.0.1 --hostname-for-clients=127.0.0.1    --J=-Dgemfire.distributed-system-id=1

sleep 20

# Configure PDX
docker exec -it gf-locator gfsh -e "connect --jmx-manager=gf-locator[1099]" -e "configure pdx --read-serialized=true --disk-store"

# Run Cache Server
docker run -d -it -e 'ACCEPT_TERMS=y' --rm --name gf-server1 --network=gemfire-cache -p 40404:40404 -p 50510:50510 -p 50511:50511 gemfire/gemfire:10.1.2-jdk17 gfsh start server --name=server1 --locators=gf-locator\[10334\] --hostname-for-clients=127.0.0.1 --J=-Dgemfire.distributed-system-id=1

sleep 20
# Setup GemFire Vehicle Region
#docker run -it -e 'ACCEPT_TERMS=y' --network=gemfire-cache gemfire/gemfire:10.1.2-jdk17 gfsh -e "connect --jmx-manager=gf-locator[1099]" -e "create region --name=Account --type=PARTITION  --enable-statistics=true"
docker exec -it gf-locator gfsh -e "connect --jmx-manager=gf-locator[1099]" -e "create region --name=Vehicle --type=PARTITION  --enable-statistics=true"


docker exec -it gf-locator gfsh -e "connect --jmx-manager=gf-locator[1099]" -e "create gateway-receiver --hostname-for-senders=127.0.0.1 --start-port=50510 --end-port=50511"

