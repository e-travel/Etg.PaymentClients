package com.etraveli.payments.client.dto.integration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimplePaymentsRoutingRequestDto {
	private String bin;
	private String currency;
	private List<String> preferredGateways;
	private int transactionValue;

	@JsonProperty("Bin")
	public String getBin() {
		return this.bin;
	}

	@JsonProperty("Currency")
	public String getCurrency() {
		return this.currency;
	}

	@JsonProperty("PreferredGateways")
	public List<String> getPreferredGateways() {
		return this.preferredGateways;
	}

	@JsonProperty("TransactionValue")
	public int getTransactionValue() {
		return this.transactionValue;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setPreferredGateways(List<String> preferredGateways) {
		this.preferredGateways = preferredGateways;
	}

	public void setTransactionValue(int transactionValue) {
		this.transactionValue = transactionValue;
	}
}
