-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler version: 0.9.4
-- PostgreSQL version: 12.0
-- Project Site: pgmodeler.io
-- Model Author: ---
-- object: what | type: ROLE --
-- DROP ROLE IF EXISTS what;
-- CREATE ROLE what WITH
-- 	SUPERUSER
-- 	CREATEDB
-- 	LOGIN
-- 	REPLICATION
-- 	BYPASSRLS
-- 	ENCRYPTED PASSWORD 'masterkey';
-- ddl-end --


-- Database creation must be performed outside a multi lined SQL file. 
-- These commands were put in this file only as a convenience.
-- 
-- object: bus_system | type: DATABASE --
-- DROP DATABASE IF EXISTS bus_system;
-- CREATE DATABASE bus_system;
-- ddl-end --


-- object: public.buspoints_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.buspoints_seq CASCADE;
CREATE SEQUENCE public.buspoints_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.buspoints_seq OWNER TO what;
-- ddl-end --

-- object: public.seats_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.seats_seq CASCADE;
CREATE SEQUENCE public.seats_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.seats_seq OWNER TO what;
-- ddl-end --

-- object: public.buses_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.buses_seq CASCADE;
CREATE SEQUENCE public.buses_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.buses_seq OWNER TO what;
-- ddl-end --

-- object: public.bustrips_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.bustrips_seq CASCADE;
CREATE SEQUENCE public.bustrips_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.bustrips_seq OWNER TO what;
-- ddl-end --

-- object: public.fares_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.fares_seq CASCADE;
CREATE SEQUENCE public.fares_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.fares_seq OWNER TO what;
-- ddl-end --

-- object: public.seats | type: TABLE --
-- DROP TABLE IF EXISTS public.seats CASCADE;
CREATE TABLE public.seats (
	seat_id bigint NOT NULL DEFAULT nextval('public.seats_seq'::regclass),
	seat_name varchar(10) NOT NULL,
	seat_is_occupied boolean NOT NULL,
	bustrip_id bigint,
	CONSTRAINT seats_pk PRIMARY KEY (seat_id)
);
-- ddl-end --
ALTER TABLE public.seats OWNER TO what;
-- ddl-end --

-- object: public.carriers_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.carriers_seq CASCADE;
CREATE SEQUENCE public.carriers_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.carriers_seq OWNER TO what;
-- ddl-end --

-- object: public.tickets_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.tickets_seq CASCADE;
CREATE SEQUENCE public.tickets_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 9223372036854775807
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.tickets_seq OWNER TO what;
-- ddl-end --

-- object: public.buses | type: TABLE --
-- DROP TABLE IF EXISTS public.buses CASCADE;
CREATE TABLE public.buses (
	bus_id bigint NOT NULL DEFAULT nextval('public.buses_seq'::regclass),
	bus_make_model varchar(255) NOT NULL,
	manufacturer_country varchar(255) NOT NULL,
	year_of_manufacture integer NOT NULL,
	seat_count integer NOT NULL,
	bus_reg_number varchar(50) NOT NULL,
	carrier_id bigint,
	CONSTRAINT buses_pk PRIMARY KEY (bus_id)
);
-- ddl-end --
ALTER TABLE public.buses OWNER TO what;
-- ddl-end --

-- object: public.buspoints | type: TABLE --
-- DROP TABLE IF EXISTS public.buspoints CASCADE;
CREATE TABLE public.buspoints (
	buspoint_id bigint NOT NULL DEFAULT nextval('public.buspoints_seq'::regclass),
	buspoint_name varchar(255) NOT NULL,
	buspoint_address varchar(255) NOT NULL,
	buspoint_type varchar(255) NOT NULL,
	CONSTRAINT buspoints_pk PRIMARY KEY (buspoint_id)
);
-- ddl-end --
ALTER TABLE public.buspoints OWNER TO what;
-- ddl-end --

-- object: public.bustrips | type: TABLE --
-- DROP TABLE IF EXISTS public.bustrips CASCADE;
CREATE TABLE public.bustrips (
	bustrip_id bigint NOT NULL DEFAULT nextval('public.bustrips_seq'::regclass),
	departure_buspoint_id bigint,
	arrival_buspoint_id bigint,
	distance numeric NOT NULL,
	bus_route_number varchar(15) NOT NULL,
	departure_datetime timestamptz NOT NULL,
	arrival_datetime timestamptz NOT NULL,
	bus_id bigint,
	fare_id bigint,
	CONSTRAINT bustrips_pk PRIMARY KEY (bustrip_id)
);
-- ddl-end --
ALTER TABLE public.bustrips OWNER TO what;
-- ddl-end --

