delete from password_reset_tokens;
delete from exercises;
delete from exercise_types;
delete from workouts;
delete from users;
alter sequence global_seq restart with 1000;

insert into users (name, email, password, registered, email_verification_token, enabled, is_non_locked, role)
values ('user1', 'user1@test.ru', '123456', '2020-11-29 19:00:00', null, true, true, 'ROLE_USER'),
       ('user2', 'user2@test.ru', '123456', '2020-11-29 19:00:00', '123AAA123', false, true, 'ROLE_USER'),
       ('user3', 'user3@test.ru', '123456', '2020-11-29 19:00:00', null, true, false, 'ROLE_USER'),
       ('admin', 'admin@test.ru', '$2y$10$mM4j46rI5jEwIa4FjZZUdOCD1VgO0.KovMo4ZDhcS/Tvg8v4aV4TO', '2020-11-29 19:00:00', null, true, true, 'ROLE_ADMIN');

insert into exercise_types (name, measure, user_id)
values ('user1 exercise type 1', 'TIMES', 1000),
       ('user1 exercise type 2', 'SECONDS', 1000),
       ('user1 exercise type 3', 'KILOMETERS', 1000),
       ('admin exercise type 1', 'TIMES', 1003),
       ('admin exercise type 2', 'TIMES', 1003);

insert into workouts (date_time, user_id)
values ('2020-11-28 13:00:00', 1000),
       ('2020-11-28 18:00:00', 1000),
       ('2020-11-29 11:00:00', 1000),
       ('2020-11-27 11:00:00', 1003),
       ('2020-11-28 11:00:00', 1003);

insert into exercises (quantity, exercise_type_id, workout_id, user_id)
values (90, 1004, 1009, 1000),
       (30, 1005, 1009, 1000),
       (5, 1006, 1009, 1000),
       (80, 1004, 1010, 1000),
       (80, 1004, 1011, 1000),
       (10, 1006, 1011, 1000),
       (50, 1007, 1012, 1003),
       (60, 1008, 1012, 1003),
       (50, 1007, 1013, 1003),
       (50, 1008, 1013, 1003);