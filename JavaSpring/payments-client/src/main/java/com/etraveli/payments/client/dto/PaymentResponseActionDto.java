package com.etraveli.payments.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentResponseActionDto {
	private String description;
	private String requestPayload;
	private String responsePayload;
	private String outcome;
	
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonProperty("requestPayload")
	public String getRequestPayload() {
		return requestPayload;
	}
	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}
	
	@JsonProperty("responsePayload")
	public String getResponsePayload() {
		return responsePayload;
	}
	public void setResponsePayload(String responsePayload) {
		this.responsePayload = responsePayload;
	}
	
	@JsonProperty("outcome")
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	
}
