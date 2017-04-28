package org.githubservice.service;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class GithubClientConfiguration {

    @Value("${github.api.connectionTimeout}")
    private int connectionTimeout;

    @Value("${github.api.socketTimeout}")
    private int socketTimeout;

    @Value("${github.api.requestTimeout}")
    private int requestTimeout;

    @Value("${github.api.maxRedirects}")
    private int maxRedirects;

    @Value("${github.api.expectContinue}")
    private boolean expectContinue;

    @Value("${github.api.retriesCounter}")
    private int retriesCounter;

    @Value("${github.api.retriesTimeout}")
    private int retriesTimeout;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(getFixedBackOffPolicy());
        retryTemplate.setRetryPolicy(getRetryPolicy());
        return retryTemplate;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(getRequestConfig())
                .disableCookieManagement()
                .disableAuthCaching()
                .build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    private RequestConfig getRequestConfig() {
        RequestConfig.Builder builder = RequestConfig.custom()
                .setConnectTimeout(this.connectionTimeout * 1000)
                .setSocketTimeout(this.socketTimeout * 1000)
                .setConnectionRequestTimeout(this.requestTimeout * 1000)
                .setMaxRedirects(this.maxRedirects)
                .setExpectContinueEnabled(this.expectContinue);
        if (this.maxRedirects <= 0) {
            builder.setRedirectsEnabled(false);
        }
        return builder.build();
    }

    private FixedBackOffPolicy getFixedBackOffPolicy() {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(retriesTimeout * 1000);
        return fixedBackOffPolicy;
    }

    private SimpleRetryPolicy getRetryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionsMap = new HashMap<>();
        exceptionsMap.put(HttpStatusCodeException.class, true);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(this.retriesCounter, exceptionsMap);
        return retryPolicy;
    }
}
