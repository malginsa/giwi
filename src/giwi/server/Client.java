package giwi.server;

public class Client {
	private Integer id;
	private String name;
	private String password;

	public Client(Integer id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}
	
	public Client(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Client [id=" + this.id + 
			", name=" + this.name + 
			", password=" + this.password + "]";
	}
}
