package giwi.shared;

import java.io.Serializable;

public class Account implements Serializable{
	
	private static final long serialVersionUID = 7125727578773086412L;
	
	private Integer client_id;
	private String cardNumber;
	private Integer balance;
	private Boolean isBlocked;

	// this constructor dedicated to GWT's strange serialization model
	private Account() {}
	
	public Account(Integer client_id, String cardNumber, 
			Integer balance, Boolean isBlocked) 
	{
		this.client_id = client_id;
		this.cardNumber = cardNumber;
		this.balance = balance;
		this.isBlocked = isBlocked;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	
	public Integer getBalance() {
		return balance;
	}
	
	public void setbalance(Integer balance) {
		this.balance = balance;
	}

	public Boolean getIsBlocked() {
		return isBlocked;
	}
	
	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	@Override
	public String toString() {
		return "Account [client_id=" + this.client_id + 
				", cardNumber=" + this.cardNumber + 
				", balance=" + this.balance + 
				", isBlocked="	+ this.isBlocked + "]";
	}
}
