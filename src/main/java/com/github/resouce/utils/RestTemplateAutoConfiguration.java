package com.github.resouce.utils;

import java.util.Collections;

import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.github.resouce.utils.filter.MDCInterceptor;

@Configuration
@ConditionalOnProperty(name = "http.client.connectTimeout", matchIfMissing = false)
public class RestTemplateAutoConfiguration {
    @Autowired
    private RestTemplateBuilder builder;
    @Autowired
    private HttpClient          httpClient;

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate initRestTemplate() {
        RestTemplate restTemplate = builder.requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient))
                .build();

        restTemplate.setInterceptors(Collections.singletonList(new MDCInterceptor()));

        return restTemplate;
    }
}
