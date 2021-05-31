CREATE TABLE pivotalmarkets.vehicle (
	id text NOT NULL,
	latitude float8 null,
	longitude float8 null ,
	odometer int8,
	speed int4 null,
	temperature int4 null,
	vin varchar(255) NULL,
	CONSTRAINT vehicle_pkey PRIMARY KEY (id)
);