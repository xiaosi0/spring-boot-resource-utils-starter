package com.github.resouce.utils.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;

/**
 * Dubbo服务类：用于从注册中心动态获取服务
 */
public class DubboService {

    /**
     * 注册中心地址
     */
    private String            registryAddress;

    /**
     * 应用名称
     */
    private String            appName;

    /**
     * 应用配置
     */
    private ApplicationConfig applicationConfig;

    /**
     * 注册中心配置
     */
    private RegistryConfig    registryConfig;

    public DubboService() {
        super();
    }

    public DubboService(String registryAddress, String appName) {
        super();
        this.registryAddress = registryAddress;
        this.appName = appName;
    }

    /**
     * 根据接口+分组+版本获取服务
     * 
     * @param interfaceClass
     * @param groupName
     * @return
     */
    public <T> T getService(Class<T> interfaceClass, String groupName, String version) {
        if (applicationConfig == null) {
            applicationConfig = new ApplicationConfig(appName);
        }

        if (registryConfig == null) {
            registryConfig = new RegistryConfig(registryAddress);
        }

        ReferenceConfig<T> referenceConfig = new ReferenceConfig<T>();
        referenceConfig.setRegistry(registryConfig);
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setInterface(interfaceClass);
        referenceConfig.setGroup(groupName);
        referenceConfig.setVersion(version);

        //ReferenceConfig实例很重，此处作缓存
        ReferenceConfigCache configCache = ReferenceConfigCache.getCache();
        T service = null;
        try {
            service = configCache.get(referenceConfig);
        } catch (RuntimeException e) {
            configCache.destroy(referenceConfig);
            throw e;
        }

        return service;
    }

    /**
     * 根据接口获取服务
     * 
     * @param interfaceClass
     * @return
     */
    public <T> T getService(Class<T> interfaceClass) {
        return getService(interfaceClass, null, null);
    }

    /**
     * 根据接口+分组获取服务
     * 
     * @param interfaceClass
     * @return
     */
    public <T> T getService(Class<T> interfaceClass, String groupName) {
        return getService(interfaceClass, groupName, null);
    }

}
