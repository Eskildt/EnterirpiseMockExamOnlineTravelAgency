create sequence hibernate_sequence start with 1 increment by 1;
create table purchase
(
    id                  bigint       not null,
    date_of_booking     date         not null,
    booked_by_userid    varchar(255) not null,
    trip_information_id bigint       not null,
    primary key (id)
);
create table trip
(
    id             bigint       not null,
    cost           bigint       not null,
    departure_date date         not null,
    description    varchar(255),
    location_name  varchar(300) not null,
    return_date    date         not null,
    title          varchar(300),
    primary key (id)
);
create table users
(
    userid          varchar(255) not null,
    email           varchar(255),
    enabled         boolean      not null,
    hashed_password varchar(255),
    last_name       varchar(128),
    name            varchar(128),
    primary key (userid)
);
create table users_booked_trips
(
    all_travelers_userid varchar(255) not null,
    booked_trips_id      bigint       not null
);
create table users_roles
(
    users_userid varchar(255) not null,
    roles        varchar(255)
);
alter table users
    add constraint UK_6dotkott2kjsp8vw4d0m25fb7 unique (email);
alter table purchase
    add constraint FKex4wa959mkii4fwx3s210ug24 foreign key (booked_by_userid) references users;
alter table purchase
    add constraint FK3tv91lsph9bbpk4lpdu231yn0 foreign key (trip_information_id) references trip;
alter table users_booked_trips
    add constraint FKqsn5ohuprxkk1s7cd348e8btl foreign key (booked_trips_id) references trip;
alter table users_booked_trips
    add constraint FK2d6a73d4l6qaq1th7ni5po6rj foreign key (all_travelers_userid) references users;
alter table users_roles
    add constraint FKnqgxij5udu4xrsqju9dtbc8pr foreign key (users_userid) references users;