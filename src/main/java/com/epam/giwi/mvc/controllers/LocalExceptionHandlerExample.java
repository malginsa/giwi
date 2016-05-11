package com.epam.giwi.mvc.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LocalExceptionHandlerExample {

	// Example how to handle an exception
	@RequestMapping(value="/exception")
	public String exceptionThrower() {
		if (true) throw new RuntimeException("Test exception...");
		return "welcome";
	}

	// Scope of this ExceptionHandler is bounded by ExceptionHandlerExample controller  
//	@ExceptionHandler(Exception.class)  // RuntimeException will be caught by local handler
	@ExceptionHandler(NullPointerException.class)  // RuntimeException will be caught by global handler
	public String localExceptionHandler(HttpServletRequest request /* prevent databinding*/) {
		return "local_error";
	}
	
}
