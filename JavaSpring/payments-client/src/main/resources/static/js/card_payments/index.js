(function(window, document, $, undefined) {
	$(function() {

		var page = {
			initialize : function() {
				$(".card-content:not(#payment-form-card)").hide();
				$(".card-link[data-card=threed-secure-card]").hide();
				
				this.addEventListeners();
			},

			addEventListeners : function() {
				$(".card-link").on("click", this.onCardLinkClick);
				
				$("#payments_creation_form")
					.on("submit", this.onPaymentFormSubmit);
				
				$("#payments_creation_form")
					.on("reset", this.onPaymentFormReset);
				
				$("#generate_fake_data")
					.on("click", this.onGenerateFakeDataClick);
				
				$("#proceed_to_payment")
					.on("click", this.onProceedToPaymentClick);
				
				$("#use_3d_secure")
					.on("click", this.onUse3dSecureClick);
				
				$("#product_creation_add")
					.on("click", this.onProductCreationAddClick);
				
				$("#products_table tbody")
					.on("click", "tr .product-creation-delete", 
						this.onProductCreationDeleteClick);
				
				$("#products_table tbody")
					.on("mouseover", "tr", this.onProductsTableRowOver);
				
				$("#products_table tbody")
					.on("mouseout", this.onProductsTableOut);
			},
			
			onProductsTableOut: function (evt) {
				$("#products_table .product-creation-delete").hide();
			},
			
			onProductsTableRowOver: function (evt) {
				$("#products_table .product-creation-delete").hide();
				$(this).find(".product-creation-delete").show();
			},
			
			onProductCreationDeleteClick: function (evt) {
				$(this).parents("tr").remove();
			},
			
			onProductCreationAddClick: function (evt) {
				var rows = $("#products_table > tbody > tr").length;
				
				var htmlText = "<tr><td>" + rows 
							 + "</td><td class='product-type'>" + $("#product_creation_type").val() 
							 + "</td><td class='product-id'>" + $("#product_creation_id").val()
							 + "</td><td><input type=button class='btn btn-danger btn-block "
							 + "product-creation-delete btn-sm' value='Delete Item' /></td></tr>";
				
				$(htmlText).insertAfter("#product_creation_row");
				
				$("#product_creation_type").val("");
				$("#product_creation_id").val("");
				$("#products_table .product-creation-delete").hide();
			},
			
			onUse3dSecureClick: function (evt) {
				var $this = $(this);
				
				if ($this.is(":checked")) {
					$(".card-link[data-card=threed-secure-card]").show();
				} else {
					$(".card-link[data-card=threed-secure-card]").hide();
				}
			},

			onGenerateFakeDataClick: function(evt) {
				var $form = $("#payments_creation_form");

				$("#merchantref").val("Flights_"
					+ faker.internet.color().substr(1).toUpperCase() 
					+ "_Ticket_");

				$("#orderid").val(UUIDv4Generator.generate());

				var brands = ["pamediakopes.gr", "avion.ro", 
							  "mytrip.com", "trip.ru"];

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
				
				var htmlText = "<tr><td>1"
							 + "</td><td class='product-type'>Flights"  
							 + "</td><td class='product-id'>" + UUIDv4Generator.generate()
							 + "</td><td><input type=button class='btn btn-danger btn-block "
							 + "product-creation-delete btn-sm' value='Delete Item' /></td></tr>";
	
				$("#products_table tr").each(function (idx, obj) {
					if ($(this).is("#product_creation_row")) return;
					$(this).remove();
				});
				
				$(htmlText).insertAfter("#product_creation_row");
				$("#products_table .product-creation-delete").hide();
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

			onPaymentFormReset: function (evt) {
				$("#products_table tr").each(function (idx, obj) {
					if ($(this).is("#product_creation_row")) return;
					$(this).remove();
				});
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
						
						$(".card-link[data-card=token-card]")
							.removeClass("disabled").trigger("click");
						
						$("#tokenization_request_text")
							.html(JSON.stringify(tokenizationRequest, null, "\t"));
						
						$("#tokenization_response_text")
							.html(JSON.stringify(data, null, "\t"));

						$('#tokenization_request_text, #tokenization_response_text')
							.each(function(i, block) { hljs.highlightBlock(block); });
						
						$.ajax({
							url: "/api/gateways/simple_routing",
							accept: "application/json",
							contentType: "application/json",
							method: "GET",
							data: {
								bin: data.Metadata.Bin,
								currency: page.formDataHash.currency,
								amount: page.formDataHash.amount
							},
							
							success: function (routingData) {
								console.log("Routing success", routingData);
								
								var gateways =
									routingData.simplePaymentsRoutingResponseDto.OrderedGateways;
								
								$(".routing-container").append("<ol class='sortable'><li>" + 
										gateways.reduce(function (acc, obj) { 
											return acc + "</li><li>" + obj;
										}));
								
								$(".sortable").sortable();
							}
						});
					},
					
					error : function() {
						$form.find(":input").prop("disabled", false);
						$(".card-link[data-card=token-card]").addClass("disabled");
						$(".card-link[data-card=payment-form-card]").trigger("click");
					}
				});
			},
			
			onProceedToPaymentClick: function (evt) {
				var products = [], gateways = [];
				
				$("#products_table tr").each(function (idx, obj) {
					if ($(obj).find(".product-type").length == 0) return;
					
					products.push({
						type: $(obj).find(".product-type").text(), 
						id: $(obj).find(".product-id").text()
					});
				});
				
				$("ol.sortable li").each(function (idx, obj) {
					gateways.push($(obj).text());
				});
				
				var paymentRequest = window.factories
					.paymentRequestFactory(page.formDataHash, page.token, products, gateways);
				
				console.log("Sending payment request to back end", paymentRequest);
				
				$.ajax({
					url: "/api/payments/card",
					accept: "application/json",
					contentType: "application/json",
					method: "POST",
					data: JSON.stringify(paymentRequest),
					
					success: function (data) {
						console.log("success", data);
						
						$(".card-link[data-card=log-card]")
							.removeClass("disabled").trigger("click");
						
						var template = '<div class="container"><h5>Step ##index## - ##description## '
							+ '<span class="badge badge-##outcome_label##">##outcome##</span></h5>'
							+ '<h6><a href=#request_##index## data-toggle=collapse>Request</a></h6>'
							+ '<pre class=collapse id=request_##index##><code class="json request-text">'
							+ '##request_content##</code></pre>'
							+ '<h6><a href=#response_##index## data-toggle=collapse>Response</a></h6>'
							+ '<pre class=collapse id=response_##index##><code class="json response-text">'
							+ '##response_content##</code></pre></div>';

						var html = "";
						
						for(var i = 0; i < data.paymentActions.length; i ++) {
							var paymentAction = data.paymentActions[i];
							html += template
								.replace(/##index##/gi, i + 1)
								.replace(/##description##/gi, paymentAction.description)
								.replace(/##outcome_label##/gi, paymentAction.outcome === "Success"
									? "success" : "danger")
								.replace(/##outcome##/gi, paymentAction.outcome === "Success"
									? "Call succeeded" : "Call failed")
								.replace(/##request_content##/gi, 
									JSON.stringify(JSON.parse(paymentAction.requestPayload), null, '\t'))
								.replace(/##response_content##/gi, 
									JSON.stringify(JSON.parse(paymentAction.responsePayload), null, '\t'));
						}
						
						$('#log-card').append($(html));

						$('.request-text,.response-text')
							.each(function(i, block) { hljs.highlightBlock(block); });

						var lastAction = data.paymentActions[data.paymentActions.length - 1];
						
						if (/^Enrollment/gi.test(lastAction.description)) {
							var enrollmentCheckResponse = JSON.parse(lastAction.responsePayload);
							console.log(enrollmentCheckResponse);
							if (enrollmentCheckResponse && enrollmentCheckResponse.RedirectUrl) {
								$(".card-link[data-card=threed-secure-card]")
									.removeClass("disabled").trigger("click");
								
								$("#threed-frame").prop("src", enrollmentCheckResponse.RedirectUrl);
								$("#threed-frame").on("load", function (evt) {
									console.log("Load event after redirection to 3DS",
											this.contentWindow.location.href);
								})
							} else if (enrollmentCheckResponse && enrollmentCheckResponse.AcsUri) {
								var enrollmentCheckResponse = JSON.parse(lastAction.responsePayload);
								console.log(enrollmentCheckResponse);
								if (enrollmentCheckResponse && enrollmentCheckResponse.AcsUri) {
									$(".card-link[data-card=threed-secure-card]")
										.removeClass("disabled").trigger("click");
									
									$("#threed-frame").prop("src", enrollmentCheckResponse.AcsUri);
									$("#threed-frame").on("load", function (evt) {
										console.log("Load event after redirection to 3DS",
												this.contentWindow.location.href);
									})
								}
							}
						} 
					}
				});
			}
		};

		$(window).on("etr:configurationLoaded", function() {
			page.initialize();
		});

	});
})(window, document, jQuery);