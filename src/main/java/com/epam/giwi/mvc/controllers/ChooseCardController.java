package com.epam.giwi.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epam.giwi.mvc.services.CardsServiceStub;

@Controller
public class ChooseCardController {

	@Autowired
	private CardsServiceStub cardsServiceStub;

	@RequestMapping("/")
	public String goHome(Model model){
		model.addAttribute("cards", this.cardsServiceStub.findAll());
		return "choose_card";
	}
	
}
