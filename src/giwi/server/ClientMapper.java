package giwi.server;

import org.apache.ibatis.annotations.Select;

public interface ClientMapper {

	@Select("select * from client where name = #{param1} and password = #{param2}")
	Client getClientByName(String param1, String param2);
	
	@Select("select * from client where id = #{clientId}")
	Client getClientById(Integer clientId);
	
}
