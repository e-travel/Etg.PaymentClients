package com.etraveli.payments.client.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CardPaymentsController {
	@RequestMapping(method = RequestMethod.GET, path = { "/", "/card_payments" }, name = "index")
	public ModelAndView index() {
		return new ModelAndView("card_payments/index", "cardInfo", new Object());
	}
}
