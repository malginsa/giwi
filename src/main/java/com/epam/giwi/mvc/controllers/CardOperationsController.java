package com.epam.giwi.mvc.controllers;

import java.util.Date;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.epam.giwi.entities.Transaction;
import com.epam.giwi.mvc.data.OperationsSubmitForm;
import com.epam.giwi.mvc.services.CardsServiceStub;
import com.epam.giwi.mvc.services.TransactionsServiceStub;
import com.epam.giwi.mvc.validators.OperationsValidator;

@Controller
@RequestMapping("/operations")
public class CardOperationsController {

	final static Logger logger = LoggerFactory.getLogger(CardOperationsController.class);

	@Autowired
	private CardsServiceStub cardsServiceStub;

	@Autowired
	private TransactionsServiceStub transactionsServiceStub;

//	?
//	@ModelAttribute("card")
//	public Card addCardToModel(@PathVariable("cardId") Long cardId) {
//		logger.debug("addCardToModel is invoked");
//		return this.cardsServiceStub.find(cardId);
//	}

	//	TODO merge do_withdrawal and do_deposit
	@RequestMapping(value="/deposit/{cardId}", method=RequestMethod.GET)
	public String deposit(Model model, @PathVariable("cardId") Long cardId) {
			//	TODO Is it his card?
		model.addAttribute("card", this.cardsServiceStub.find(cardId));
		model.addAttribute("submitForm", new OperationsSubmitForm());
		return "do_deposit";
	}

//	using validation.api :: OperationsValidator.java
	@RequestMapping(value="/deposit/{cardId}", method=RequestMethod.POST)
	public String doDeposit(
			Model model, 
			@PathVariable("cardId") Long cardId, 
			@Valid @ModelAttribute OperationsSubmitForm submitForm, Errors errors) {

		model.addAttribute("card", this.cardsServiceStub.find(cardId));
		if (errors.hasErrors()) {
			logger.error(submitForm + " has an error");
			model.addAttribute("submitForm", new OperationsSubmitForm());
			return "do_deposit";
		}
			//	TODO Is it his card?
			//	TODO The Card is not blocked?
		Integer amount = Integer.parseInt(submitForm.getAmount());
		Transaction transaction = new Transaction(cardId, amount, new Date());
		this.transactionsServiceStub.addTransaction(transaction);
		this.cardsServiceStub.increaseBalance(cardId, amount);
		return "card_info";
	}

	@RequestMapping(value="/withdrawal/{cardId}", method=RequestMethod.GET)
	public String withdrawal(Model model, @PathVariable("cardId") Long cardId) {
			//	TODO Is it his card?
		model.addAttribute("card", this.cardsServiceStub.find(cardId));
		return "do_withdrawal";
	}

	// This is not correct implementation, because of redirect problem
	// TODO To solve this problem use FlashAttribute, not RedirectAttribute
	@RequestMapping(value="/withdrawal/{cardId}", method=RequestMethod.POST)
	public String doWithdrawal(
			Model model, 
			@PathVariable("cardId") Long cardId, 
			@Valid @ModelAttribute OperationsSubmitForm submitForm, Errors errors) {

		model.addAttribute("card", this.cardsServiceStub.find(cardId));
		if (errors.hasErrors()) {
			logger.error(submitForm + " has an error");
			model.addAttribute("submitForm", new OperationsSubmitForm());
			return "do_deposit";
		}
				//	TODO Is it his card?
				//	? TODO Whether Balance is allowed a Withdrawal? The Card is not blocked?
		Integer amount = Integer.parseInt(submitForm.getAmount());
		Transaction transaction = new Transaction(cardId, - amount, new Date());
		this.transactionsServiceStub.addTransaction(transaction);
		this.cardsServiceStub.decreaseBalance(cardId, amount);
		return "card_info";
	}

	@RequestMapping(value="/block/{cardId}")
	public String block(Model model, @PathVariable("cardId") Long cardId) {
//		TODO Whether Card is already blocked
		if (true) throw new RuntimeException("Test exception...");
		this.cardsServiceStub.blockCard(cardId);
		model.addAttribute("card", this.cardsServiceStub.find(cardId));
		return "card_info";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
// ? why binding yet Card.java 
		binder.addValidators(new OperationsValidator());
//		logger.error("initBinder is invoked");
	}
}
