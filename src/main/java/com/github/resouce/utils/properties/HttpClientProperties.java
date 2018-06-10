package com.github.resouce.utils.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description HttpClientProperties.java
 */
@ConfigurationProperties(prefix = "http.client")
public class HttpClientProperties {

    private int connectTimeout  = 10 * 1000;

    private int readTimeout     = 60 * 1000;

    private int maxConnTotal    = 2000;
    private int maxConnPerRoute = 10;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxConnTotal() {
        return maxConnTotal;
    }

    public void setMaxConnTotal(int maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
    }

    public int getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public void setMaxConnPerRoute(int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

}
