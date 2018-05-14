(function(window, document, $, undefined) {
	$(function() {

		var page = {
			initialize : function() {
				$(".card-content:not(#card1)").hide();
				$(".card-link[data-card=card4]").hide();
				this.addEventListeners();
			},

			addEventListeners : function() {
				$(".card-link").on("click", this.onCardLinkClick);
				$("#payments_creation_form").on("submit",
						this.onPaymentFormSubmit);
				$("#generate_fake_data").on("click",
						this.onGenerateFakeDataClick);
				$("#proceed_to_payment").on("click",
						this.onProceedToPaymentClick);
				$("#authenticationMode").on("change",
						this.onAuthenticationModeChange);
			},
			
			onAuthenticationModeChange: function (evt) {
				var $this = $(this);
				
				if ($this.val() == "AuthenticationNotApplicable") {
					$(".card-link[data-card=card4]").hide();
				} else {
					$(".card-link[data-card=card4]").show();
				}
			},

			onGenerateFakeDataClick: function(evt) {
				var $form = $("#payments_creation_form");

				$("#merchantref").val(
						"Flights_"
								+ faker.internet.color().substr(1)
										.toUpperCase() + "_Ticket_");

				$("#orderid").val(UUIDv4Generator.generate());

				var brands = [ "pamediakopes.gr", "avion.ro", "mytrip.com",
						"trip.ru" ];

				$("#brand").val(brands[faker.random.number(3)]);
				$("#country").val(faker.address.countryCode());
				$("#language").val(faker.address.countryCode());
				$("#amount").val(10000 + faker.random.number(10000));

				var currencies = [ "AED", "ALL", "AUD", "BGN", "BRL", "CAD",
						"CHF", "CNY", "CZK", "DKK", "EGP", "EUR", "GBP", "HKD",
						"HRK", "HUF", "IDR", "ILS", "INR", "JPY", "KRW", "KWD",
						"KZT", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN", "RON",
						"RSD", "RUB", "SAR", "SEK", "SGD", "THB", "TRY", "TWD",
						"UAH", "USD", "ZAR" ];

				$("#currency").val(
						currencies[faker.random.number(currencies.length - 1)]);

				$("#cardholder").val(
						(faker.name.firstName() + " " + faker.name.lastName())
								.toUpperCase());
				
				$("#email").val(faker.internet.email());
				
				var ccTypes = [ "MasterCard", "Visa" ];
				var ccType = ccTypes[faker.random.number(ccTypes.length - 1)];
				$("#cctype").val(ccType);

				$("#pan").val(creditCardsGenerator.generate(ccType));
				
				var expirationMonth = (faker.random.number(11) + 1).toString();
				expirationMonth = expirationMonth.length == 1 ? "0" + expirationMonth : expirationMonth;
				$("#expiration_month").val(expirationMonth);
				$("#expiration_year").val(faker.random.number(12) + 2018);
				
				$("#cvv2").val(
						(faker.random.number() + 100).toString().substr(0, 3));
				$("#issuer_bank").val(faker.company.companyName() + " Bank");
			},

			onCardLinkClick: function(evt) {
				var $this = $(this);
				if ($this.is(".disabled") || $this.is(".active"))
					return;
				var $targetCard = $("#" + $this.data("card"));
				$(".card-link").removeClass("active");
				$this.addClass("active");
				$(".card-content").hide();
				$targetCard.fadeIn();
			},

			onPaymentFormSubmit: function(evt) {
				evt.preventDefault();

				var $form = $("#payments_creation_form"),
					serializedFormData = $form.serializeArray();

				serializedFormData.unshift({});
				
				page.formDataHash = serializedFormData
						.reduce(function(acc, obj) {
							acc[obj["name"]] = obj["value"];
							return acc;
						});

				var tokenizationRequest = window.factories
						.tokenizationRequestFactory(page.formDataHash);

				$.ajax({
					url: window.configuration.integration.tokenizationApiUrl
					   + "/card_token",
					type: "POST",
					accept: "application/json",
					contentType: "application/json",
					data: JSON.stringify(tokenizationRequest),
					success: function(data) {
						page.token = data;
						
						$form.find(":input").prop("disabled", true);
						
						$(".card-link[data-card=card2]")
							.removeClass("disabled").trigger("click");
						
						$("#tokenization_request_text")
							.html(JSON.stringify(tokenizationRequest, null, "\t"));
						
						$("#tokenization_response_text")
							.html(JSON.stringify(data, null, "\t"));

						$('#tokenization_request_text, #tokenization_response_text')
							.each(function(i, block) { hljs.highlightBlock(block); });
					},
					error : function() {
						$form.find(":input").prop("disabled", false);
						$(".card-link[data-card=card2]").addClass("disabled");
						$(".card-link[data-card=card1]").trigger("click");
					}
				});
			},
			
			onProceedToPaymentClick: function (evt) {
				var paymentRequest = window.factories
					.paymentRequestFactory(page.formDataHash, page.token);
				
				console.log("Sending payment request to back end", paymentRequest);
				
				$.ajax({
					url: "/api/payments/card",
					accept: "application/json",
					contentType: "application/json",
					method: "POST",
					data: JSON.stringify(paymentRequest),
					success: function (data) {
						console.log("success", data);
						
						$(".card-link[data-card=card3]")
							.removeClass("disabled").trigger("click");
						
						$("#payment_request_text")
							.html(JSON.stringify(data.chargeRequest, null, "\t"));
						
						var template = '<div class="container"><h5>Charge Attempt ##index##: ##gateway## '
							+ '<span class="badge badge-##outcome_label##">##outcome##</span></h5><pre>'
							+ '<code class="json formatted-code-block payment-response-text" '
							+ 'id="payment_response_text_##index##">##content##</code></pre></div>';

						var html = "";
						
						for(var i = 0; i < data.chargeAttempts.length; i ++) {
							var attempt = data.chargeAttempts[i];
							html += template
								.replace(/##index##/gi, i + 1)
								.replace(/##gateway##/gi, data.attemptedGateways[i])
								.replace(/##outcome_label##/gi, attempt.chargeResponse.PaymentSucceeded
									? "success" : "danger")
								.replace(/##outcome##/gi, attempt.chargeResponse.PaymentSucceeded 
									? "Success" : "Failure")
								.replace(/##content##/gi, attempt.successStatusCodeReceived
										? JSON.stringify(attempt.chargeResponse, null, "\t")
										: JSON.stringify(attempt.errorContent, null, "\t"));
						}
						
						$("#payment_attempts_header").append(html);
						$("#attempted_gateways_header").append("<ol>"
							+ data.attemptedGateways
								.map(function (obj) { return "<li>" + obj + "</li>" })
								.reduce(function (acc, obj) { return acc + obj })
							+ "</ol>");
						
						$('#payment_request_text, .payment-response-text')
							.each(function(i, block) { hljs.highlightBlock(block); });
						
						$('#proceed_to_payment').prop('disabled', true);
					}
				});
			}
		};

		$(window).on("etr:configurationLoaded", function() {
			page.initialize();
		});

	});
})(window, document, jQuery);