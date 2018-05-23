package com.etraveli.payments.client.dto;

import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class SimplePaymentsRoutingResponseWrapperDto extends ResponseWrapperDto {

	private final SimplePaymentsRoutingResponseDto simplePaymentsRoutingResponseDto;

	public SimplePaymentsRoutingResponseWrapperDto(boolean successStatusCodeReceived,
			SimplePaymentsRoutingResponseDto simplePaymentsRoutingResponseDto) {
		super(successStatusCodeReceived);
		this.simplePaymentsRoutingResponseDto = simplePaymentsRoutingResponseDto;
	}

	@JsonProperty("simplePaymentsRoutingResponseDto")
	public SimplePaymentsRoutingResponseDto getSimplePaymentsRoutingResponseDto() {
		return simplePaymentsRoutingResponseDto;
	}

}
