CREATE SCHEMA IF NOT EXISTS vehicle_iot;

CREATE TABLE vehicle_iot.vehicle_telemetry (
    telemetry_id SERIAL,
	latitude float8 null,
	longitude float8 null ,
	capture_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
	odometer int8,
	speed int4 null,
	temperature int4 null,
	vin varchar(255) not NULL,
	CONSTRAINT vehicle_vpkey PRIMARY KEY (telemetry_id)
);