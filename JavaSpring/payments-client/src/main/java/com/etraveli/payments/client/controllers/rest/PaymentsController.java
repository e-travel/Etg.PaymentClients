package com.etraveli.payments.client.controllers.rest;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etraveli.payments.client.dto.ChargeResponseWrapperDto;
import com.etraveli.payments.client.dto.EnrollmentCheckResponseWrapperDto;
import com.etraveli.payments.client.dto.PaymentRequestDto;
import com.etraveli.payments.client.dto.PaymentResponseDto;
import com.etraveli.payments.client.dto.SimplePaymentsRoutingResponseWrapperDto;
import com.etraveli.payments.client.dto.integration.AuthenticationModes;
import com.etraveli.payments.client.dto.integration.ChargeRequestDto;
import com.etraveli.payments.client.dto.integration.EnrollmentCheckRequestDto;
import com.etraveli.payments.client.dto.integration.EnrollmentCheckResponseDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingRequestDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingResponseDto;
import com.etraveli.payments.client.factories.ChargeRequestDtoFactory;
import com.etraveli.payments.client.factories.EnrollmentCheckRequestDtoFactory;
import com.etraveli.payments.client.services.PaymentsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	public PaymentResponseDto createPayment(@RequestBody PaymentRequestDto paymentRequestDto, HttpServletRequest request) throws JsonProcessingException {
		PaymentResponseDto paymentResponse = new PaymentResponseDto();
		ObjectMapper mapper = new ObjectMapper();

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
		
		SimplePaymentsRoutingResponseWrapperDto simplePaymentsRoutingResponseWrapper = paymentsService
				.performSimpleRouting(simplePaymentsRoutingRequest);
		
		paymentResponse.addPaymentStep("Payment routing", 
			mapper.writeValueAsString(simplePaymentsRoutingRequest), 
			mapper.writeValueAsString(
					simplePaymentsRoutingResponseWrapper.isSuccessStatusCodeReceived()
						? simplePaymentsRoutingResponseWrapper.getSimplePaymentsRoutingResponseDto()
						: simplePaymentsRoutingResponseWrapper.getErrorContent()), 
			simplePaymentsRoutingResponseWrapper.isSuccessStatusCodeReceived() 
				? "Success" : "Failure");
		
		String clientIp = request.getRemoteAddr();
		
		ChargeRequestDto chargeRequest = ChargeRequestDtoFactory.getChargeRequest(paymentRequestDto);
		chargeRequest.setClientIp(clientIp);
		
		EnrollmentCheckRequestDto enrollmentCheckRequest = 
				EnrollmentCheckRequestDtoFactory.getEnrollmentCheckRequest(paymentRequestDto);
		enrollmentCheckRequest.setClientIp(clientIp);

		List<String> orderedGateways = 
				simplePaymentsRoutingResponseWrapper.getSimplePaymentsRoutingResponseDto().getOrderedGateways();
		
		int totalGateways = orderedGateways.size();
		for(int index = 0, attempt = 1; index < totalGateways; index ++, attempt ++) {
			String gateway = orderedGateways.get(index);
			
			EnrollmentCheckResponseWrapperDto enrollmentCheckResponseWrapper;
			if (!paymentRequestDto.getAuthenticationMode().equals(AuthenticationModes.AuthenticationNotApplicable)) {
				logger.info("Requested card authentication mode: " + paymentRequestDto.getAuthenticationMode()
					+ ", performing enrollment check.");
				
				enrollmentCheckRequest.setClientRequestId(UUID.randomUUID().toString());
				enrollmentCheckResponseWrapper = paymentsService.performEnrollmentCheck(enrollmentCheckRequest);
				
				paymentResponse.addPaymentStep("Enrollment check for payment attempt: " + attempt, 
					mapper.writeValueAsString(enrollmentCheckRequest), 
					mapper.writeValueAsString(
							enrollmentCheckResponseWrapper.isSuccessStatusCodeReceived()
								? enrollmentCheckResponseWrapper.getEnrollmentCheckResponse()
								: enrollmentCheckResponseWrapper.getErrorContent()), 
					enrollmentCheckResponseWrapper.isSuccessStatusCodeReceived() 
						? "Success" : "Failure");
				
				if (paymentRequestDto.getAuthenticationMode() == AuthenticationModes.AuthenticationRequired && 
						enrollmentCheckResponseWrapper.getEnrollmentCheckResponse().isEnrolled() == false) {
					logger.warn("Cannot continue; 3D secure is required while the customer is not enrolled.");
					return paymentResponse;
				}
			}
			
			logger.info("Attempting to charge with " + gateway + " (attempt: " + attempt + " / " + totalGateways + ")");
			chargeRequest.setGateway(gateway);
			chargeRequest.setClientRequestId(UUID.randomUUID().toString());
			
			ChargeResponseWrapperDto chargeResponseWrapper = paymentsService.performCharge(chargeRequest);
			
			paymentResponse.addPaymentStep("Charge for payment attempt: " + attempt, 
				mapper.writeValueAsString(chargeRequest), 
				mapper.writeValueAsString(
						chargeResponseWrapper.isSuccessStatusCodeReceived()
							? chargeResponseWrapper.getChargeResponse()
							: chargeResponseWrapper.getErrorContent()), 
				chargeResponseWrapper.isSuccessStatusCodeReceived() 
					? "Success" : "Failure");
			
			if (chargeResponseWrapper.getChargeResponse().isPaymentSucceded()) {
				paymentResponse.setPaymentSuccessful(true);
				break;
			}
		}
		
		return paymentResponse;
	}
}
