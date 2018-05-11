package com.etraveli.payments.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntegrationConfigDto {
	private final String paymentsApiUrl;
	private final String tokenizationApiUrl;

	public IntegrationConfigDto(String paymentsApiUrl, String tokenizationApiUrl) {
		this.paymentsApiUrl = paymentsApiUrl;
		this.tokenizationApiUrl = tokenizationApiUrl;
	}

	@JsonProperty("paymentsApiUrl")
	public String getPaymentsApiUrl() {
		return paymentsApiUrl;
	}

	@JsonProperty("tokenizationApiUrl")
	public String getTokenizationApiUrl() {
		return tokenizationApiUrl;
	}
}
