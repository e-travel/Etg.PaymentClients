package com.etraveli.payments.client.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocalizationSettingsDto {
	private String country;
	private String currency;
	private String brand;
	private String language;
	
	@JsonProperty("Country")
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@JsonProperty("Currency")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@JsonProperty("Language")
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	@JsonProperty("Brand")
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
}
