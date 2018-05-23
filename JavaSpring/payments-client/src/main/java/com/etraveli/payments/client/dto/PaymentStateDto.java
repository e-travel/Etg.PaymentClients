package com.etraveli.payments.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentStateDto {
	private PaymentRequestDto paymentRequest;
	private String gateway;
	private String transactionId;
	
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
	
	@JsonProperty("transactionId")
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
