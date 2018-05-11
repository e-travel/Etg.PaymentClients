package com.etraveli.payments.client.dto;

import com.etraveli.payments.client.dto.integration.ChargeResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ChargeResponseWrapperDto extends ResponseWrapperDto {

	private final ChargeResponseDto chargeResponse;
	private String errorContent;

	public ChargeResponseWrapperDto(boolean successStatusCodeReceived, ChargeResponseDto chargeResponse) {
		super(successStatusCodeReceived);
		this.chargeResponse = chargeResponse;
	}

	@JsonProperty("chargeResponse")
	public ChargeResponseDto getChargeResponse() {
		return chargeResponse;
	}

	@JsonProperty("errorContent")
	public String getErrorContent() {
		return errorContent;
	}

	public void setErrorContent(String errorContent) {
		this.errorContent = errorContent;
	}
	
	
}
