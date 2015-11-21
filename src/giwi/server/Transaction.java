package giwi.server;

public class Transaction {

	private Integer client_id;
	private String fromCard;
	private String toCard;
	private Integer amount;

	private Transaction() {}
	
	public Transaction(Integer client_id, String fromCard, String toCard, Integer amount) {
		this.client_id = client_id;
		this.fromCard = fromCard;
		this.toCard = toCard;
		this.amount = amount;
	}

	public Integer getClient_id() {
		return client_id;
	}

	public void setClient_id(Integer client_id) {
		this.client_id = client_id;
	}

	public String getFromCard() {
		return fromCard;
	}

	public void setFromCard(String fromCard) {
		this.fromCard = fromCard;
	}

	public String getToCard() {
		return toCard;
	}

	public void setToCard(String toCard) {
		this.toCard = toCard;
	}
	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
