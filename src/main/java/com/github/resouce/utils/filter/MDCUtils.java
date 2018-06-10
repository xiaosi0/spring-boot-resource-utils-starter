package com.github.resouce.utils.filter;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.github.java.common.base.Printable;

/**
 * @author liuhaoyong 2017年4月28日 下午5:03:00
 */
public class MDCUtils implements AutoCloseable {
    private final static Logger logger        = LoggerFactory.getLogger(MDCUtils.class);

    public static final String  REQ_KEY       = "reqKey";

    public static String        localHostName = null;

    static {
        try {
            localHostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            logger.error("获取本机HostName异常:{}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        System.out.println(localHostName);
    }

    /**
     * 初始化mdc, 通常用于请求开始前
     * 
     * @param print
     */
    public static void initMDC(Printable obj) {
        if (obj == null) {
            return;
        }

        String currentMDC = getMDC();
        if (StringUtils.isNotBlank(currentMDC)) {
            if (StringUtils.isNotBlank(obj.buildMDCKey())) {
                MDC.put(REQ_KEY, currentMDC + obj.buildMDCKey());
            }
        } else {
            MDC.put(REQ_KEY, obj.buildMDCKey());
        }
        addLocalHostName();
    }

    public static void initMDC(Object... objects) {
        if (objects == null) {
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (Object item : objects) {
            builder.append(item);
        }
        String currentMDC = getMDC();
        if (StringUtils.isNotBlank(currentMDC)) {
            MDC.put(REQ_KEY, currentMDC + builder.toString());
        } else {
            MDC.put(REQ_KEY, builder.toString());
        }
        addLocalHostName();
    }

    public static void addLocalHostName() {
        String mdc = getMDC();
        if (StringUtils.isNotBlank(localHostName) && (mdc.indexOf(localHostName) < 0)) {
            if (StringUtils.isNotBlank(mdc)) {
                MDC.put(REQ_KEY, mdc + "-" + localHostName + "-");
            } else {
                MDC.put(REQ_KEY, localHostName + "-");
            }
        }
    }

    public static String getMDC() {
        return MDC.get(REQ_KEY);
    }

    public static void clear() {
        MDC.clear();
    }

    @Override
    public void close() {
        MDC.clear();
    }
}
