package com.etraveli.payments.client.dto;

import java.util.ArrayList;
import java.util.List;

import com.etraveli.payments.client.dto.integration.ChargeRequestDto;
import com.etraveli.payments.client.dto.integration.ChargeResponseDto;
import com.etraveli.payments.client.dto.integration.EnrollmentCheckRequestDto;

public class PaymentResponseDto {
	private boolean isPaymentSuccessful = false;
	private List<PaymentResponseActionDto> paymentActions;

	public boolean isPaymentSuccessful() {
		return isPaymentSuccessful;
	}

	public void setPaymentSuccessful(boolean isPaymentSuccessful) {
		this.isPaymentSuccessful = isPaymentSuccessful;
	}

	public List<PaymentResponseActionDto> getPaymentSteps() {
		return paymentActions;
	}
	
	public PaymentResponseDto() {
		paymentActions = new ArrayList<>();
	}
	
	public void addPaymentStep(String description, String requestPayload, 
							   String responsePayload, String outcome) {
		PaymentResponseActionDto dto = new PaymentResponseActionDto();
		
		dto.setDescription(description);
		dto.setOutcome(outcome);
		dto.setRequestPayload(requestPayload);
		dto.setResponsePayload(responsePayload);
		
		paymentActions.add(dto);
	}
}
