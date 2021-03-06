package com.etraveli.payments.client.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentResponseDto {
	private boolean isPaymentSuccessful = false;
	private List<PaymentResponseActionDto> paymentActions;
	private String internalPaymentIdentifier;

	public boolean isPaymentSuccessful() {
		return isPaymentSuccessful;
	}

	public void setPaymentSuccessful(boolean isPaymentSuccessful) {
		this.isPaymentSuccessful = isPaymentSuccessful;
	}

	@JsonProperty("paymentActions")
	public List<PaymentResponseActionDto> getPaymentActions() {
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

	@JsonProperty("internalPaymentIdentifier")
	public String getInternalPaymentIdentifier() {
		return internalPaymentIdentifier;
	}

	public void setInternalPaymentIdentifier(String internalPaymentIdentifier) {
		this.internalPaymentIdentifier = internalPaymentIdentifier;
	}
}
