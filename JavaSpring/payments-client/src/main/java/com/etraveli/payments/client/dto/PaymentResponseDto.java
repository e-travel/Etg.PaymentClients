package com.etraveli.payments.client.dto;

import java.util.ArrayList;
import java.util.List;

import com.etraveli.payments.client.dto.integration.ChargeRequestDto;
import com.etraveli.payments.client.dto.integration.ChargeResponseDto;

public class PaymentResponseDto {
	private boolean isPaymentSuccessful = false;
	private ChargeRequestDto chargeRequest;
	private List<String> attemptedGateways;
	private List<ChargeResponseWrapperDto> chargeAttempts = null;
	
	public ChargeRequestDto getChargeRequest() {
		return chargeRequest;
	}

	public void setChargeRequest(ChargeRequestDto chargeRequest) {
		this.chargeRequest = chargeRequest;
	}

	public List<String> getAttemptedGateways() {
		return attemptedGateways;
	}

	public boolean isPaymentSuccessful() {
		return isPaymentSuccessful;
	}

	public void setPaymentSuccessful(boolean isPaymentSuccessful) {
		this.isPaymentSuccessful = isPaymentSuccessful;
	}
	
	public List<ChargeResponseWrapperDto> getChargeAttempts() {
		return chargeAttempts;
	}
	
	public PaymentResponseDto() {
		chargeAttempts = new ArrayList<>();
		attemptedGateways = new ArrayList<>();
	}
}
