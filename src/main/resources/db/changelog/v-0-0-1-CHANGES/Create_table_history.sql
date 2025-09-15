CREATE TABLE history
(
    id                 bigint generated always as identity,
    transfer_from_card varchar(255) not null,
    transfer_from_username varchar(255) not null,
    transfer_to_card   varchar(255) not null,
    transfer_to_username varchar(255) not null,
    amount numeric(15,2) not null ,
    creation_date timestamp not null
)