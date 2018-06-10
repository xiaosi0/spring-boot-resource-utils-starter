package com.github.resouce.utils.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "customize.tomcat")
public class CustomizedTomcatProperties {
    /**
     * tomcat的keepAlive超时时间，超过这个时间的连接没有收到下个请求，将关闭
     * 默认20秒，对外开放的web应用请保持该默认值不变，内网rest服务可以适当调大　
     */
    private int keepAliveTimeout = 20 * 1000;
    
    /**
     * 服务器保持的最大keepAlive的连接数
     * 默认100个，对外开放的web应用请保持该默认值不变，对内服务的rest服务可以适当调大
     */
    private int maxKeepAliveRequests = 100 ;

    public int getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public void setKeepAliveTimeout(int keepAliveTimeout) {
        this.keepAliveTimeout = keepAliveTimeout;
    }

    public int getMaxKeepAliveRequests() {
        return maxKeepAliveRequests;
    }

    public void setMaxKeepAliveRequests(int maxKeepAliveRequests) {
        this.maxKeepAliveRequests = maxKeepAliveRequests;
    }


}
