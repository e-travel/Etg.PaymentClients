package com.etraveli.payments.client.dto;

import java.util.List;

public class AvailableGatewaysResponseWrapperDto extends ResponseWrapperDto {

	private final List<String> availableGateways;
	
	public AvailableGatewaysResponseWrapperDto(boolean successStatusCodeReceived, List<String> availableGateways) {
		super(successStatusCodeReceived);
		this.availableGateways = availableGateways;
	}

	public List<String> getAvailableGateways() {
		return availableGateways;
	}
}
