package giwi.server;

public class Account {
	
	private Integer id;
	private Integer balance;
	private Integer client_id;
	
	public Account(Integer id, Integer balance, Integer client_id) 
	{
		this.id = id;
		this.balance = balance;
		this.client_id = client_id;
	}
	
	public Integer getId() {
		return id;
	}

	public Integer getBalance() {
		return this.balance;
	}

	public Integer getClientId() {
		return client_id;
	}
	
	@Override
	public String toString() {
		return "Account [id=" + this.id +
				", balance=" + this.balance + 
				", client_id=" + this.client_id + "]";
	}

}
