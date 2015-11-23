package giwi.server;

import org.apache.ibatis.annotations.Select;

public interface AdminMapper {

	@Select("select * from admin where name = #{param1} and password = #{param2}")
	Admin getAdmin(String param1, String param2);

}
