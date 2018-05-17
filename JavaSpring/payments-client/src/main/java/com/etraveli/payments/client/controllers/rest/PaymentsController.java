package com.etraveli.payments.client.controllers.rest;

import java.util.List;
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
import com.etraveli.payments.client.dto.integration.AuthenticationModes;
import com.etraveli.payments.client.dto.integration.ChargeRequestDto;
import com.etraveli.payments.client.dto.integration.EnrollmentCheckRequestDto;
import com.etraveli.payments.client.factories.ChargeRequestDtoFactory;
import com.etraveli.payments.client.factories.EnrollmentCheckRequestDtoFactory;
import com.etraveli.payments.client.services.PaymentsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PaymentsController {
	private final static Logger logger = LoggerFactory.getLogger(PaymentsController.class);

	@Autowired
	private final PaymentsService paymentsService;

	public PaymentsController(PaymentsService paymentsService) {
		this.paymentsService = paymentsService;
	}

	@RequestMapping(path = "/api/payments/card", method = RequestMethod.POST, 
			produces = "application/json", consumes = "application/json")
	public PaymentResponseDto createPayment(@RequestBody PaymentRequestDto paymentRequestDto, 
			HttpServletRequest request) throws JsonProcessingException {
		PaymentResponseDto paymentResponse = new PaymentResponseDto();
		
		logger.debug("Initiating card payment... ");
		String clientIp = request.getRemoteAddr();
		
		ChargeRequestDto chargeRequest = ChargeRequestDtoFactory.getChargeRequest(paymentRequestDto);
		chargeRequest.setClientIp(clientIp);
		
		EnrollmentCheckRequestDto enrollmentCheckRequest = 
				EnrollmentCheckRequestDtoFactory.getEnrollmentCheckRequest(paymentRequestDto);
		enrollmentCheckRequest.setClientIp(clientIp);

		List<String> orderedGateways = paymentRequestDto.getGateways();
		
		if (paymentRequestDto.isUse3dSecure()) {
			handle3dSecureEnrollmentCheck(orderedGateways, enrollmentCheckRequest, paymentResponse);
		} else {
			handleNon3dCharge(orderedGateways, chargeRequest, paymentResponse);
		}
		
		return paymentResponse;
	}

	private void handleNon3dCharge(List<String> orderedGateways,
			ChargeRequestDto chargeRequest, PaymentResponseDto paymentResponse) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		int totalGateways = orderedGateways.size();
		
		for(int index = 0, attempt = 1; index < totalGateways; index ++, attempt ++) {
			String gateway = orderedGateways.get(index);
			
			logger.info("Attempting to charge with " + gateway + " (attempt: " + attempt + " / " + totalGateways + ")");
			chargeRequest.setGateway(gateway);
			chargeRequest.setAuthenticationMode(AuthenticationModes.AuthenticationNotApplicable);
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
	}

	private void handle3dSecureEnrollmentCheck(List<String> orderedGateways,
			EnrollmentCheckRequestDto enrollmentCheckRequest, 
			PaymentResponseDto paymentResponse) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		int totalGateways = orderedGateways.size();
		for(int index = 0, attempt = 1; index < totalGateways; index ++, attempt ++) {
			String gateway = orderedGateways.get(index);
			
			EnrollmentCheckResponseWrapperDto enrollmentCheckResponseWrapper;
				logger.info("Requested 3D Secure. Performing enrollment check.");
				
				enrollmentCheckRequest.setGateway(gateway);
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
				
				if (enrollmentCheckResponseWrapper.isSuccessStatusCodeReceived())
					break;
		}
	}
}
