-- DROP SCHEMA IF EXISTS auth CASCADE;
CREATE SCHEMA auth;
-- ddl-end --
ALTER SCHEMA auth OWNER TO what;
-- ddl-end --
-- COMMENT ON SCHEMA auth IS E'Система аутентификации и авторизации пользователей автоматизированной системы продажи билетов.';
-- ddl-end --

SET search_path TO pg_catalog,public,auth;