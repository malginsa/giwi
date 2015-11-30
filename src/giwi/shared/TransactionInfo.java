package giwi.shared;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class TransactionInfo implements Serializable{

	private static final long serialVersionUID = 5143763598593988899L;
	
	private Integer amount;
	private Date date;
	private Time time;
	
	private TransactionInfo() {}
	
	public TransactionInfo(Integer amount, Date date, Time time) {
		this.amount = amount;
		this.date = date;
		this.time = time;
	}
	
	public Integer getAmount() {
		return amount;
	}
	public Date getDate() {
		return date;
	}
	public Time getTime() {
		return time;
	}
	
	@Override
	public String toString() {
		return "CarsTransactionInfo [" +
				"amount=" + this.amount +
				" ,date=" + this.date +
				", time=" + this.time + "]";
	}
	
}
