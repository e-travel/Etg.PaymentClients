package com.etraveli.payments.client.interceptors;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class LoggingRequestInterceptor implements ClientHttpRequestInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		traceRequest(request, body);
		ClientHttpResponse response = execution.execute(request, body);
		traceResponse(response);
		return response;
	}

	private void traceRequest(HttpRequest request, byte[] body) throws IOException {
		logger.debug("===========================request begin================================================");
		logger.debug("URI         : {}", request.getURI());
		logger.debug("Method      : {}", request.getMethod());
		logger.debug("Headers     : {}", request.getHeaders());
		logger.debug("Request body: {}", new String(body, "UTF-8"));
		logger.debug("==========================request end================================================");
	}

	private void traceResponse(ClientHttpResponse response) throws IOException {
		
		logger.debug("============================response begin==========================================");
		logger.debug("Status code  : {}", response.getStatusCode());
		logger.debug("Status text  : {}", response.getStatusText());
		logger.debug("Headers      : {}", response.getHeaders());
		logger.debug("Response body: {}", StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
		logger.debug("=======================response end=================================================");
	}
}
