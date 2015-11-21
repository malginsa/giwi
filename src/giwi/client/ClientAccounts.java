package giwi.client;

import java.util.LinkedList;
import java.util.List;

import giwi.shared.Account;

public class ClientAccounts {
	
	private static List<Account> accounts;
	
	public static void setAccounts(List<Account> _account) {
		accounts = _account;
	}
	
	public static List<String> getActiveCardNumbers() {
		List<String> cards = new LinkedList<String>();
		for (Account account : accounts) {
			if (account.isNotBlocked()) {
				cards.add(account.getCardNumber());
			}
		}
		return cards;
	}
	
	public static Integer size() {
		return accounts.size();
	}
	
}
