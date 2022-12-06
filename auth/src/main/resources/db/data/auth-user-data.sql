insert into users (username, password, enabled)
values('manager', '$2a$12$4VQ8t0ISvHsj3tDClxaWw.X77PdQKHQ8j0d5VB5pGYSzYXhtgqUdG', true);

insert into authorities (username, authority)
values('manager', 'ROLE_MANAGER');

insert into authorities (username, authority)
values('manager', 'ROLE_USER');

insert into profiles (username)
values('manager')