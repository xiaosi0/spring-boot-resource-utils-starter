package com.github.resouce.utils.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * zookeeper的熟悉
 * @author liuhaoyong 2017年6月23日 上午11:34:31
 */
@ConfigurationProperties( prefix = "zookeeper" )
public class ZookeeperProperties {
    
    /**
     * zk服务器的链接字符串,格式　ip:prot,ip:port
     */
    private String connectString;

    /**
     * 到zk服务器session的超时时间
     * 如果给定的这个时间段内, 服务器未收到客户端的心跳, zk服务器将认为客户端已下线,并删除与之相关的临时节点
     * 注意该时间至少需要设置成zk服务器ticketTimeOut的2倍,但不能大于20倍
     */
    private int sessionTimeoutMs=4000;
    
    /**
     * zk的根路径
     */
    private String rootPath="/finance";

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    } 
    
    
    
    
    
}
