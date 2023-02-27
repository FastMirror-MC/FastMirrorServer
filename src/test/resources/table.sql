create table if not exists t_project (
    "name" varchar(31) primary key,
    "url" varchar(255) not null,
    "tag" varchar(63) not null,
    "recommend" boolean not null
);
create table if not exists t_core (
    "name" varchar(31) references t_project("name"),
    "mc_version" varchar(15) not null,
    "core_version" varchar(31) not null,
    "update_time" timestamp not null,
    "sha1" varchar(127) not null,
    "filetype" varchar(31) not null,
    "path" varchar(255) not null,
    "enable" boolean not null,
    constraint t_core_pk primary key ("name", "mc_version", "core_version")
);
create table if not exists t_account (
    "name" varchar(20) primary key,
    "authorization_string" varchar(255) not null,
    "permission" varchar(20) not null,
    "last_login" timestamp
);
