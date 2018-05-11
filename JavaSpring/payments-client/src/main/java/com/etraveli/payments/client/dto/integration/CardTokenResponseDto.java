package com.etraveli.payments.client.dto.integration;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardTokenResponseDto {
	private Date createdAt;
	private TokenMetadataDto metadata;
	private String token;

	@JsonProperty("CreatedAt")
	public Date getCreatedAt() {
		return this.createdAt;
	}

	@JsonProperty("Metadata")
	public TokenMetadataDto getMetadata() {
		return this.metadata;
	}

	@JsonProperty("Token")
	public String getToken() {
		return this.token;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setMetadata(TokenMetadataDto metadata) {
		this.metadata = metadata;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
