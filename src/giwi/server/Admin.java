package giwi.server;

/**
* Информация о админе: его id, имя, пароль
*/

public class Admin {

	private Integer id;
	private String name;
	private String password;

	public Admin(Integer id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public Integer getId() {
		return id;
	}

}
