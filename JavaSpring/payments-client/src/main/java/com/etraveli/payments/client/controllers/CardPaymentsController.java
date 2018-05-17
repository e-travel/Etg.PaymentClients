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
	
	@RequestMapping(method = RequestMethod.GET, path = "/card_payments/success_3d", name = "success_3d")
	public ModelAndView success_3d() {
		return new ModelAndView("card_payments/success_3d");
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/card_payments/failure_3d", name = "failure_3d")
	public ModelAndView failure_3d() {
		return new ModelAndView("card_payments/failure_3d");
	}
}
