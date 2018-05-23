package com.etraveli.payments.client.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GatewaysCurrenciesMappingDto {
	
	private Map<String,List<String>> gatewaysPerCurrency;

	public GatewaysCurrenciesMappingDto() {
		this.gatewaysPerCurrency = new HashMap<>();
	}
	
	public GatewaysCurrenciesMappingDto(Map<String,List<String>> gatewaysPerCurrency) {
		this.gatewaysPerCurrency = gatewaysPerCurrency;
	}

	@JsonProperty("GatewaysPerCurrency")
	public Map<String, List<String>> getGatewaysPerCurrency() {
		return gatewaysPerCurrency;
	}

	public void setGatewaysPerCurrency(Map<String, List<String>> gatewaysPerCurrency) {
		this.gatewaysPerCurrency = gatewaysPerCurrency;
	}
}
