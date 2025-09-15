create table cards
(
    card_id      bigint generated always as identity
        primary key,
    card_number  varchar(200)        not null,
    user_id      bigint              not null,
    status       varchar(200)        not null,
    balance      numeric(15, 2)      not null,
    last4        varchar(200)        not null,
    expiry_year  bigint default 2025 not null,
    expiry_month bigint default 10   not null
);