package com.etraveli.payments.client.dto.integration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductMetadataDto {
	private String cardToken;
	private LocalizationSettingsDto localizationSettings;
	private List<ProductInformationDto> products;

	@JsonProperty("CardToken")
	public String getCardToken() {
		return cardToken;
	}

	@JsonProperty("LocalizationSettings")
	public LocalizationSettingsDto getLocalizationSettings() {
		return localizationSettings;
	}

	@JsonProperty("Products")
	public List<ProductInformationDto> getProducts() {
		return products;
	}

	public void setCardToken(String cardToken) {
		this.cardToken = cardToken;
	}

	public void setLocalizationSettings(LocalizationSettingsDto localizationSettings) {
		this.localizationSettings = localizationSettings;
	}

	public void setProducts(List<ProductInformationDto> products) {
		this.products = products;
	}
}
