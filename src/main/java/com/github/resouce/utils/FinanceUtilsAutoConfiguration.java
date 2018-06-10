package com.github.resouce.utils;

import java.io.IOException;
import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.alibaba.boot.dubbo.DubboProperties;
import com.github.diamond.client.PropertiesConfiguration;
import com.github.java.common.utils.StringUtils;
import com.github.resouce.utils.dubbo.DubboService;
import com.github.resouce.utils.filter.MDCFilter;
import com.github.resouce.utils.properties.HttpClientProperties;
import com.github.resouce.utils.properties.PaymentCommonProperties;
import com.github.resouce.utils.properties.SuperDiamondProperties;
import com.github.resouce.utils.properties.ZookeeperProperties;
import com.github.resouce.utils.utils.RedisCache;
import com.github.resouce.utils.utils.SpringContextHolder;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * common类里的spring相关bean定义在这里
 */
@Order(10)
@Configuration
@EnableSwagger2
@EnableConfigurationProperties({ PaymentCommonProperties.class, HttpClientProperties.class,
        SuperDiamondProperties.class, ZookeeperProperties.class })
public class FinanceUtilsAutoConfiguration {

    private final static Logger          logger = LoggerFactory.getLogger(FinanceUtilsAutoConfiguration.class);
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;
    @Autowired
    private StringRedisTemplate          redisTemplate;

    @Autowired
    private PaymentCommonProperties      paymentCommonProperties;
    @Autowired
    private HttpClientProperties         httpClientProperties;
    @Autowired
    private SuperDiamondProperties       superDiamondProperties;
    @Autowired
    private ZookeeperProperties          zookeeperProperties;
    @Autowired
    private DubboProperties              dubboProperties;

    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    @ConditionalOnExpression("${spring-context-holder-enable:false}")
    public SpringContextHolder springContextHolder(ApplicationContext applicationContext) {
        return new SpringContextHolder(applicationContext);
    }

    @Bean
    @ConditionalOnMissingBean(RedisCache.class)
    @ConditionalOnProperty(name = "spring.redis.host", matchIfMissing = false)
    public RedisCache redisCache() {
        if (StringUtils.isBlank(paymentCommonProperties.getRedisKeyPrefix())) {
            logger.warn("RedisKeyPrefix not found!");
        }
        return new RedisCache(paymentCommonProperties.getRedisKeyPrefix(), redisTemplate);
    }

    /**
     * 转换器：数字转日期
     */
    @PostConstruct
    public void initEditableValidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter
                .getWebBindingInitializer();
        if (initializer.getConversionService() != null) {
            GenericConversionService genericConversionService = (GenericConversionService) initializer
                    .getConversionService();
            class String2TimestampConverter implements Converter<Long, Timestamp> {
                @Override
                public Timestamp convert(Long time) {
                    return new Timestamp(time);
                }
            }
            genericConversionService.addConverter(new String2TimestampConverter());
        }

    }

    @Bean
    @ConditionalOnExpression("${mdc-filter-enable:false}")
    public FilterRegistrationBean mdcFilterRegistrationBean() {
        MDCFilter.mdcPrefix = paymentCommonProperties.getMdcPrefix();
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        MDCFilter mdcFilter = new MDCFilter();
        registrationBean.setFilter(mdcFilter);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnProperty(name = "http.client.connectTimeout", matchIfMissing = false)
    public CloseableHttpClient initHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(httpClientProperties.getConnectTimeout())
                .setConnectionRequestTimeout(httpClientProperties.getConnectTimeout())
                .setSocketTimeout(httpClientProperties.getReadTimeout()).build();

        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setMaxConnTotal(httpClientProperties.getMaxConnTotal())
                .setMaxConnPerRoute(httpClientProperties.getMaxConnPerRoute())
                .setRetryHandler(new DefaultHttpRequestRetryHandler() {
                    @Override
                    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                        boolean isRetry = super.retryRequest(exception, executionCount, context);
                        if (isRetry) {
                            return isRetry;
                        }
                        if (executionCount > super.getRetryCount()) {
                            return isRetry;
                        }
                        if (exception instanceof NoHttpResponseException) {
                            return true;
                        }
                        return isRetry;
                    }
                }).build();
        // JVM shutdown时释放资源
        Runtime run = Runtime.getRuntime();
        run.addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("release http client resource begin ...");
                try {
                    httpClient.close();
                } catch (IOException e) {
                    logger.info("IO异常", e);
                }
                logger.info("release http client resource end ...");
            }
        });
        return httpClient;
    }

    /**
     * 初始化配置中心服务,访问superDiamond
     */
    @Bean("propertiesConfiguration")
    @ConditionalOnMissingBean(name = "propertiesConfiguration")
    @ConditionalOnProperty(name = "super.diamond.serverIp", matchIfMissing = false)
    public PropertiesConfiguration initDiamond() {
        PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(
                superDiamondProperties.getServerIp(), superDiamondProperties.getServerPort(),
                superDiamondProperties.getProjCode(), superDiamondProperties.getServerEnv());
        return propertiesConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean(Docket.class)
    @ConditionalOnExpression("${swagger-enable:false}")
    public Docket buildDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("Spring Boot中使用Swagger2 UI构建API文档").contact("COM").version("1.0")
                .build();
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo).select()
                .apis(RequestHandlerSelectors.basePackage("com")) // 要扫描的API(Controller)基础包
                .paths(PathSelectors.any()).build();
    }

    @Bean
    @ConditionalOnMissingBean(CuratorFramework.class)
    @ConditionalOnProperty(name = "zookeeper.connect-string", matchIfMissing = false)
    public CuratorFramework curatorFramework() {
        final CuratorFramework client = CuratorFrameworkFactory.newClient(this.zookeeperProperties.getConnectString(),
                zookeeperProperties.getSessionTimeoutMs(), 4000, new ExponentialBackoffRetry(1000, 3));
        client.start();
        /**
         * 优雅关闭：系统关闭时候，监听关闭，处理完成任务后关闭
         */
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                client.close();
            }
        }));
        return client;
    }

    /**
     * dubbo注册服务
     * 
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(DubboService.class)
    @ConditionalOnProperty(name = "spring.dubbo.registry", matchIfMissing = false)
    public DubboService dubboService() {
        DubboService dubboService = new DubboService(dubboProperties.getRegistry(), dubboProperties.getAppname());
        return dubboService;
    }

}
