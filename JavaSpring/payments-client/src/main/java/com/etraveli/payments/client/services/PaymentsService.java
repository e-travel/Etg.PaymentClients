package com.etraveli.payments.client.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.etraveli.payments.client.config.IntegrationConfig;
import com.etraveli.payments.client.dto.AvailableGatewaysResponseWrapperDto;
import com.etraveli.payments.client.dto.ChargeResponseWrapperDto;
import com.etraveli.payments.client.dto.EnrollmentCheckResponseWrapperDto;
import com.etraveli.payments.client.dto.SimplePaymentsRoutingResponseWrapperDto;
import com.etraveli.payments.client.dto.SupportedCurrenciesResponseWrapperDto;
import com.etraveli.payments.client.dto.integration.ChargeRequestDto;
import com.etraveli.payments.client.dto.integration.ChargeResponseDto;
import com.etraveli.payments.client.dto.integration.EnrollmentCheckRequestDto;
import com.etraveli.payments.client.dto.integration.EnrollmentCheckResponseDto;
import com.etraveli.payments.client.dto.integration.ReversalResponseDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingRequestDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingResponseDto;

@Service
@Scope("prototype")
public class PaymentsService {
	private static final Logger logger = LoggerFactory.getLogger(PaymentsService.class);

	private static final String AVAILABLE_GATEWAYS = "/credit_card_payments/gateways";
	private static final String CHARGES = "/credit_card_payments/charges";
	private static final String ENROLLMENT_CHECKS = "/credit_card_payments/enrollment_checks";
	private static final String REVERSALS = "/reversals";
	private static final String SIMPLE_ROUTING = "/payment_routing/other";

	@Autowired
	private final IntegrationConfig integrationConfig;
	
	@Autowired
	private final RestTemplate restTemplate;

	public PaymentsService(RestTemplate restTemplate, IntegrationConfig integrationConfig) {
		this.restTemplate = restTemplate;
		this.integrationConfig = integrationConfig;
	}

	public AvailableGatewaysResponseWrapperDto getAvailableGatewaysForCardPayments() {
		AvailableGatewaysResponseWrapperDto response;

		String url = integrationConfig.getPaymentsApiUrl() + AVAILABLE_GATEWAYS;

		try {
			logger.debug("Executing HTTP GET request to: " + url);
			ResponseEntity<String[]> availableGatewaysResponseEntity = restTemplate.getForEntity(url, String[].class);

			logger.info("Available gateways:" + Arrays.toString(availableGatewaysResponseEntity.getBody()));
			response = new AvailableGatewaysResponseWrapperDto(true,
					Arrays.asList(availableGatewaysResponseEntity.getBody()));
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error("Could not get available gateways");
			response = new AvailableGatewaysResponseWrapperDto(false, new ArrayList<>());
		}

		return response;
	}

	public SupportedCurrenciesResponseWrapperDto getSupportedCurrenciesForGateay(String gateway) {
		SupportedCurrenciesResponseWrapperDto response;

		String url = integrationConfig.getPaymentsApiUrl() + AVAILABLE_GATEWAYS + "/" + gateway + "/currencies";

		try {
			logger.debug("Executing HTTP GET request to: " + url);
			ResponseEntity<String[]> supportedCurrenciesResponseEntity = restTemplate.getForEntity(url, String[].class);

			logger.info("Gateway: " + gateway + " supported currencies: "
					+ Arrays.toString(supportedCurrenciesResponseEntity.getBody()));
			response = new SupportedCurrenciesResponseWrapperDto(true, gateway,
					Arrays.asList(supportedCurrenciesResponseEntity.getBody()));
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error("Could not get supported currencies for gateway:" + gateway);
			response = new SupportedCurrenciesResponseWrapperDto(false, gateway, new ArrayList<>());
		} 

		return response;
	}

	public Map<String, List<String>> getSupportedCardGatewaysPerCurrency() {
		Map<String, List<String>> result = new HashMap<>();
		AvailableGatewaysResponseWrapperDto availableGatewaysResponseWrapperDto = getAvailableGatewaysForCardPayments();

		if (!availableGatewaysResponseWrapperDto.isSuccessStatusCodeReceived())
			return new HashMap<>();

		availableGatewaysResponseWrapperDto.getAvailableGateways().stream().forEach(gateway -> {
			SupportedCurrenciesResponseWrapperDto supportedCurrenciesResponseWrapperDto = 
					getSupportedCurrenciesForGateay(gateway);
			
			if (supportedCurrenciesResponseWrapperDto.isSuccessStatusCodeReceived()
					&& supportedCurrenciesResponseWrapperDto.getSupportedCurrencies() != null
					&& supportedCurrenciesResponseWrapperDto.getSupportedCurrencies().size() > 0) {

				supportedCurrenciesResponseWrapperDto.getSupportedCurrencies().stream().forEach(currency -> {
					if (result.containsKey(currency)) {
						result.get(currency).add(gateway);
						return;
					}
					
					List<String> gateways = new ArrayList<>(6);
					gateways.add(gateway);
					result.put(currency, gateways);
				});
			}
		});

		return result;
	}

