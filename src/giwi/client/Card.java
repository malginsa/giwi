package giwi.client;

public class Card {

	private String number;
	private Boolean isBlocked;
	private Integer account_id;

	public Card(String number, Boolean isBlocked, Integer account_id) {
		this.number = number;
		this.isBlocked = isBlocked;
		this.account_id = account_id;
	}

	@Override
	public String toString() {
		return "Card [number=" + this.number +
				 ", isBlocked=" + this.isBlocked +
				 ", balance=" + this.account_id + "]";
	}
}
