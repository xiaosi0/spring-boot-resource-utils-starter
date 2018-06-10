package com.github.resouce.utils.filter;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class MDCInterceptor implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpHeaders headers = request.getHeaders();
		headers.add(MDCFilter.TRACE_NO, MDCUtils.getMDC());
		return execution.execute(request, body);
	}

}