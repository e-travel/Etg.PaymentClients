(function (window, document, $, undefined) {
	$(function () {

		$.ajax({
			url: "/api/config/etravel_integration",
			dataType: "json",
			success: function (data) {
				window.configuration = window.configuration || {};
				window.configuration.integration = window.configuration.integration || data; 
				$(window).trigger("etr:configurationLoaded");
			}
		});
		
	});
})(window, document, jQuery);