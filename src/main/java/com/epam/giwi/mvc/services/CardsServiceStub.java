package com.epam.giwi.mvc.services;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.epam.giwi.entities.Card;

public class CardsServiceStub {

	private List<Card> cards = new LinkedList<>();

	public CardsServiceStub() {
		this.cards.addAll(Arrays.asList(new Card[] { 
				new Card(0L, "1111", 11L, 100, false),
				new Card(1L, "2222", 12L, 500, true), 
				new Card(2L, "3333", 13L, 0, false) }));
	}

	public List<Card> findAll() {
		return this.cards;
	}

	public Card find(Long id) {
		return this.cards.stream()
				.filter(c -> c.getId().equals(id))
				.findFirst().orElse(null);
	}

	private void changeBalance(Long cardId, Integer amount) {
		this.cards.stream()
			.filter(c -> c.getId().equals(cardId))
			.forEach(c -> c.setBalance(c.getBalance() + amount));
	}
	
	public void increaseBalance(Long cardId, Integer amount) {
		this.changeBalance(cardId, amount);
	}

	public void decreaseBalance(Long cardId, Integer amount) {
		this.changeBalance(cardId, - amount);
	}

	public void blockCard(Long cardId) {
		this.cards.stream()
			.filter(c -> c.getId().equals(cardId))
			.forEach(c -> c.setIsBlocked(true));
	}

}
