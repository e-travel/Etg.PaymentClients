package com.etraveli.payments.client.dto;

import com.etraveli.payments.client.dto.integration.CardTokenResponseDto;
import com.etraveli.payments.client.dto.integration.ProductMetadataDto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRequestDto {
	private int amount;
	private String brand;
	private String currency;
	private String issuerBank;
	private String language;
	private String merchantReference;
	private String orderId;
	private String country;
	private String email;
	private boolean use3dSecure;
	private CardTokenResponseDto token;
	private ProductMetadataDto productMetadata;

	@JsonProperty("amount")
	public int getAmount() {
		return amount;
	}

	@JsonProperty("brand")
	public String getBrand() {
		return brand;
	}

	@JsonProperty("currency")
	public String getCurrency() {
		return currency;
	}

	@JsonProperty("issuerBank")
	public String getIssuerBank() {
		return issuerBank;
	}

	@JsonProperty("language")
	public String getLanguage() {
		return language;
	}

	@JsonProperty("merchantReference")
	public String getMerchantReference() {
		return merchantReference;
	}

	@JsonProperty("orderId")
	public String getOrderId() {
		return orderId;
	}

	@JsonProperty("token")
	public CardTokenResponseDto getToken() {
		return token;
	}

	@JsonProperty("country")
	public String getCountry() {
		return country;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("use3dSecure")
	public boolean isUse3dSecure() {
		return use3dSecure;
	}

	@JsonProperty("productMetadata")
	public ProductMetadataDto getProductMetadata() {
		return productMetadata;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setIssuerBank(String issuerBank) {
		this.issuerBank = issuerBank;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setMerchantReference(String merchantReference) {
		this.merchantReference = merchantReference;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void setToken(CardTokenResponseDto token) {
		this.token = token;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUse3dSecure(boolean use3dSecure) {
		this.use3dSecure = use3dSecure;
	}

	public void setProductMetadata(ProductMetadataDto productMetadata) {
		this.productMetadata = productMetadata;
	}
}