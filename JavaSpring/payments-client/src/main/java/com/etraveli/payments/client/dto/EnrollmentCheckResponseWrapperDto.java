package com.etraveli.payments.client.dto;

import com.etraveli.payments.client.dto.integration.EnrollmentCheckResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class EnrollmentCheckResponseWrapperDto extends ResponseWrapperDto {

	private final EnrollmentCheckResponseDto enrollmentCheckResponse;

	public EnrollmentCheckResponseWrapperDto(boolean responseReceived,
			EnrollmentCheckResponseDto enrollmentCheckResponse) {
		super(responseReceived);
		this.enrollmentCheckResponse = enrollmentCheckResponse;
	}

	@JsonProperty("EnrollmentCheckResponse")
	public EnrollmentCheckResponseDto getEnrollmentCheckResponse() {
		return enrollmentCheckResponse;
	}
}