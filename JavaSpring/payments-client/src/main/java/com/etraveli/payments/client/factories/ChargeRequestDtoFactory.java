package com.etraveli.payments.client.factories;

import com.etraveli.payments.client.dto.PaymentRequestDto;
import com.etraveli.payments.client.dto.integration.AuthenticationModes;
import com.etraveli.payments.client.dto.integration.ChargeRequestDto;

public final class ChargeRequestDtoFactory {
	public static ChargeRequestDto getChargeRequest(PaymentRequestDto paymentRequest) {
		ChargeRequestDto chargeRequest = new ChargeRequestDto();
		
		chargeRequest.setAmountInCents(paymentRequest.getAmount());
		chargeRequest.setCardToken(paymentRequest.getToken().getToken());
		chargeRequest.setCurrency(paymentRequest.getCurrency());
		chargeRequest.setDomain(paymentRequest.getBrand());
		chargeRequest.setLocalizationCountryIsoCode(paymentRequest.getCountry());
		chargeRequest.setLocalizationLanguageIsoCode(paymentRequest.getLanguage());
		chargeRequest.setMerchantReference(paymentRequest.getMerchantReference());
		chargeRequest.setCustomerEmail(paymentRequest.getEmail());
		
		return chargeRequest;
	}
}
