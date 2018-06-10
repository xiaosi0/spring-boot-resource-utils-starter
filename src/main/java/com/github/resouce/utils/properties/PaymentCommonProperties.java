package com.github.resouce.utils.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @Description: PaymentCommon properties
 * @author quyinjun
 * @date 2017年6月7日 下午1:06:52
 */
@ConfigurationProperties(prefix = "payment.common")
public class PaymentCommonProperties {
	/**
	 * redis存储前缀，如payment-engine: payment.common.redis.key.prefix=PAY-PE
	 */
	private String redisKeyPrefix;

	/**
	 * MDC前缀
	 */
	private String mdcPrefix;
	
	/**
	 * 渠道指令前缀
	 */
	private String channelInstructionIdPrefix;
	


	public String getRedisKeyPrefix() {
		return redisKeyPrefix;
	}

	public void setRedisKeyPrefix(String redisKeyPrefix) {
		this.redisKeyPrefix = redisKeyPrefix;
	}

	public String getMdcPrefix() {
		return mdcPrefix;
	}

	public void setMdcPrefix(String mdcPrefix) {
		this.mdcPrefix = mdcPrefix;
	}

	public String getChannelInstructionIdPrefix() {
		return channelInstructionIdPrefix;
	}

	public void setChannelInstructionIdPrefix(String channelInstructionIdPrefix) {
		this.channelInstructionIdPrefix = channelInstructionIdPrefix;
	}

}
