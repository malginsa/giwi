package giwi.server;

/**
* Карта клиента: номер карты - строка 16 символов, 
* заблокирована или активна, id счёта, к которому она привязана
*/

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
	
	public String getNumber() {
		return this.number;
	}
	
	public Boolean getIsBlocked() {
		return this.isBlocked;
	}

	public Integer getAccount_id() {
		return this.account_id;
	}

}
