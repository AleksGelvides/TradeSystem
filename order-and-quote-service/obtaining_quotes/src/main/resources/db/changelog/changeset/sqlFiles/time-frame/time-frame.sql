create table time_frame
(
    id bigserial primary key,
    time_frame_name varchar
);

insert into time_frame(time_frame_name)
values ('_DONT_IDENTIFIER');

insert into time_frame(time_frame_name)
values ('_5_MINUTES');

insert into time_frame(time_frame_name)
values ('_HOUR');

insert into time_frame(time_frame_name)
values ('_DAY');

