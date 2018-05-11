package com.etraveli.payments.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResponseWrapperDto {

	private final boolean successStatusCodeReceived;

	protected ResponseWrapperDto(boolean successStatusCodeReceived) {
		super();
		this.successStatusCodeReceived = successStatusCodeReceived;
	}

	@JsonProperty("")
	public final boolean isSuccessStatusCodeReceived() {
		return successStatusCodeReceived;
	}

}
