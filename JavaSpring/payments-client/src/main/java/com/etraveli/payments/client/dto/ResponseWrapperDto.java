package com.etraveli.payments.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResponseWrapperDto {

	private final boolean successStatusCodeReceived;
	protected String errorContent;

	protected ResponseWrapperDto(boolean successStatusCodeReceived) {
		super();
		this.successStatusCodeReceived = successStatusCodeReceived;
	}

	@JsonProperty("")
	public final boolean isSuccessStatusCodeReceived() {
		return successStatusCodeReceived;
	}

	@JsonProperty("errorContent")
	public String getErrorContent() {
		return errorContent;
	}

	public void setErrorContent(String errorContent) {
		this.errorContent = errorContent;
	}

}
