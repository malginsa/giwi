package giwi.server;

public class Transaction {

	private Integer account_id;
	private Integer amount;

//	private Transaction() {}
//	
	public Transaction(Integer account_id, Integer amount) {
		this.account_id = account_id;
		this.amount = amount;
	}
	
}
