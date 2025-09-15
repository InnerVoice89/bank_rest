create table users
(
    id       bigint generated always as identity
        primary key,
    name     varchar(200) not null,
    surname  varchar(200) not null,
    username varchar(200) not null
        unique,
    password varchar(200) not null
);