package com.etraveli.payments.client.factories;

import com.etraveli.payments.client.dto.PaymentRequestDto;
import com.etraveli.payments.client.dto.integration.AuthenticationModes;
import com.etraveli.payments.client.dto.integration.ChargeRequestDto;

public final class ChargeRequestDtoFactory {
	public static ChargeRequestDto getChargeRequest(PaymentRequestDto paymentRequestDto) {
		ChargeRequestDto chargeRequest = new ChargeRequestDto();
		
		chargeRequest.setAmountInCents(paymentRequestDto.getAmount());
		chargeRequest.setAuthenticationMode(paymentRequestDto.getAuthenticationMode());
		chargeRequest.setCardToken(paymentRequestDto.getToken().getToken());
		chargeRequest.setCurrency(paymentRequestDto.getCurrency());
		chargeRequest.setDomain(paymentRequestDto.getBrand());
		chargeRequest.setLocalizationCountryIsoCode(paymentRequestDto.getCountry());
		chargeRequest.setLocalizationLanguageIsoCode(paymentRequestDto.getLanguage());
		chargeRequest.setMerchantReference(paymentRequestDto.getMerchantReference());
		chargeRequest.setCustomerEmail(paymentRequestDto.getEmail());
		
		return chargeRequest;
	}
}
