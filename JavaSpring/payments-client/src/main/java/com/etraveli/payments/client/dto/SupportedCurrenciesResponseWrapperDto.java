package com.etraveli.payments.client.dto;

import java.util.List;

public class SupportedCurrenciesResponseWrapperDto extends ResponseWrapperDto {

	private final String gateway;
	private final List<String> supportedCurrencies;

	public SupportedCurrenciesResponseWrapperDto(
			boolean successStatusCodeReceived, 
			String gateway,
			List<String> supportedCurrencies) {
		super(successStatusCodeReceived);
		this.gateway = gateway;
		this.supportedCurrencies = supportedCurrencies;
	}

	public String getGateway() {
		return gateway;
	}

	public List<String> getSupportedCurrencies() {
		return supportedCurrencies;
	}
}
