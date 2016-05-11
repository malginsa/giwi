package com.epam.giwi.mvc.services;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.epam.giwi.entities.Transaction;

public class TransactionsServiceStub {

	private List<Transaction> transactions = new LinkedList<>();
	
	public TransactionsServiceStub() {
		transactions.addAll( Arrays.asList( new Transaction[] {
				new Transaction(0L, 10, new Date(new Date().getTime()-1000)),
				new Transaction(0L, -10, new Date(new Date().getTime()-100)),
				new Transaction(0L, 20, new Date(new Date().getTime()-80)),
				new Transaction(0L, -20, new Date(new Date().getTime()-70)),
				new Transaction(1L, 100, new Date(new Date().getTime()-80)),
				new Transaction(1L, -100, new Date(new Date().getTime()-50))
		})	);
	}
	
	public List<Transaction> findAll() {
		return this.transactions;
	}
	
	public List<Transaction> find( Long cardId ) {
		return this.transactions.stream().filter(t -> t.getCardId().equals(cardId)).collect(toList());
	}

	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}
}
