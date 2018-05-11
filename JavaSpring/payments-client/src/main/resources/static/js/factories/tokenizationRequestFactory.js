(function (window, document, undefined) {
	window.factories = window.factories || {};
	window.factories.tokenizationRequestFactory = window.factories.tokenizationRequestFactory
			|| function(paymentFormData) {
				return {
					Card : {
						CardType : paymentFormData["cctype"],
						CardNumber : paymentFormData["pan"],
						CardHolder : paymentFormData["cardholder"],
						CCV : paymentFormData["cvv2"],
						ExpirationMonth : paymentFormData["expiration_month"],
						ExpirationYear : paymentFormData["expiration_year"],
						IssuerBank : paymentFormData["issuer_bank"]
					}
				};
			}
})(window, document);