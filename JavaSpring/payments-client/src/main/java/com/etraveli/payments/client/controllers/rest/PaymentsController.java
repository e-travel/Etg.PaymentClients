package com.etraveli.payments.client.controllers.rest;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etraveli.payments.client.dto.ChargeResponseWrapperDto;
import com.etraveli.payments.client.dto.PaymentRequestDto;
import com.etraveli.payments.client.dto.PaymentResponseDto;
import com.etraveli.payments.client.dto.integration.AuthenticationModes;
import com.etraveli.payments.client.dto.integration.ChargeRequestDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingRequestDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingResponseDto;
import com.etraveli.payments.client.factories.ChargeRequestDtoFactory;
import com.etraveli.payments.client.services.PaymentsService;

@RestController
public class PaymentsController {
	private final static Logger logger = LoggerFactory.getLogger(PaymentsController.class);
	private static Map<String, List<String>> gatewaysPerCurrencyCache = null;

	@Autowired
	private final PaymentsService paymentsService;

	public PaymentsController(PaymentsService paymentsService) {
		this.paymentsService = paymentsService;
	}

	@RequestMapping(path = "/api/payments/card", method = RequestMethod.POST, 
			produces = "application/json", consumes = "application/json")
	public PaymentResponseDto createPayment(@RequestBody PaymentRequestDto paymentRequestDto, HttpServletRequest request) {
		logger.debug("Initiating card payment... ");
		
		// Payments initialization
		if(gatewaysPerCurrencyCache == null)
			gatewaysPerCurrencyCache = paymentsService.getSupportedCardGatewaysPerCurrency();
		
		if(!gatewaysPerCurrencyCache.containsKey(paymentRequestDto.getCurrency()))
			//TODO log error and/or throw exception
			return null;
		
		// Payments routing
		List<String> availableGateways = gatewaysPerCurrencyCache.get(paymentRequestDto.getCurrency());
		
		SimplePaymentsRoutingRequestDto simplePaymentsRoutingRequest = new SimplePaymentsRoutingRequestDto();
		
		simplePaymentsRoutingRequest.setBin(paymentRequestDto.getToken().getMetadata().getBin());
		simplePaymentsRoutingRequest.setCurrency(paymentRequestDto.getCurrency());
		simplePaymentsRoutingRequest.setPreferredGateways(availableGateways);
		simplePaymentsRoutingRequest.setTransactionValue(paymentRequestDto.getAmount());
		
		SimplePaymentsRoutingResponseDto simplePaymentsRoutingResponseDto = paymentsService
				.performSimpleRouting(simplePaymentsRoutingRequest) 
				.getSimplePaymentsRoutingResponseDto();
		
		ChargeRequestDto chargeRequest = ChargeRequestDtoFactory.getChargeRequest(paymentRequestDto);
		chargeRequest.setClientIp(request.getRemoteAddr());
		
		PaymentResponseDto paymentResponse = new PaymentResponseDto();
		paymentResponse.setChargeRequest(chargeRequest);

		List<String> orderedGateways = simplePaymentsRoutingResponseDto.getOrderedGateways();
		
		int cnt = 0, totalGateways = orderedGateways.size();
		
		for(String gateway:orderedGateways) {
			cnt ++;
			logger.info("Attempting to charge with " + gateway + " (attempt: " + cnt + " / " + totalGateways + ")");
			chargeRequest.setGateway(gateway);
			
			ChargeResponseWrapperDto chargeResponseWrapper = paymentsService.performCharge(chargeRequest);
			
			paymentResponse.getChargeAttempts().add(chargeResponseWrapper);
			paymentResponse.getAttemptedGateways().add(gateway);
			
			if (chargeResponseWrapper.getChargeResponse().isPaymentSucceded()) {
				paymentResponse.setPaymentSuccessful(true);
				break;
			}
		}
		
		return paymentResponse;
	}
}
