package com.etraveli.payments.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "etravel.integration")
public class IntegrationConfig {

	private String paymentsApiUrl;
	private String tokenizationApiUrl;

	public String getPaymentsApiUrl() {
		return paymentsApiUrl;
	}

	public String getTokenizationApiUrl() {
		return tokenizationApiUrl;
	}

	public void setPaymentsApiUrl(String paymentsApiUrl) {
		this.paymentsApiUrl = paymentsApiUrl;
	}

	public void setTokenizationApiUrl(String tokenizationApiUrl) {
		this.tokenizationApiUrl = tokenizationApiUrl;
	}
}
