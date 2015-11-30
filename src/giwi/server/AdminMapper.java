package giwi.server;

import org.apache.ibatis.annotations.Select;

/**
 * Мэпер талицы клиентов
 * create table admin (
 * 	id integer primary key auto_increment,		# id админа
 * 	name varchar(255),								# имя
 * 	password varchar(255)							# пароль
 * );
 */

public interface AdminMapper {

	@Select("select * from admin where name = #{param1} and password = #{param2}")
	Admin getAdminByName(String param1, String param2);

	@Select("select * from admin where id = #{adminId}")
	Admin getAdminById(Integer adminId);
	
}
