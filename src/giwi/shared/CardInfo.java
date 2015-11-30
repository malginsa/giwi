package giwi.shared;

import java.io.Serializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Информация о карте, которая передаётся клиенту 
 * агрегируется из классов Card и Account:
 * номер карты клиента, заблокирована или активна, баланс на счёте
 */

public class CardInfo implements Serializable {

	private static final long serialVersionUID = -3469183068442440482L;

	public static final ProvidesKey<CardInfo> KEY_PROVIDER = 
			new ProvidesKey<CardInfo>() {
		@Override
		public Object getKey(CardInfo card) {
			return null == card ? null : card.getNumber();
		} 
	};

	private String number;
	private Boolean isBlocked;
	private Integer balance;

	// used by GWT's serialization model
	@SuppressWarnings("unused")
	private CardInfo() {}
	
	public CardInfo(String number, Boolean isBlocked, Integer balance) {
		this.number = number;
		this.isBlocked = isBlocked;
		this.balance = balance;
	}

	public String getNumber() {
		return number;
	}

	public Boolean getIsBlocked() {
		return isBlocked;
	}

	public Integer getStatusId() {
		return isBlocked ? 1 : 0;
	}
	
	public void setIsBlocked(Boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public void doBlock() {
		this.isBlocked = true;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "CardInfo [number=" + this.number +
				 ", isBlocked=" + this.isBlocked +
				 ", balance=" + this.balance + "]";
	}

}
