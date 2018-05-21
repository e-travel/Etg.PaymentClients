package com.etraveli.payments.client.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.etraveli.payments.client.config.IntegrationConfig;
import com.etraveli.payments.client.dto.IntegrationConfigDto;

@RestController
public class ConfigController {

	private final IntegrationConfig integrationConfig;

	@Autowired
	public ConfigController(IntegrationConfig integrationConfig) {
		this.integrationConfig = integrationConfig;
	}

	@RequestMapping(path = "/api/config/etravel_integration", method = RequestMethod.GET, produces = "application/json")
	public IntegrationConfigDto getEtravelConfig() {
		return new IntegrationConfigDto(integrationConfig.getPaymentsApiUrl(),
				integrationConfig.getTokenizationApiUrl());
	}
}
