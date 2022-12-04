insert into users (username, password, enabled)
values('manager', '$2a$12$4VQ8t0ISvHsj3tDClxaWw.X77PdQKHQ8j0d5VB5pGYSzYXhtgqUdG', 1);

insert into authorities (username, authority)
values('manager', 'role_manager');
-- insert into authorities values('manager', 'role_user');

insert into profiles (username)
values('manager')