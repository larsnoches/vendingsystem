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
-- object: vending_system_auth | type: DATABASE --
-- DROP DATABASE IF EXISTS vending_system_auth;
-- CREATE DATABASE vending_system_auth;
-- ddl-end --


-- object: public.authorities | type: TABLE --
-- DROP TABLE IF EXISTS public.authorities CASCADE;
CREATE TABLE public.authorities (
	username varchar(50) NOT NULL,
	authority varchar(68) NOT NULL,
	username_users varchar(50),
	CONSTRAINT authorities_pk PRIMARY KEY (username)
);
-- ddl-end --
ALTER TABLE public.authorities OWNER TO what;
-- ddl-end --

-- object: public.users | type: TABLE --
-- DROP TABLE IF EXISTS public.users CASCADE;
CREATE TABLE public.users (
	username varchar(50) NOT NULL,
	password varchar(68) NOT NULL,
	enabled smallint NOT NULL,
	CONSTRAINT users_pk PRIMARY KEY (username)
);
-- ddl-end --
ALTER TABLE public.users OWNER TO what;
-- ddl-end --

-- object: public.profiles | type: TABLE --
-- DROP TABLE IF EXISTS public.profiles CASCADE;
CREATE TABLE public.profiles (
	username varchar(50) NOT NULL,
	lastname varchar(255) NOT NULL,
	firstname varchar(255) NOT NULL,
	middlename varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	phone varchar(20),
	username_users varchar(50),
	CONSTRAINT profiles_pk PRIMARY KEY (username)
);
-- ddl-end --
ALTER TABLE public.profiles OWNER TO what;
-- ddl-end --

-- object: users_fk | type: CONSTRAINT --
-- ALTER TABLE public.authorities DROP CONSTRAINT IF EXISTS users_fk CASCADE;
ALTER TABLE public.authorities ADD CONSTRAINT users_fk FOREIGN KEY (username_users)
REFERENCES public.users (username) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: users_fk | type: CONSTRAINT --
-- ALTER TABLE public.profiles DROP CONSTRAINT IF EXISTS users_fk CASCADE;
ALTER TABLE public.profiles ADD CONSTRAINT users_fk FOREIGN KEY (username_users)
REFERENCES public.users (username) MATCH FULL
ON DELETE SET NULL ON UPDATE CASCADE;
-- ddl-end --

-- object: profiles_uq | type: CONSTRAINT --
-- ALTER TABLE public.profiles DROP CONSTRAINT IF EXISTS profiles_uq CASCADE;
ALTER TABLE public.profiles ADD CONSTRAINT profiles_uq UNIQUE (username_users);
-- ddl-end --


