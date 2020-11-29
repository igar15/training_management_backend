drop table if exists exercises;
drop table if exists exercise_types;
drop table if exists workouts;
drop table if exists users;
drop sequence if exists global_seq;

create sequence global_seq start with 1000;

create table users (
    id bigint primary key default nextval('global_seq'),
    name varchar not null,
    email varchar not null,
    password varchar not null,
    registered timestamp default now() not null,
    email_verification_token varchar not null,
    email_verification_status boolean not null,
    is_non_locked boolean not null,
    role varchar not null
);
create unique index users_unique_email_idx on users (email);

create table exercise_types (
    id bigint primary key default nextval('global_seq'),
    name varchar not null,
    measure varchar not null,
    user_id bigint not null,
    foreign key (user_id) references users (id) on delete cascade
);
create unique index exercise_types_unique__user_id_name_idx on exercise_types (user_id, name);

create table workouts (
    id bigint primary key default nextval('global_seq'),
    date_time timestamp not null,
    user_id bigint not null,
    foreign key (user_id) references users (id) on delete cascade
);
create unique index workouts_unique_user_id_datetime_idx on workouts (user_id, date_time);

create table exercises (
    id bigint primary key default nextval('global_seq'),
    quantity integer not null,
    exercise_type_id bigint not null,
    workout_id bigint not null,
    foreign key (exercise_type_id) references exercise_types (id) on delete cascade,
    foreign key (workout_id) references workouts (id) on delete cascade
);
