(function(window, document, undefined) {

	var pseudoRandom = Math.random;
	var visaPrefixList = new Array("4539", "4556", "4916", "4532", "4929",
			"40240071", "4485", "4716", "4");
	var mastercardPrefixList = new Array("51", "52", "53", "54", "55");
	var amexPrefixList = new Array("34", "37");
	var discoverPrefixList = new Array("6011");
	var dinersPrefixList = new Array("300", "301", "302", "303", "36", "38");
	var enRoutePrefixList = new Array("2014", "2149");
	var jcbPrefixList = new Array("35");
	var voyagerPrefixList = new Array("8699");

	function strrev(str) {
		if (!str)
			return '';
		var revstr = '';
		for (var i = str.length - 1; i >= 0; i--)
			revstr += str.charAt(i)
		return revstr;
	}

	function completed_number(prefix, length) {
		var ccnumber = prefix;

		while (ccnumber.length < (length - 1)) {
			ccnumber += Math.floor(pseudoRandom() * 10);
		}

		var reversedCCnumberString = strrev(ccnumber);

		var reversedCCnumber = new Array();
		for (var i = 0; i < reversedCCnumberString.length; i++) {
			reversedCCnumber[i] = parseInt(reversedCCnumberString.charAt(i));
		}

		var sum = 0;
		var pos = 0;

		while (pos < length - 1) {

			var odd = reversedCCnumber[pos] * 2;
			if (odd > 9) {
				odd -= 9;
			}

			sum += odd;

			if (pos != (length - 2)) {

				sum += reversedCCnumber[pos + 1];
			}
			pos += 2;
		}

		var checkdigit = ((Math.floor(sum / 10) + 1) * 10 - sum) % 10;
		ccnumber += checkdigit;

		return ccnumber;

	}

	function credit_card_number(prefixList, length, howMany) {

		var result = new Array();
		for (var i = 0; i < howMany; i++) {

			var randomArrayIndex = Math.floor(pseudoRandom()
					* prefixList.length);
			var ccnumber = prefixList[randomArrayIndex];
			result.push(completed_number(ccnumber, length));
		}

		return result;
	}

	window.creditCardsGenerator = window.creditCardsGenerator || {};

	window.creditCardsGenerator.Schemes = window.creditCardsGenerator.Schemes
			|| {
				"VISA" : {
					prefixList : visaPrefixList,
					digitCount : 16
				},
				"MasterCard" : {
					prefixList : mastercardPrefixList,
					digitCount : 16
				},
				"Amex" : {
					prefixList : amexPrefixList,
					digitCount : 15
				},
				"Diners" : {
					prefixList : dinersPrefixList,
					digitCount : 16
				},
				"Discover" : {
					prefixList : discoverPrefixList,
					digitCount : 16
				},
				"EnRoute" : {
					prefixList : enRoutePrefixList,
					digitCount : 16
				},
				"JCB" : {
					prefixList : jcbPrefixList,
					digitCount : 16
				},
				"Voyager" : {
					prefixList : voyagerPrefixList,
					digitCount : 16
				}
			}

	window.creditCardsGenerator.generate = window.creditCardsGenerator.generate
			|| function(CardScheme, howMany, randomGen) {
				pseudoRandom = randomGen || pseudoRandom;
				var amount = howMany || 1;

				// Try to get configs to the selected Scheme
				if (typeof window.creditCardsGenerator.Schemes[CardScheme] != 'undefined') {
					return credit_card_number(
							window.creditCardsGenerator.Schemes[CardScheme].prefixList,
							window.creditCardsGenerator.Schemes[CardScheme].digitCount,
							amount);
				} else { // Defaults to MasterCard
					return credit_card_number(
							window.creditCardsGenerator.Schemes["MasterCard"].prefixList,
							window.creditCardsGenerator.Schemes["MasterCard"].digitCount,
							amount);
				}
			}

})(window, document);
