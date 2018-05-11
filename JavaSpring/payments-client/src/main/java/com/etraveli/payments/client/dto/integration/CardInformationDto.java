package com.etraveli.payments.client.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardInformationDto {
	private String cvv2;
	private String expiration;
	private String holder;
	private String pan;
	private String type;

	@JsonProperty("Cvv2")
	public String getCvv2() {
		return cvv2;
	}

	@JsonProperty("Expiration")
	public String getExpiration() {
		return expiration;
	}

	@JsonProperty("Holder")
	public String getHolder() {
		return holder;
	}

	@JsonProperty("Pan")
	public String getPan() {
		return pan;
	}

	@JsonProperty("Type")
	public String getType() {
		return type;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public void setHolder(String holder) {
		this.holder = holder;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public void setType(String type) {
		this.type = type;
	}

}
