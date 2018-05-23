package com.etraveli.payments.client.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductInformationDto {
	private String productId;
	private String productType;
	
	@JsonProperty("ProductId")
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	
	@JsonProperty("ProductType")
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
}
