package com.epam.giwi.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.giwi.mvc.services.CardsServiceStub;
import com.epam.giwi.mvc.services.TransactionsServiceStub;

@Controller
@RequestMapping("/card")
public class CardController {

	@Autowired
	private CardsServiceStub cardsServiceStub;
	
	@RequestMapping(value="/{cardId}")
	public String findCard(Model model, @PathVariable("cardId") Long cardId) {
		model.addAttribute("card", this.cardsServiceStub.find(cardId));
		return "card_info";
	}

}
