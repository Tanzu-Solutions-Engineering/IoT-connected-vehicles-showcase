# IoT-data-showCase


**Use cases**

- Hundreds of thousands of vehicles driving across the globe, 24 hours a day, 7 days a week.
- Need to access aggregated real-time location, driving, behavior data, temperature sensors and more.

**Solution**

- Scalable, resiliency Kubernetes platform based on Tanzu
- Cloud Native Microservices based on Spring
- Reliable messaging deliverable with RabbitMQ
- Low latency data read/write operations with GemFire


![img.png](docs/images/overview.png)

# Project Modules


Applications                                                                        |    Notes
-------------------------------------------------------------------------           |    ----------------------
[geotabgroup-tracking-source](applications/geotabgroup-tracking-source)             |    Any car positioning based Geotab's fleet management streaming source microservice for vehicle details in JSON format
[IoT-connected-vehicle-dashboard](applications/IoT-connected-vehicle-dashboard)     |    GUI interface to views vehicle information
[IoT-connected-vehicles-sink](applications/IoT-connected-vehicles-sink)             |    Microservice streaming sink for storing Vehicle data in GemFire
[randmcnally-triplanning-source](applications/randmcnally-triplanning-source)       |    Trip planninhg details based on Rand McNally fleet management streaming source for vehicle data in XML format
[volvo-safecar-source](applications/volvo-safecar-source)                           |    Safe car Volvo Fleet Management management streaming source for vehicle data in XML format


# Docker

Use the following command to build docker images

```shell script
gradle :applications:vehicle-generator-app:bootBuildImage
gradle :applications:IoT-connected-vehicles-sink:bootBuildImage

```

TODO: What does the prod look like (source providers or vehicle)


Today 

- Demo
- Install k8 (apps, rabbitmq, gemfire)






