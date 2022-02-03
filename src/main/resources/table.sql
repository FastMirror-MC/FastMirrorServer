create table if not exists t_core (
    "name" varchar(16) not null,
    "version" varchar(8) not null,
    "build" varchar(8) not null,
    "release" boolean not null,
    "update" timestamp not null,
    "sha1" varchar(512) not null,
    constraint t_core_pkey primary key ("name", "version", "build")
);
create table if not exists t_submit_log (
    "id" serial primary key,
    "client" varchar(255) not null,
    "name" varchar(16) not null,
    "version" varchar(8) not null,
    "build" varchar(8) not null,
    "time" timestamp not null
);