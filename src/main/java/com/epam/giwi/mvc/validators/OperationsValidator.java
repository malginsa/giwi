package com.epam.giwi.mvc.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.epam.giwi.entities.Card;
import com.epam.giwi.mvc.data.OperationsSubmitForm;

public class OperationsValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
//		System.out.println("	clazz=" + clazz.getName());
//		System.out.println("	OperationsSubmitForm=" + OperationsSubmitForm.class.getName());
//		return true;
		return OperationsSubmitForm.class.equals(clazz) ||
			// костыль
			Card.class.equals(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		OperationsSubmitForm form = (OperationsSubmitForm) object;
		
		int amount = 0;
		
		try {
			amount = Integer.parseInt(form.getAmount(), 10);
		} catch (NumberFormatException e) {
			System.out.println("	entered not integer");
//			errors.rejectValue("amount", "submitForm.amount", "Enter only integer value");
			errors.rejectValue("amount", null, "Enter only integer value");
			return;
		}
		
		if( amount < 1 ) {
			System.out.println("	entered negative integer");
			errors.rejectValue("amount", "submitForm.amount", "Enter only positive integer value");
			return;
		}

	}

}
