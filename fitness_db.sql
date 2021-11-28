begin transaction;

drop schema if exists fitness_db;
create schema fitness_db;
set search_path to 'fitness_db';

drop table if exists User_ cascade;
drop table if exists Cycle_User_Aims cascade;
drop table if exists Cycle cascade;
drop table if exists Workout cascade;

create table User_ (
    discord_user_id char(18) primary key
);

create table Cycle_User_Aims (
    aim text,
    reflection text,
    user_ char(18) references User_(discord_user_id),
    start_date_ date primary key,
    end_date date
);

create table Cycle (
    start_date_ date primary key,
    end_date date
);

create table Workout (
    date_ date,
    user_ char(18) references User_(discord_user_id),
    evidence_description text,
    number_ int default 1
);

commit;


