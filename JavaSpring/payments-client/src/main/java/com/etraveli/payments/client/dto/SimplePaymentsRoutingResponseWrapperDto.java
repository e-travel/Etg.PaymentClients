package com.etraveli.payments.client.dto;

import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingResponseDto;

public final class SimplePaymentsRoutingResponseWrapperDto extends ResponseWrapperDto {

	private final SimplePaymentsRoutingResponseDto simplePaymentsRoutingResponseDto;

	public SimplePaymentsRoutingResponseWrapperDto(boolean successStatusCodeReceived,
			SimplePaymentsRoutingResponseDto simplePaymentsRoutingResponseDto) {
		super(successStatusCodeReceived);
		this.simplePaymentsRoutingResponseDto = simplePaymentsRoutingResponseDto;
	}

	public SimplePaymentsRoutingResponseDto getSimplePaymentsRoutingResponseDto() {
		return simplePaymentsRoutingResponseDto;
	}

}
