package com.etraveli.payments.client.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReversalResponseDto {

	private String clientRequestId;
	private String jobId;

	@JsonProperty("ClientRequestId")
	public String getClientRequestId() {
		return clientRequestId;
	}

	public void setClientRequestId(String clientRequestId) {
		this.clientRequestId = clientRequestId;
	}

	@JsonProperty("JobId")
	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}
