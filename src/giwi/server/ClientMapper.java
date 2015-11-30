package giwi.server;

import org.apache.ibatis.annotations.Select;

/**
 * Мэпер таблицы клиентов
 * create table client (
 * 	id integer primary key auto_increment,		# id клиента
 * 	name varchar(255),								# имя
 * 	password varchar(255)							# пароль
 * );
 */

public interface ClientMapper {

	@Select("select * from client where name = #{param1} and password = #{param2}")
	Client getClientByName(String param1, String param2);
	
	@Select("select * from client where id = #{clientId}")
	Client getClientById(Integer clientId);
	
}
