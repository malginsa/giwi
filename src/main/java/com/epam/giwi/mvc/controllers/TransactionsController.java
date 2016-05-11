package com.epam.giwi.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.giwi.mvc.services.CardsServiceStub;
import com.epam.giwi.mvc.services.TransactionsServiceStub;

@Controller
@RequestMapping("/transactions")
public class TransactionsController {

	@Autowired
	private CardsServiceStub cardsServiceStub;
	
	@Autowired
	private TransactionsServiceStub transactionsServiceStub;

	@RequestMapping(value="/{cardId}")
	public String findTransactions(Model model, @PathVariable("cardId") Long cardId) {
		model.addAttribute("transactions", this.transactionsServiceStub.find(cardId));
		model.addAttribute("card", this.cardsServiceStub.find(cardId));
		return "transactions_info";
	}

}
