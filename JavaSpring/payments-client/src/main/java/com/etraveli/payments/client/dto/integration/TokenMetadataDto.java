package com.etraveli.payments.client.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenMetadataDto {
	private String bin;
	private String cardHolder;
	private String cardType;
	private String clearTextRepresentation;
	private String hash;
	private String issuerBank;

	@JsonProperty("Bin")
	public String getBin() {
		return this.bin;
	}

	@JsonProperty("CardHolder")
	public String getCardHolder() {
		return this.cardHolder;
	}

	@JsonProperty("CardType")
	public String getCardType() {
		return this.cardType;
	}

	@JsonProperty("ClearTextRepresentation")
	public String getClearTextRepresentation() {
		return this.clearTextRepresentation;
	}

	@JsonProperty("Hash")
	public String getHash() {
		return this.hash;
	}

	@JsonProperty("IssuerBank")
	public String getIssuerBank() {
		return this.issuerBank;
	}

	public void setBin(String bin) {
		this.bin = bin;
	}

	public void setCardHolder(String cardHolder) {
		this.cardHolder = cardHolder;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public void setClearTextRepresentation(String clearTextRepresentation) {
		this.clearTextRepresentation = clearTextRepresentation;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public void setIssuerBank(String issuerBank) {
		this.issuerBank = issuerBank;
	}
}