-- object: bustrips_fk | type: CONSTRAINT --
-- ALTER TABLE public.seats DROP CONSTRAINT IF EXISTS bustrips_fk CASCADE;
ALTER TABLE public.seats ADD CONSTRAINT bustrips_fk FOREIGN KEY (bustrip_id)
REFERENCES public.bustrips (bustrip_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public.carriers | type: TABLE --
-- DROP TABLE IF EXISTS public.carriers CASCADE;
CREATE TABLE public.carriers (
	carrier_id bigint NOT NULL DEFAULT nextval('public.carriers_seq'::regclass),
	carrier_name varchar(255) NOT NULL,
	carrier_inn varchar(24) NOT NULL,
	carrier_address varchar(255) NOT NULL,
	CONSTRAINT carriers_pk PRIMARY KEY (carrier_id)
);
-- ddl-end --
ALTER TABLE public.carriers OWNER TO what;
-- ddl-end --

-- object: carriers_fk | type: CONSTRAINT --
-- ALTER TABLE public.buses DROP CONSTRAINT IF EXISTS carriers_fk CASCADE;
ALTER TABLE public.buses ADD CONSTRAINT carriers_fk FOREIGN KEY (carrier_id)
REFERENCES public.carriers (carrier_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public.fares | type: TABLE --
-- DROP TABLE IF EXISTS public.fares CASCADE;
CREATE TABLE public.fares (
	fare_id bigint NOT NULL DEFAULT nextval('public.fares_seq'::regclass),
	fare_name varchar(255) NOT NULL,
	price numeric NOT NULL,
	carrier_id bigint,
	CONSTRAINT fares_pk PRIMARY KEY (fare_id)
);
-- ddl-end --
ALTER TABLE public.fares OWNER TO what;
-- ddl-end --

-- object: buses_fk | type: CONSTRAINT --
-- ALTER TABLE public.bustrips DROP CONSTRAINT IF EXISTS buses_fk CASCADE;
ALTER TABLE public.bustrips ADD CONSTRAINT buses_fk FOREIGN KEY (bus_id)
REFERENCES public.buses (bus_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: carriers_fk | type: CONSTRAINT --
-- ALTER TABLE public.fares DROP CONSTRAINT IF EXISTS carriers_fk CASCADE;
ALTER TABLE public.fares ADD CONSTRAINT carriers_fk FOREIGN KEY (carrier_id)
REFERENCES public.carriers (carrier_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public.tickets | type: TABLE --
-- DROP TABLE IF EXISTS public.tickets CASCADE;
CREATE TABLE public.tickets (
	ticket_id bigint NOT NULL DEFAULT nextval('public.tickets_seq'::regclass),
	issue_datetime timestamptz NOT NULL,
	passenger_lastname varchar(255),
	passenger_firstname varchar(255),
	passenger_middlename varchar(255),
	qr_code varchar(255) NOT NULL,
	seat_name varchar(10) NOT NULL,
	ticket_price numeric NOT NULL,
	ticket_status varchar(255) NOT NULL,
	email varchar(255),
	bustrip_id bigint,
	CONSTRAINT tickets_pk PRIMARY KEY (ticket_id)
);
-- ddl-end --
ALTER TABLE public.tickets OWNER TO what;
-- ddl-end --

-- object: bustrips_fk | type: CONSTRAINT --
-- ALTER TABLE public.tickets DROP CONSTRAINT IF EXISTS bustrips_fk CASCADE;
ALTER TABLE public.tickets ADD CONSTRAINT bustrips_fk FOREIGN KEY (bustrip_id)
REFERENCES public.bustrips (bustrip_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: fares_fk | type: CONSTRAINT --
-- ALTER TABLE public.bustrips DROP CONSTRAINT IF EXISTS fares_fk CASCADE;
ALTER TABLE public.bustrips ADD CONSTRAINT fares_fk FOREIGN KEY (fare_id)
REFERENCES public.fares (fare_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: public.users_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.users_seq CASCADE;
CREATE SEQUENCE public.users_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.users_seq OWNER TO what;
-- ddl-end --

-- object: public.users | type: TABLE --
-- DROP TABLE IF EXISTS public.users CASCADE;
CREATE TABLE public.users (
	user_id bigint NOT NULL DEFAULT nextval('public.users_seq'::regclass),
	email varchar(255) NOT NULL,
	password varchar(68) NOT NULL,
	lastname varchar(255),
	firstname varchar(255),
	middlename varchar(255),
	enabled boolean,
	user_role varchar(255) NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_id),
	CONSTRAINT email_uq UNIQUE (email)
);
-- ddl-end --
COMMENT ON TABLE public.users IS E'Пользователи';
-- ddl-end --
ALTER TABLE public.users OWNER TO what;
-- ddl-end --

-- object: departure_buspoints_fk | type: CONSTRAINT --
-- ALTER TABLE public.bustrips DROP CONSTRAINT IF EXISTS departure_buspoints_fk CASCADE;
ALTER TABLE public.bustrips ADD CONSTRAINT departure_buspoints_fk FOREIGN KEY (departure_buspoint_id)
REFERENCES public.buspoints (buspoint_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: arrival_buspoints_fk | type: CONSTRAINT --
-- ALTER TABLE public.bustrips DROP CONSTRAINT IF EXISTS arrival_buspoints_fk CASCADE;
ALTER TABLE public.bustrips ADD CONSTRAINT arrival_buspoints_fk FOREIGN KEY (arrival_buspoint_id)
REFERENCES public.buspoints (buspoint_id) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --


