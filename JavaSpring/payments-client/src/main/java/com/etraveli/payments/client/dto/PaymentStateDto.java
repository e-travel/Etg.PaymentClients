package com.etraveli.payments.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentStateDto {
	private PaymentRequestDto paymentRequest;
	private String gateway;
	
	@JsonProperty("paymentRequest")
	public PaymentRequestDto getPaymentRequest() {
		return paymentRequest;
	}
	public void setPaymentRequest(PaymentRequestDto paymentRequest) {
		this.paymentRequest = paymentRequest;
	}

	@JsonProperty("gateway")
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
}
