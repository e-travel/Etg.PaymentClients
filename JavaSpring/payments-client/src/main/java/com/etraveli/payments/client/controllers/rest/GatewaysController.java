package com.etraveli.payments.client.controllers.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.etraveli.payments.client.dto.GatewaysCurrenciesMappingDto;
import com.etraveli.payments.client.dto.SimplePaymentsRoutingResponseWrapperDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingRequestDto;
import com.etraveli.payments.client.services.FileStorageService;
import com.etraveli.payments.client.services.PaymentsService;

@RestController
public class GatewaysController {
	private static final Logger logger = LoggerFactory.getLogger(GatewaysController.class);

	private final PaymentsService paymentsService;
	private final FileStorageService fileStorageService;

	@Autowired()
	public GatewaysController(PaymentsService paymentsService, FileStorageService fileStorageService) {
		this.paymentsService = paymentsService;
		this.fileStorageService = fileStorageService;
	}

	@RequestMapping(path = "/api/gateways/simple_routing", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
	public SimplePaymentsRoutingResponseWrapperDto getGatewaysWithRouting(@RequestParam String bin,
			@RequestParam String currency, @RequestParam int amount) {
		logger.debug("Initiating card payment... ");

		GatewaysCurrenciesMappingDto gatewaysPerCurrency = fileStorageService.<GatewaysCurrenciesMappingDto>loadData("gateways_per_currency.json",GatewaysCurrenciesMappingDto.class);
		
		if (gatewaysPerCurrency == null) {
			gatewaysPerCurrency = paymentsService.getSupportedCardGatewaysPerCurrency();
			fileStorageService.<GatewaysCurrenciesMappingDto>saveData("gateways_per_currency.json", gatewaysPerCurrency, GatewaysCurrenciesMappingDto.class);
		}

		if (gatewaysPerCurrency == null)
			return null;
		if(!gatewaysPerCurrency.getGatewaysPerCurrency().containsKey(currency))
			return null;

		List<String> availableGateways = gatewaysPerCurrency.getGatewaysPerCurrency().get(currency);

		SimplePaymentsRoutingRequestDto simplePaymentsRoutingRequest = new SimplePaymentsRoutingRequestDto();

		simplePaymentsRoutingRequest.setPreferredGateways(availableGateways);
		simplePaymentsRoutingRequest.setBin(bin);
		simplePaymentsRoutingRequest.setCurrency(currency);
		simplePaymentsRoutingRequest.setTransactionValue(amount);

		return paymentsService.performSimpleRouting(simplePaymentsRoutingRequest);
	}
}
