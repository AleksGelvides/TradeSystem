create table finance_instrument(
    id bigserial primary key,
    figi varchar unique,
    tool varchar,
    nominal double precision,
    create_date timestamp not null
)