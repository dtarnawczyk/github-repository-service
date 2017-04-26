package org.githubservice.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfiguration {

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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(getClientHttpRequestFactory());
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        RequestConfig.Builder builder = RequestConfig.custom()
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectionRequestTimeout(requestTimeout)
                .setMaxRedirects(maxRedirects)
                .setExpectContinueEnabled(expectContinue);
        if (maxRedirects <= 0) {
            builder.setRedirectsEnabled(false);
        }
        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultRequestConfig(builder.build())
                .disableCookieManagement()
                .disableAuthCaching()
                .build();
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }
}
