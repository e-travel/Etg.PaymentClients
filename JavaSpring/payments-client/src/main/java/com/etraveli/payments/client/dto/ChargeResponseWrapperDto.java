package com.etraveli.payments.client.dto;

import com.etraveli.payments.client.dto.integration.ChargeResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class ChargeResponseWrapperDto extends ResponseWrapperDto {

	private final ChargeResponseDto chargeResponse;
	public ChargeResponseWrapperDto(boolean successStatusCodeReceived, ChargeResponseDto chargeResponse) {
		super(successStatusCodeReceived);
		this.chargeResponse = chargeResponse;
	}

	@JsonProperty("chargeResponse")
	public ChargeResponseDto getChargeResponse() {
		return chargeResponse;
	}
}
