package com.etraveli.payments.client.factories;

import com.etraveli.payments.client.dto.PaymentRequestDto;
import com.etraveli.payments.client.dto.integration.EnrollmentCheckRequestDto;

public final class EnrollmentCheckRequestDtoFactory {
	public static EnrollmentCheckRequestDto getEnrollmentCheckRequest(PaymentRequestDto paymentRequest) {
		EnrollmentCheckRequestDto enrollmentCheckRequest = new EnrollmentCheckRequestDto();
		
		enrollmentCheckRequest.setAmountInCents(paymentRequest.getAmount());
		enrollmentCheckRequest.setCardToken(paymentRequest.getToken().getToken());
		enrollmentCheckRequest.setCurrency(paymentRequest.getCurrency());
		enrollmentCheckRequest.setCustomerEmail(paymentRequest.getEmail());
		enrollmentCheckRequest.setDomain(paymentRequest.getBrand());
		enrollmentCheckRequest.setLocalizationCountryIsoCode(paymentRequest.getCountry());
		enrollmentCheckRequest.setLocalizationLanguageIsoCode(paymentRequest.getLanguage());
		enrollmentCheckRequest.setMerchantReference(paymentRequest.getMerchantReference());
		enrollmentCheckRequest.setProductMetadata(paymentRequest.getProductMetadata());
		
		return enrollmentCheckRequest;
	}
}
