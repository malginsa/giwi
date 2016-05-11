package com.epam.giwi.mvc.data;

import org.hibernate.validator.constraints.NotBlank;

public class OperationsSubmitForm {

	@NotBlank(message="Yot must provide an amount.")
	// TODO only positive integer (using annotations from hibernate-validator)
	private String amount;

	public OperationsSubmitForm() {
	}
	
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationsSubmitForm other = (OperationsSubmitForm) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OperationsSubmitForm [amount=" + amount + "]";
	}
	
}
