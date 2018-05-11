package com.etraveli.payments.client.dto.integration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimplePaymentsRoutingResponseDto {

	private List<String> orderedGateways;

	@JsonProperty("OrderedGateways")
	public List<String> getOrderedGateways() {
		return orderedGateways;
	}

	public void setOrderedGateways(List<String> orderedGateways) {
		this.orderedGateways = orderedGateways;
	}
}
