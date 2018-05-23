package com.etraveli.payments.client.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.etraveli.payments.client.dto.GatewaysCurrenciesMappingDto;
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
	private static final String EXECUTING_HTTP_POST_REQUEST_TO = "Executing HTTP POST request to: {} ...";

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

			logger.debug("Executing HTTP GET request to: {}", url);
			ResponseEntity<String[]> availableGatewaysResponseEntity = restTemplate.getForEntity(url, String[].class);

			String availableGatewaysAsString = Arrays.toString(availableGatewaysResponseEntity.getBody());
			logger.info("Available gateways: {}", availableGatewaysAsString);

			response = new AvailableGatewaysResponseWrapperDto(true,
					Arrays.asList(availableGatewaysResponseEntity.getBody()));

		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error("Could not get available gateways: {}", httpClientErrorException.getMessage());
			response = new AvailableGatewaysResponseWrapperDto(false, new ArrayList<>());
		}

		return response;
	}

	public SupportedCurrenciesResponseWrapperDto getSupportedCurrenciesForGateay(String gateway) {
		SupportedCurrenciesResponseWrapperDto response;

		String url = integrationConfig.getPaymentsApiUrl() + AVAILABLE_GATEWAYS + "/" + gateway + "/currencies";

		try {
			logger.debug("Executing HTTP GET request to: {}", url);
			ResponseEntity<String[]> supportedCurrenciesResponseEntity = restTemplate.getForEntity(url, String[].class);

			String supportedCurrencies = Arrays.toString(supportedCurrenciesResponseEntity.getBody());
			logger.info("Gateway: {}, supported currencies: {}", gateway, supportedCurrencies);

			response = new SupportedCurrenciesResponseWrapperDto(true, gateway,
					Arrays.asList(supportedCurrenciesResponseEntity.getBody()));
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error("Could not get supported currencies for gateway: {}. Reason: {}", gateway,
					httpClientErrorException.getMessage());
			logger.error(httpClientErrorException.getMessage(), httpClientErrorException);
			response = new SupportedCurrenciesResponseWrapperDto(false, gateway, new ArrayList<>());
		}

		return response;
	}

	public GatewaysCurrenciesMappingDto getSupportedCardGatewaysPerCurrency() {
		Map<String, List<String>> gatewaysPerCurrencyMapping = new HashMap<>();
		AvailableGatewaysResponseWrapperDto availableGatewaysResponseWrapperDto = getAvailableGatewaysForCardPayments();

		if (!availableGatewaysResponseWrapperDto.isSuccessStatusCodeReceived())
			return new GatewaysCurrenciesMappingDto();

		availableGatewaysResponseWrapperDto.getAvailableGateways().stream().forEach(gateway -> {
			SupportedCurrenciesResponseWrapperDto supportedCurrenciesResponseWrapperDto = getSupportedCurrenciesForGateay(
					gateway);

			if (supportedCurrenciesResponseWrapperDto.isSuccessStatusCodeReceived()
					&& supportedCurrenciesResponseWrapperDto.getSupportedCurrencies() != null
					&& !supportedCurrenciesResponseWrapperDto.getSupportedCurrencies().isEmpty()) {

				supportedCurrenciesResponseWrapperDto.getSupportedCurrencies().stream().forEach(currency -> {
					if (gatewaysPerCurrencyMapping.containsKey(currency)) {
						gatewaysPerCurrencyMapping.get(currency).add(gateway);
						return;
					}

					List<String> gateways = new ArrayList<>(6);
					gateways.add(gateway);
					gatewaysPerCurrencyMapping.put(currency, gateways);
				});
			}
		});

		return new GatewaysCurrenciesMappingDto(gatewaysPerCurrencyMapping);
	}

	public ChargeResponseWrapperDto performCharge(ChargeRequestDto chargeRequest) {
		logger.debug("Client request id for charge: {}", chargeRequest.getClientRequestId());

		ChargeResponseWrapperDto response;

		String url = integrationConfig.getPaymentsApiUrl() + CHARGES;

		try {
			logger.debug(EXECUTING_HTTP_POST_REQUEST_TO, url);
			ResponseEntity<ChargeResponseDto> chargeResponseEntity = restTemplate.postForEntity(url, chargeRequest,
					ChargeResponseDto.class);

			logger.debug("Received HTTP status: {} {}", chargeResponseEntity.getStatusCodeValue(),
					chargeResponseEntity.getStatusCode());

			if (chargeResponseEntity.getStatusCode() != HttpStatus.CREATED)
				response = new ChargeResponseWrapperDto(false, null);
			else
				response = new ChargeResponseWrapperDto(true, chargeResponseEntity.getBody());

		} catch (HttpClientErrorException httpClientErrorException) {
			response = new ChargeResponseWrapperDto(false, null);
			response.setErrorContent(httpClientErrorException.getResponseBodyAsString());

			logger.debug("Received HTTP status: {} {} ", httpClientErrorException.getRawStatusCode(),
					httpClientErrorException.getStatusText());

			if (httpClientErrorException.getRawStatusCode() == 502)
				reverse(chargeRequest.getClientRequestId());
		}

		return response;
	}

	public EnrollmentCheckResponseWrapperDto performEnrollmentCheck(EnrollmentCheckRequestDto enrollmentCheckRequest) {
		String url = integrationConfig.getPaymentsApiUrl() + ENROLLMENT_CHECKS;
		logger.debug(EXECUTING_HTTP_POST_REQUEST_TO, url);

		try {
			ResponseEntity<EnrollmentCheckResponseDto> enrollmentCheckResponseEntity = restTemplate.postForEntity(url,
					enrollmentCheckRequest, EnrollmentCheckResponseDto.class);

			return new EnrollmentCheckResponseWrapperDto(true, enrollmentCheckResponseEntity.getBody());
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.error("Enrollment check request failed with: " + httpClientErrorException.getRawStatusCode() + " "
					+ httpClientErrorException.getStatusText());
			logger.error(httpClientErrorException.toString());

			EnrollmentCheckResponseWrapperDto response = new EnrollmentCheckResponseWrapperDto(false, null);
			response.setErrorContent(httpClientErrorException.getResponseBodyAsString());

			return response;
		}
	}

	public SimplePaymentsRoutingResponseWrapperDto performSimpleRouting(
			SimplePaymentsRoutingRequestDto simplePaymentsRoutingRequest) {
		SimplePaymentsRoutingResponseWrapperDto response;

		String orderedGatewaysAsString = Arrays.toString(simplePaymentsRoutingRequest.getPreferredGateways().toArray());
		logger.debug("Original gateway order: {}", orderedGatewaysAsString);

		String url = integrationConfig.getPaymentsApiUrl() + SIMPLE_ROUTING;

		try {
			logger.debug(EXECUTING_HTTP_POST_REQUEST_TO, url);
			ResponseEntity<SimplePaymentsRoutingResponseDto> simplePaymentsRoutingResponseEntity = restTemplate
					.postForEntity(url, simplePaymentsRoutingRequest, SimplePaymentsRoutingResponseDto.class);
			SimplePaymentsRoutingResponseDto body = simplePaymentsRoutingResponseEntity.getBody();

			String orderedGatwaysAsString = Arrays.toString(body.getOrderedGateways().toArray());
			logger.debug("Routed gateway order: {}", orderedGatwaysAsString);
			response = new SimplePaymentsRoutingResponseWrapperDto(true, body);
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.warn("Payment routing failed with code: {}", httpClientErrorException.getRawStatusCode());
			SimplePaymentsRoutingResponseDto dummyResponse = new SimplePaymentsRoutingResponseDto();
			dummyResponse.setOrderedGateways(simplePaymentsRoutingRequest.getPreferredGateways());
			response = new SimplePaymentsRoutingResponseWrapperDto(false, dummyResponse);
			response.setErrorContent(httpClientErrorException.getResponseBodyAsString());
		}

		return response;
	}

	private void reverse(String clientRequestId) {
		String url = integrationConfig.getPaymentsApiUrl() + REVERSALS;
		logger.debug(EXECUTING_HTTP_POST_REQUEST_TO, url);
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("clientRequestId", clientRequestId);

		try {
			ResponseEntity<ReversalResponseDto> reversalResponseEntity = restTemplate.postForEntity(url, null,
					ReversalResponseDto.class, uriVariables);
			logger.debug("Reversal for request: {} queued with job id: {}", clientRequestId,
					reversalResponseEntity.getBody().getJobId());
		} catch (HttpClientErrorException httpClientErrorException) {
			logger.warn("Failed to reverse request: {}", clientRequestId);
		}
	}
}
