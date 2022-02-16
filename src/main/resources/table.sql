create table if not exists t_core (
    "name" varchar(31) not null,
    "version" varchar(15) not null,
    "build" integer not null,
    "core_version" varchar(31) not null,
    "release" boolean not null,
    "update" timestamp not null,
    "sha1" varchar(127) not null,
    "path" varchar(255) not null,
    constraint t_core_pkey primary key ("name", "version", "core_version")
);
create table if not exists t_submit_log (
    "id" serial primary key,
    "client" varchar(255) not null,
    "name" varchar(31) not null,
    "version" varchar(15) not null,
    "core_version" varchar(31) not null,
    "time" timestamp not null
);
