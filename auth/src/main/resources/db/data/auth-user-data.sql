-- create table users
-- (
--     username varchar(50) not null,
--     password varchar(68) not null,
--     enabled smallint(1) not null,
--     primary key(username)
-- );
--
-- insert into users (username, password, enabled) values('employee','$2a$10$crqfrdolnvfw6saju0eneoe0vc29aiyxwfsesy2fz2axy3mnh8zga',1);
insert into users (username, password, enabled)
values('manager', '$2a$10$crqfrdolnvfw6saju0eneoe0vc29aiyxwfsesy2fz2axy3mnh8zga', 1);
--
-- create table authorities
-- (
--     username varchar(50) not null,
--     authority varchar(68) not null,
--     foreign key (username) references users(username)
-- );
--
-- insert into authorities values('employee','role_employee');
-- insert into authorities values('employee','role_user');
insert into authorities values('manager', 'role_manager');
-- insert into authorities values('manager', 'role_user');