	public ChargeResponseWrapperDto performCharge(ChargeRequestDto chargeRequest) {
		logger.debug("Client request id for charge:" + chargeRequest.getClientRequestId());

		ChargeResponseWrapperDto response;

		String url = integrationConfig.getPaymentsApiUrl() + CHARGES;

		try {
			logger.debug("Executing HTTP POST request to: " + url + " ...");
			ResponseEntity<ChargeResponseDto> chargeResponseEntity = restTemplate.postForEntity(url, chargeRequest,
					ChargeResponseDto.class);
			
			if (chargeResponseEntity.getStatusCode() != HttpStatus.CREATED) {
				logger.debug("Received HTTP status: " + chargeResponseEntity.getStatusCodeValue() 
			       + " " + chargeResponseEntity.getStatusCode().toString());
				
				response = new ChargeResponseWrapperDto(false, null);
			} else {
				logger.debug("Received HTTP status 201 CREATED...");
				response = new ChargeResponseWrapperDto(true, chargeResponseEntity.getBody());
			}
		} catch (HttpClientErrorException httpClientErrorException) {
			response = new ChargeResponseWrapperDto(false, null);
			response.setErrorContent(httpClientErrorException.getResponseBodyAsString());

			if (httpClientErrorException.getRawStatusCode() == 502) {
				logger.debug("Received HTTP status 502 BAD_GATEWAY...");
				reverse(chargeRequest.getClientRequestId());
				logger.debug("Enqueued reversal");
			} else {
				logger.debug("Received HTTP status: " + httpClientErrorException.getRawStatusCode() 
					+ " " + httpClientErrorException.getStatusText());
			}
		}

		return response;
	}

	public EnrollmentCheckResponseWrapperDto performEnrollmentCheck(EnrollmentCheckRequestDto enrollmentCheckRequest) {
		String url = integrationConfig.getPaymentsApiUrl() + ENROLLMENT_CHECKS;
		logger.debug("Executing HTTP POST request to:" + url + " ...");
		
		try {
			ResponseEntity<EnrollmentCheckResponseDto> enrollmentCheckResponseEntity = restTemplate.postForEntity(url,
					enrollmentCheckRequest, EnrollmentCheckResponseDto.class);
			
			return new EnrollmentCheckResponseWrapperDto(true, enrollmentCheckResponseEntity.getBody());
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error("Enrollment check request failed with: "
					+ httpClientErrorException.getRawStatusCode() + " " + httpClientErrorException.getStatusText());
			logger.error(httpClientErrorException.toString());
			
			EnrollmentCheckResponseWrapperDto response = new EnrollmentCheckResponseWrapperDto(false, null); 
			response.setErrorContent(httpClientErrorException.getResponseBodyAsString());
			
			return response;
		}
	}

	public SimplePaymentsRoutingResponseWrapperDto performSimpleRouting(
			SimplePaymentsRoutingRequestDto simplePaymentsRoutingRequest) {
		SimplePaymentsRoutingResponseWrapperDto response;
		
		logger.debug("Original gateway order:"
				+ Arrays.toString(simplePaymentsRoutingRequest.getPreferredGateways().toArray()));

		String url = integrationConfig.getPaymentsApiUrl() + SIMPLE_ROUTING;

		try {
			logger.debug("Executing HTTP POST request to:" + url + " ...");
			ResponseEntity<SimplePaymentsRoutingResponseDto> simplePaymentsRoutingResponseEntity = restTemplate
					.postForEntity(url, simplePaymentsRoutingRequest, SimplePaymentsRoutingResponseDto.class);
			SimplePaymentsRoutingResponseDto body = simplePaymentsRoutingResponseEntity.getBody();
		
			logger.debug("Routed gateway order:" + Arrays.toString(body.getOrderedGateways().toArray()));
			response = new SimplePaymentsRoutingResponseWrapperDto(true, body);
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.warn("Payment routing failed with code: " + httpClientErrorException.getRawStatusCode());
			SimplePaymentsRoutingResponseDto dummyResponse = new SimplePaymentsRoutingResponseDto();
			dummyResponse.setOrderedGateways(simplePaymentsRoutingRequest.getPreferredGateways());
			response = new SimplePaymentsRoutingResponseWrapperDto(false, dummyResponse);
			response.setErrorContent(httpClientErrorException.getResponseBodyAsString());
		}

		return response;
	}

	private void reverse(String clientRequestId) {
		String url = integrationConfig.getPaymentsApiUrl() + REVERSALS;
		logger.debug("Executing HTTP POST request to: " + url + " ...");
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("clientRequestId", clientRequestId);
		
		try {
			ResponseEntity<ReversalResponseDto> reversalResponseEntity = restTemplate.postForEntity(url, null,
					ReversalResponseDto.class, uriVariables);
			logger.debug("Reversal for request: " + clientRequestId + " queued with job id:"
					+ reversalResponseEntity.getBody().getJobId());
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.warn("Failed to reverse request: " + clientRequestId);
		}
	}
}
