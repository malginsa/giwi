
mysql -u payments -p111 payments
SET NAMES utf8 COLLATE utf8_unicode_ci;

create database payments
	CHARACTER SET utf8 
	COLLATE utf8_unicode_ci;

use payments;

create table client (
	id integer primary key auto_increment,
	name varchar(255),
	password varchar(255)
	);

create table admin (
	name varchar(255),
	password varchar(255)
	);

create table account (
	client_id integer,	# -> client.id
	cardNumber varchar(16),
	balance integer,
	isBlocked bit
	);

create table transaction (
	client_id integer, # -> client.id
	fromCard varchar(16),
	toCard varchar(16),
	amount integer,
	timestamp datetime
	);

grant all on payments.* to 'payments' identified by '111';
flush privileges;

insert into client values (1, "Askur", "11111");
insert into client values (2, "Embla", "22222");
insert into client values (3, "Torgir", "33333");
insert into client values (0, "Kudasov", "44444");

insert into account values (1, "1111111111111111", 11, FALSE);
insert into account values (1, "1111111111111112", 12, FALSE);
insert into account values (2, "2222222222222222", 22, FALSE);
insert into account values (3, "3333333333333333", 33, FALSE);

insert into admin values ("sysop", "123");

alter table account change amount balance integer;
alter table transaction drop column description;
alter table transaction add column amount integer;
rename table person to client;

update account set balance = balance - amount where cardNumber = fromCard;
insert into transaction values (1, "1", "2", NOW());

select * from transaction order by timestamp desc limit 1;
select balance from account where cardNumber = "2222222222222222";
update account set isBlocked = FALSE where cardNumber = "1111111111111111";



# посмотреть collation по-умолчанию для кодировки utf8
SHOW COLLATION LIKE "utf8%";


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

