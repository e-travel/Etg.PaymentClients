package com.etraveli.payments.client.dto.integration;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChargeResponseDto {
	private String acsUri;
	private List<AirlineReservationRecordDto> airlineReservations;
	private long amountInCents;
	private String authenticationMode;
	private CardInformationDto card;
	private String clientIp;
	private String clientRequestId;
	private String currency;
	private String customerEmail;
	private List<String> descriptions;
	private String domain;
	private String eci;
	private boolean enrolled;
	private String enrollmentFlag;
	private String failure3DSecureUrl;
	private String gateway;
	private String gatewayMessage;
	private long id;
	private int legacyPaymentId;
	private String md;
	private String merchantReference;
	private String paReq;
	private String paRes;
	private boolean paymentSucceded;
	private String paymentToken;
	private String phoneCountryCode;
	private String redirectUrl;
	private String refundReference;
	private String responseCode;
	private String responseInfo;
	private String statusFlag;
	private String success3DSecureUrl;
	private String termUrl;
	private String transactionId;
	private String xid;

	@JsonProperty("AcsUri")
	public String getAcsUri() {
		return this.acsUri;
	}

	@JsonProperty("AirlineReservations")
	public List<AirlineReservationRecordDto> getAirlineReservations() {
		return this.airlineReservations;
	}

	@JsonProperty("AmountInCents")
	public long getAmountInCents() {
		return this.amountInCents;
	}

	@JsonProperty("AuthenticationMode")
	public String getAuthenticationMode() {
		return authenticationMode;
	}

	@JsonProperty("Card")
	public CardInformationDto getCard() {
		return this.card;
	}

	@JsonProperty("ClientIp")
	public String getClientIp() {
		return this.clientIp;
	}

	@JsonProperty("ClientRequestId")
	public String getClientRequestId() {
		return this.clientRequestId;
	}

	@JsonProperty("Currency")
	public String getCurrency() {
		return this.currency;
	}

	@JsonProperty("CustomerEmail")
	public String getCustomerEmail() {
		return this.customerEmail;
	}

	@JsonProperty("Descriptions")
	public List<String> getDescriptions() {
		return this.descriptions;
	}

	@JsonProperty("Domain")
	public String getDomain() {
		return this.domain;
	}

	@JsonProperty("Eci")
	public String getEci() {
		return eci;
	}

	@JsonProperty("EnrollmentFlag")
	public String getEnrollmentFlag() {
		return enrollmentFlag;
	}

	@JsonProperty("Failure3DSecureUrl")
	public String getFailure3DSecureUrl() {
		return this.failure3DSecureUrl;
	}

	@JsonProperty("Gateway")
	public String getGateway() {
		return this.gateway;
	}

	@JsonProperty("GatewayMessage")
	public String getGatewayMessage() {
		return gatewayMessage;
	}

	@JsonProperty("Id")
	public long getId() {
		return this.id;
	}

	@JsonProperty("LegacyPaymentId")
	public int getLegacyPaymentId() {
		return this.legacyPaymentId;
	}

	@JsonProperty("Md")
	public String getMd() {
		return this.md;
	}

	@JsonProperty("MerchantReference")
	public String getMerchantReference() {
		return this.merchantReference;
	}

	@JsonProperty("PaReq")
	public String getPaReq() {
		return this.paReq;
	}

	@JsonProperty("PaRes")
	public String getPaRes() {
		return paRes;
	}

	@JsonProperty("PaymentToken")
	public String getPaymentToken() {
		return this.paymentToken;
	}

	@JsonProperty("PhoneCountryCode")
	public String getPhoneCountryCode() {
		return this.phoneCountryCode;
	}

	@JsonProperty("RedirectUrl")
	public String getRedirectUrl() {
		return this.redirectUrl;
	}

	@JsonProperty("RefundReference")
	public String getRefundReference() {
		return refundReference;
	}

	@JsonProperty("ResponseCode")
	public String getResponseCode() {
		return responseCode;
	}

	@JsonProperty("ResponseInfo")
	public String getResponseInfo() {
		return responseInfo;
	}

	@JsonProperty("StatusFlag")
	public String getStatusFlag() {
		return statusFlag;
	}

	@JsonProperty("Success3DSecureUrl")
	public String getSuccess3DSecureUrl() {
		return this.success3DSecureUrl;
	}

	@JsonProperty("TermUrl")
	public String getTermUrl() {
		return this.termUrl;
	}

	@JsonProperty("TransactionId")
	public String getTransactionId() {
		return this.transactionId;
	}

	@JsonProperty("Xid")
	public String getXid() {
		return xid;
	}

	@JsonProperty("IsEnrolled")
	public boolean isEnrolled() {
		return this.enrolled;
	}

	@JsonProperty("PaymentSucceded")
	public boolean isPaymentSucceded() {
		return paymentSucceded;
	}

	public void setAcsUri(String AcsUri) {
		this.acsUri = AcsUri;
	}

	public void setAirlineReservations(List<AirlineReservationRecordDto> AirlineReservations) {
		this.airlineReservations = AirlineReservations;
	}

	public void setAmountInCents(long AmountInCents) {
		this.amountInCents = AmountInCents;
	}

	public void setAuthenticationMode(String authenticationMode) {
		this.authenticationMode = authenticationMode;
	}

	public void setCard(CardInformationDto Card) {
		this.card = Card;
	}

	public void setClientIp(String ClientIp) {
		this.clientIp = ClientIp;
	}

	public void setClientRequestId(String ClientRequestId) {
		this.clientRequestId = ClientRequestId;
	}

	public void setCurrency(String Currency) {
		this.currency = Currency;
	}

	public void setCustomerEmail(String CustomerEmail) {
		this.customerEmail = CustomerEmail;
	}

	public void setDescriptions(List<String> Descriptions) {
		this.descriptions = Descriptions;
	}

	public void setDomain(String Domain) {
		this.domain = Domain;
	}

	public void setEci(String eci) {
		this.eci = eci;
	}

	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}

	public void setEnrollmentFlag(String enrollmentFlag) {
		this.enrollmentFlag = enrollmentFlag;
	}

	public void setFailure3DSecureUrl(String Failure3DSecureUrl) {
		this.failure3DSecureUrl = Failure3DSecureUrl;
	}

	public void setGateway(String Gateway) {
		this.gateway = Gateway;
	}

	public void setGatewayMessage(String gatewayMessage) {
		this.gatewayMessage = gatewayMessage;
	}

	public void setId(long Id) {
		this.id = Id;
	}

	public void setLegacyPaymentId(int LegacyPaymentId) {
		this.legacyPaymentId = LegacyPaymentId;
	}

	public void setMd(String Md) {
		this.md = Md;
	}

	public void setMerchantReference(String MerchantReference) {
		this.merchantReference = MerchantReference;
	}

	public void setPaReq(String PaReq) {
		this.paReq = PaReq;
	}

	public void setPaRes(String paRes) {
		this.paRes = paRes;
	}

	public void setPaymentSucceded(boolean paymentSucceded) {
		this.paymentSucceded = paymentSucceded;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}

	public void setPhoneCountryCode(String PhoneCountryCode) {
		this.phoneCountryCode = PhoneCountryCode;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public void setRefundReference(String refundReference) {
		this.refundReference = refundReference;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public void setResponseInfo(String responseInfo) {
		this.responseInfo = responseInfo;
	}

	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}

	public void setSuccess3DSecureUrl(String Success3DSecureUrl) {
		this.success3DSecureUrl = Success3DSecureUrl;
	}

	public void setTermUrl(String TermUrl) {
		this.termUrl = TermUrl;
	}

	public void setTransactionId(String TransactionId) {
		this.transactionId = TransactionId;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}
}
