package com.etraveli.payments.client.dto.integration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This card represents an /credit_card_payments/enrollment_checks request
 * payload. It has been designed to support JSON serialization.
 * 
 * @author g.gkogkolis
 *
 */
public class EnrollmentCheckRequestDto {

	private List<AirlineReservationRecordDto> airlineReservations;
	private long amountInCents;
	private String cardToken;
	private String clientIp;
	private String clientRequestId;
	private String currency;
	private String customerEmail;
	private List<String> descriptions;
	private String domain;
	private String failure3DSecureUrl;
	private String gateway;
	private String localizationCountryIsoCode;
	private String localizationLanguageIsoCode;
	private String merchantReference;
	private String phoneCountryCode;
	private ProductMetadataDto productMetadata;
	private String success3DSecureUrl;

	@JsonProperty("AirlineReservations")
	public List<AirlineReservationRecordDto> getAirlineReservations() {
		return airlineReservations;
	}

	@JsonProperty("AmountInCents")
	public long getAmountInCents() {
		return amountInCents;
	}

	@JsonProperty("CardToken")
	public String getCardToken() {
		return cardToken;
	}

	@JsonProperty("ClientIp")
	public String getClientIp() {
		return clientIp;
	}

	@JsonProperty("ClientRequestId")
	public String getClientRequestId() {
		return clientRequestId;
	}

	@JsonProperty("Currency")
	public String getCurrency() {
		return currency;
	}

	@JsonProperty("CustomerEmail")
	public String getCustomerEmail() {
		return customerEmail;
	}

	@JsonProperty("Descriptions")
	public List<String> getDescriptions() {
		return descriptions;
	}

	@JsonProperty("Domain")
	public String getDomain() {
		return domain;
	}

	@JsonProperty("Failure3DSecureUrl")
	public String getFailure3DSecureUrl() {
		return failure3DSecureUrl;
	}

	@JsonProperty("Gateway")
	public String getGateway() {
		return gateway;
	}

	@JsonProperty("LocalizationCountryIsoCode")
	public String getLocalizationCountryIsoCode() {
		return localizationCountryIsoCode;
	}

	@JsonProperty("LocalizationLanguageIsoCode")
	public String getLocalizationLanguageIsoCode() {
		return localizationLanguageIsoCode;
	}

	@JsonProperty("MerchantReference")
	public String getMerchantReference() {
		return merchantReference;
	}

	@JsonProperty("PhoneCountryCode")
	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}

	@JsonProperty("ProductMetadata")
	public ProductMetadataDto getProductMetadata() {
		return productMetadata;
	}

	@JsonProperty("Success3DSecureUrl")
	public String getSuccess3DSecureUrl() {
		return success3DSecureUrl;
	}

	public void setAirlineReservations(List<AirlineReservationRecordDto> airlineReservations) {
		this.airlineReservations = airlineReservations;
	}

	public void setAmountInCents(long amountInCents) {
		this.amountInCents = amountInCents;
	}

	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public void setClientRequestId(String clientRequestId) {
		this.clientRequestId = clientRequestId;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setFailure3DSecureUrl(String failure3dSecureUrl) {
		failure3DSecureUrl = failure3dSecureUrl;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public void setLocalizationCountryIsoCode(String localizationCountryIsoCode) {
		this.localizationCountryIsoCode = localizationCountryIsoCode;
	}

	public void setLocalizationLanguageIsoCode(String localizationLanguageIsoCode) {
		this.localizationLanguageIsoCode = localizationLanguageIsoCode;
	}

	public void setMerchantReference(String merchantReference) {
		this.merchantReference = merchantReference;
	}

	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}

	public void setProductMetadata(ProductMetadataDto productMetadata) {
		this.productMetadata = productMetadata;
	}

	public void setSuccess3DSecureUrl(String success3dSecureUrl) {
		success3DSecureUrl = success3dSecureUrl;
	}
}
