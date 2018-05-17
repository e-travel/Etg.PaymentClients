package com.etraveli.payments.client.controllers.rest;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etraveli.payments.client.dto.SimplePaymentsRoutingResponseWrapperDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingRequestDto;
import com.etraveli.payments.client.services.PaymentsService;

@RestController
public class GatewaysController {
	private final static Logger logger = LoggerFactory.getLogger(GatewaysController.class);
	private static Map<String, List<String>> gatewaysPerCurrencyCache = null;

	@Autowired
	private final PaymentsService paymentsService;

	public GatewaysController(PaymentsService paymentsService) {
		this.paymentsService = paymentsService;
	}
	
	@RequestMapping(path = "/api/gateways/simple_routing", method = RequestMethod.GET,
			produces = "application/json", consumes = "application/json")
	public SimplePaymentsRoutingResponseWrapperDto getGatewaysWithRouting(
			@RequestParam String bin, @RequestParam String currency, @RequestParam int amount) {
		logger.debug("Initiating card payment... ");
		
		if(gatewaysPerCurrencyCache == null)
			gatewaysPerCurrencyCache = paymentsService.getSupportedCardGatewaysPerCurrency();
		
		if(!gatewaysPerCurrencyCache.containsKey(currency))
			return null;
		
		List<String> availableGateways = gatewaysPerCurrencyCache.get(currency);
		
		SimplePaymentsRoutingRequestDto simplePaymentsRoutingRequest = new SimplePaymentsRoutingRequestDto();
		
		simplePaymentsRoutingRequest.setPreferredGateways(availableGateways);
		simplePaymentsRoutingRequest.setBin(bin);
		simplePaymentsRoutingRequest.setCurrency(currency);
		simplePaymentsRoutingRequest.setTransactionValue(amount);

		return paymentsService.performSimpleRouting(simplePaymentsRoutingRequest);
	}
}
