

Note that MQTT uses slashes ("/") for topic segment separators
and AMQP 0-9-1 uses dots. This plugin translates patterns under 
the hood to bridge the two, for example, cities/london becomes 
cities.london and vice versa. 

This has one important limitation: MQTT topics that have dots in 
them won't work as expected and are to be avoided, the same goes for 
AMQP 0-9-1 routing keys that contains slashes.
