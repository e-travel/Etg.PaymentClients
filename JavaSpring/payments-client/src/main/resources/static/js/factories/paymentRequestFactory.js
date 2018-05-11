(function (window, document, undefined) {
	window.factories = window.factories || {};
	window.factories.paymentRequestFactory = window.factories.paymentRequestFactory
			|| function(paymentFormData, token) {
				console.log("Payment Request Factory", paymentFormData);
		
				return {
					token: token,
					amount: paymentFormData.amount,
					brand: paymentFormData.brand,
					country: paymentFormData.country,
					currency: paymentFormData.currency,
					issuerBank: paymentFormData.issuer_bank,
					language: paymentFormData.language,
					merchantReference: paymentFormData.merchantref,
					orderId: paymentFormData.orderid,
					email: paymentFormData.email
				};
			}
})(window, document);