package com.etraveli.payments.client.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.etraveli.payments.client.dto.ChargeResponseWrapperDto;
import com.etraveli.payments.client.dto.PaymentRequestDto;
import com.etraveli.payments.client.dto.PaymentResponseDto;
import com.etraveli.payments.client.dto.integration.ChargeRequestDto;
import com.etraveli.payments.client.factories.ChargeRequestDtoFactory;
import com.etraveli.payments.client.services.FileStorageService;
import com.etraveli.payments.client.services.PaymentsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
public class CardPaymentsController {
	private static final Logger logger = LoggerFactory.getLogger(CardPaymentsController.class);

	private final PaymentsService paymentsService;
	private final FileStorageService fileStorageService;
	
	@Autowired
	public CardPaymentsController(PaymentsService paymentsService, FileStorageService fileStorageService) {
		this.paymentsService = paymentsService;
		this.fileStorageService = fileStorageService;
	}
	
	@RequestMapping(method = RequestMethod.GET, path = { "/", "/card_payments" }, name = "index")
	public ModelAndView index() {
		return new ModelAndView("card_payments/index", "cardInfo", new Object());
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/card_payments/success_3d", name = "yandex_success_3d")
	public ModelAndView getYandexSuccess3d() {
		return new ModelAndView("card_payments/yandex_success_3d");
	}
	
	@RequestMapping(method = RequestMethod.GET, path = "/card_payments/failure_3d", name = "yandex_failure_3d")
	public ModelAndView getYandexFailure3d() {
		return new ModelAndView("card_payments/yandex_failure_3d");
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/card_payments/return_from_3d", name = "return_from_3d")
	public ModelAndView postReturnFrom3d(@RequestParam("PaRes") String paRes, @RequestParam String MD, 
			@RequestParam("internal_payment_identifier") String internalPaymentIdentifier) throws JsonProcessingException {

		Map<String, String> model = new HashMap<>();
		
		if (internalPaymentIdentifier == null || internalPaymentIdentifier.length() == 0) {
			model.put("outcome", "Card Verification Failure");
			model.put("paymentResponse", "{}");
		} else {
			model.put("outcome", "Card Verification Success");
			String filename = internalPaymentIdentifier + ".json";
			PaymentRequestDto paymentRequest = fileStorageService.<PaymentRequestDto>loadData(filename);
			ChargeRequestDto chargeRequest = ChargeRequestDtoFactory.getChargeRequest(paymentRequest);
			chargeRequest.setPayload(paRes);
			ChargeResponseWrapperDto chargeResponseWrapper = paymentsService.performCharge(chargeRequest);
			ObjectMapper mapper = new ObjectMapper();
			PaymentResponseDto paymentResponse = new PaymentResponseDto();
			
			paymentResponse.addPaymentStep("Card verification result",
					mapper.writeValueAsString(chargeRequest),
					mapper.writeValueAsString(chargeResponseWrapper.isSuccessStatusCodeReceived()
							? chargeResponseWrapper.getChargeResponse()
							: chargeResponseWrapper.getErrorContent()),
					chargeResponseWrapper.isSuccessStatusCodeReceived() ? "Success" : "Failure");
			
			model.put("paymentResponse", mapper.writeValueAsString(paymentResponse));
		}
		
		return new ModelAndView("card_payments/return_from_3d", model);
	}
}
