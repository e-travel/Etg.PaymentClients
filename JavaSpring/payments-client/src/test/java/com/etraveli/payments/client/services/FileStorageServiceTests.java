package com.etraveli.payments.client.services;

import static org.junit.Assert.assertNotNull;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.junit4.SpringRunner;

import com.etraveli.payments.client.dto.PaymentRequestDto;
import com.etraveli.payments.client.dto.integration.CardTokenResponseDto;

@RunWith(SpringRunner.class)
public class FileStorageServiceTests {
	private FileStorageService fileStorageService;
	
	@Before
	public void setUp() {
		this.fileStorageService = new FileStorageService();
	}
	
	@After
	public void tearDown() {
		this.fileStorageService = null;
	}
	
	@Test
	public void SaveThenLoadPaymentRequestDto() {
		PaymentRequestDto paymentRequest = new PaymentRequestDto();
		CardTokenResponseDto cardToken = new CardTokenResponseDto(); 
		
		paymentRequest.setAmount(1);
		paymentRequest.setBrand("pamediakopes.gr");
		paymentRequest.setCountry("GR");
		paymentRequest.setCurrency("EUR");
		paymentRequest.setEmail("user@example.com");
		paymentRequest.setIssuerBank("Eurobank");
		paymentRequest.setLanguage("EL");
		paymentRequest.setMerchantReference("Flights_ABC123_Ticket_");
		paymentRequest.setOrderId(UUID.randomUUID().toString());
		
		cardToken.setToken(UUID.randomUUID().toString());
		paymentRequest.setToken(cardToken);
		
		String filename = UUID.randomUUID().toString() + ".json";
		fileStorageService.<PaymentRequestDto>saveData(filename, paymentRequest, PaymentRequestDto.class);
		
		PaymentRequestDto readPaymentRequest = 
			fileStorageService.<PaymentRequestDto>loadData(filename, PaymentRequestDto.class);
		
		assertNotNull(readPaymentRequest);
	}
}
