giwi.sql

mysql -u giwi -pgiwi giwi
SET NAMES utf8 COLLATE utf8_unicode_ci;

create database giwi
	CHARACTER SET utf8 
	COLLATE utf8_unicode_ci;

grant all on giwi.* to 'giwi' identified by 'giwi';
flush privileges;

use giwi;

create table client (
	id integer primary key auto_increment,
	name varchar(255),
	password varchar(255)
	);

create table admin (
	id integer primary key auto_increment,
	name varchar(255),
	password varchar(255)
	);

create table account (
	id integer primary key auto_increment,
	balance integer,
	client_id integer
	);

create table card (
	number varchar(16),
	isBlocked bit,
	account_id integer
	);

insert into client values (1, "Askur", "1");
insert into client values (2, "Embla", "1");
insert into client values (3, "Torgir", "1");
insert into client values (4, "Kudasov", "1");

insert into account values (1, 11, 1);
insert into account values (2, 12, 1);
insert into account values (3, 22, 2);
insert into account values (4, 33, 3);
insert into account values (5, 66, 6);
insert into account values (6, 66, 1);

insert into card values ("1111111111111111", FALSE, 1);
insert into card values ("1111111111111112", FALSE, 2);
insert into card values ("2222222222222222", FALSE, 3);
insert into card values ("3333333333333333", FALSE, 4);

insert into admin values (1, "admin", "1");
insert into admin values (2, "админ", "1");
insert into admin values (3, "root", "1");
insert into admin values (4, "sysop", "1");

select card.number, card.isblocked, account.balance from account, card where account.client_id = 1 and account.id = card.account_id;

select account.* from card, account where card.number = 1111111111111111 and account.id = card.account_id;

select card.number, card.isblocked, account.balance from account, card where card.number = 1111111111111111 and account.id = card.account_id

create table transaction (
	account_id integer,
	amount integer,
	date date,
	time time,
	);

select transaction.amount, transaction.timestamp from card, transaction where card.account_id = transaction.account_id and card.number = "1111111111111111";

alter table transaction add column date date;
alter table transaction add column time time;
alter table transaction drop column timestamp;


mysql -u giwi -pgiwi giwi
select * from account where client_id = 1;
select * from transaction;
select * from card where isBlocked = TRUE




-----   utf8  -----

/etc/mysql/my.cnf :

[client]
default_character_set = utf8

[mysqld]
character-set-server = utf8
collation-server = utf8_unicode_ci
init_connect='SET collation_connection = utf8_unicode_ci'

проверка:
SHOW VARIABLES LIKE 'char%';
SHOW VARIABLES LIKE 'collation%';

если mysql -u root, то вручную:
SET collation_connection = utf8_unicode_ci;

