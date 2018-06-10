package com.github.resouce.utils.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** 
 * @Description SuperDiamondProperties.java
 * @author  mapenghui 
 * @date 2017年6月19日 下午4:55:54 
 */
@ConfigurationProperties( prefix = "super.diamond" )
public class SuperDiamondProperties {

    private String serverIp;

    private int serverPort;
    
    private String projCode;

    private String serverEnv;
    
    private String channelServerIp;

    private int channelServerPort;
    
    private String channelProjCode;

    private String channelServerEnv;

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getProjCode() {
		return projCode;
	}

	public void setProjCode(String projCode) {
		this.projCode = projCode;
	}

	public String getServerEnv() {
		return serverEnv;
	}

	public void setServerEnv(String serverEnv) {
		this.serverEnv = serverEnv;
	}

	public String getChannelServerIp() {
		return channelServerIp;
	}

	public void setChannelServerIp(String channelServerIp) {
		this.channelServerIp = channelServerIp;
	}

	public int getChannelServerPort() {
		return channelServerPort;
	}

	public void setChannelServerPort(int channelServerPort) {
		this.channelServerPort = channelServerPort;
	}

	public String getChannelProjCode() {
		return channelProjCode;
	}

	public void setChannelProjCode(String channelProjCode) {
		this.channelProjCode = channelProjCode;
	}

	public String getChannelServerEnv() {
		return channelServerEnv;
	}

	public void setChannelServerEnv(String channelServerEnv) {
		this.channelServerEnv = channelServerEnv;
	}
    
}


