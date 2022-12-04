-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler version: 0.9.4
-- PostgreSQL version: 13.0
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
-- object: vending_system_auth | type: DATABASE --
-- DROP DATABASE IF EXISTS vending_system_auth;
-- CREATE DATABASE vending_system_auth;
-- ddl-end --


-- object: public.authorities_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.authorities_seq CASCADE;
CREATE SEQUENCE public.authorities_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.authorities_seq OWNER TO what;
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

-- object: public.profiles_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.profiles_seq CASCADE;
CREATE SEQUENCE public.profiles_seq
	INCREMENT BY 1
	MINVALUE 0
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;

-- ddl-end --
ALTER SEQUENCE public.profiles_seq OWNER TO what;
-- ddl-end --

-- object: public.users | type: TABLE --
-- DROP TABLE IF EXISTS public.users CASCADE;
CREATE TABLE public.users (
	user_id bigint NOT NULL DEFAULT nextval('public.users_seq'::regclass),
	username varchar(50) NOT NULL,
	password varchar(68) NOT NULL,
	enabled smallint NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (user_id),
	CONSTRAINT username_uq UNIQUE (username)
);
-- ddl-end --
ALTER TABLE public.users OWNER TO what;
-- ddl-end --

-- object: public.profiles | type: TABLE --
-- DROP TABLE IF EXISTS public.profiles CASCADE;
CREATE TABLE public.profiles (
	profile_id bigint NOT NULL DEFAULT nextval('public.profiles_seq'::regclass),
	username varchar(50) NOT NULL,
	lastname varchar(255),
	firstname varchar(255),
	middlename varchar(255),
	email varchar(255),
	phone varchar(20),
	CONSTRAINT profiles_pk PRIMARY KEY (profile_id),
	CONSTRAINT profilies_uq UNIQUE (username)
);
-- ddl-end --
ALTER TABLE public.profiles OWNER TO what;
-- ddl-end --

-- object: public.authorities | type: TABLE --
-- DROP TABLE IF EXISTS public.authorities CASCADE;
CREATE TABLE public.authorities (
	authority_id bigint NOT NULL DEFAULT nextval('public.authorities_seq'::regclass),
	username varchar(50) NOT NULL,
	authority varchar(68) NOT NULL,
	CONSTRAINT authorities_pk PRIMARY KEY (authority_id)
);
-- ddl-end --
ALTER TABLE public.authorities OWNER TO what;
-- ddl-end --

-- object: authorities_fk | type: CONSTRAINT --
-- ALTER TABLE public.authorities DROP CONSTRAINT IF EXISTS authorities_fk CASCADE;
ALTER TABLE public.authorities ADD CONSTRAINT authorities_fk FOREIGN KEY (username)
REFERENCES public.users (username) MATCH SIMPLE
ON DELETE CASCADE ON UPDATE NO ACTION;
-- ddl-end --

-- object: profilies_fk | type: CONSTRAINT --
-- ALTER TABLE public.profiles DROP CONSTRAINT IF EXISTS profilies_fk CASCADE;
ALTER TABLE public.profiles ADD CONSTRAINT profilies_fk FOREIGN KEY (username)
REFERENCES public.users (username) MATCH SIMPLE
ON DELETE NO ACTION ON UPDATE NO ACTION;
-- ddl-end --


