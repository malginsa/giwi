package giwi.shared;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;

public class Card implements Serializable {

	private static final long serialVersionUID = -6026016480963292925L;

	public static final ProvidesKey<Card> KEY_PROVIDER = new ProvidesKey<Card>() {
		@Override
		public Object getKey(Card card) {
			return null == card ? null : card.getNumber();
		} 
	};

	private String number;
	private Boolean isBlocked;
	private Integer balance;

	// this constructor dedicated to GWT's serialization model
	private Card() {}
	
	public Card(String number, Boolean isBlocked, Integer balance) {
		this.number = number;
		this.isBlocked = isBlocked;
		this.balance = balance;
	}

	public String getNumber() {
		return number;
	}

	public Integer getStatusId() {
		return isBlocked ? 1 : 0;
	}
	
	public Boolean getIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "Card [number=" + this.number +
				 ", isBlocked=" + this.isBlocked +
				 ", balance=" + this.balance + "]";
	}

	public void doBlock() {
		this.isBlocked = true;
	}
}
