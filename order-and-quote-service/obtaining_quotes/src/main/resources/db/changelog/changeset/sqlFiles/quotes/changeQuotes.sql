create table quote_histories(
    id bigserial primary key,
    fin_instr_id bigint,
    time_frame_id integer,
    high numeric not null,
    low numeric not null,
    open numeric not null,
    close numeric not null,
    open_date timestamp not null,
    constraint fk_fiid foreign key (fin_instr_id) references finance_instrument(id),
    constraint fk_tf foreign key (time_frame_id) references time_frame(id)
)