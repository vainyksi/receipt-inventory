create table if not exists receipts
(
    id   SERIAL PRIMARY KEY,
    code varchar(255) not null
);
