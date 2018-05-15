(function (window, document, undefined) {
	window.factories = window.factories || {};
	window.factories.paymentRequestFactory = window.factories.paymentRequestFactory
			|| function(paymentFormData, token, products) {
				console.log("Payment Request Factory", paymentFormData, products);
		
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
					email: paymentFormData.email,
					productMetadata: {
						CardToken: token.Token,
						Products: products.map(function (obj) {
							return {
								ProductId: obj.id,
								ProductType: obj.type
							};
						}),
						LocalizationSettings: {
							Brand: paymentFormData.brand,
							Country: paymentFormData.country,
							Currency: paymentFormData.currency,
							Language: paymentFormData.language
						}
					}
				};
			}
})(window, document);