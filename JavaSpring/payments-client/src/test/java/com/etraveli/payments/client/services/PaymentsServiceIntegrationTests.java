package com.etraveli.payments.client.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.apache.tomcat.util.buf.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.etraveli.payments.client.config.IntegrationConfig;
import com.etraveli.payments.client.dto.GatewaysCurrenciesMappingDto;
import com.etraveli.payments.client.dto.SimplePaymentsRoutingResponseWrapperDto;
import com.etraveli.payments.client.dto.integration.SimplePaymentsRoutingRequestDto;

@RunWith(SpringRunner.class)
public class PaymentsServiceIntegrationTests {

	private PaymentsService paymentsService;

	@Before
	public void setUp() {
		IntegrationConfig integrationConfig = new IntegrationConfig();
		integrationConfig.setPaymentsApiUrl("http://localhost/payments-v1");
		integrationConfig.setTokenizationApiUrl("http://tokenization-v2.staging.api.e-travel.gr");

		this.paymentsService = new PaymentsService(new RestTemplate(), integrationConfig);
	}

	@After
	public void tearDown() {
		this.paymentsService = null;
	}

	@Test
	public void testLoads() {
		assertNotNull(paymentsService);
	}

	@Test
	public void testLoadGatewaysPerCurrencyMap() {
		GatewaysCurrenciesMappingDto gatewaysPerCurrency = paymentsService.getSupportedCardGatewaysPerCurrency();

		assertNotNull(gatewaysPerCurrency);
		assertNotNull(gatewaysPerCurrency.getGatewaysPerCurrency());
		assertFalse(gatewaysPerCurrency.getGatewaysPerCurrency().isEmpty());
	}

	@Test
	public void testSimplePaymentsRouting() {
		GatewaysCurrenciesMappingDto gatewaysPerCurrency = paymentsService.getSupportedCardGatewaysPerCurrency();

		List<String> availableGateways = gatewaysPerCurrency.getGatewaysPerCurrency().get("EUR");
		System.out.println(StringUtils.join(availableGateways, ','));

		assertNotNull(availableGateways);

		SimplePaymentsRoutingRequestDto simplePaymentsRoutingRequest = new SimplePaymentsRoutingRequestDto();
		simplePaymentsRoutingRequest.setBin("516732");
		simplePaymentsRoutingRequest.setCurrency("EUR");
		simplePaymentsRoutingRequest.setPreferredGateways(availableGateways);
		simplePaymentsRoutingRequest.setTransactionValue(1);

		SimplePaymentsRoutingResponseWrapperDto simplePaymentsRoutingResponseDto = paymentsService
				.performSimpleRouting(simplePaymentsRoutingRequest);

		assertNotNull(simplePaymentsRoutingResponseDto);
		assertThat(simplePaymentsRoutingResponseDto.isSuccessStatusCodeReceived() == true);

		System.out.println(StringUtils.join(
				simplePaymentsRoutingResponseDto.getSimplePaymentsRoutingResponseDto().getOrderedGateways(), ','));
	}

}
