# spring-boot-resource-utils-starter
spring-boot-resource-utils-starter
所有配置如下
#resource-util start---------
#resource-util spring applicationContext
spring-context-holder-enable=true

#resource-util  mdc
mdc-filter-enable=true
#resource-util mdc
resouce.common.mdcPrefix=demo

#resource-util  swagger
swagger-enable=true

#resource-util redisPrefix
resouce.common.redisKeyPrefix=demo

#resource-util zookeeper
zookeeper.connect-string=172.16.20.58:2181

#resource-util httpClient
http.client.connectTimeout=1000
http.client.readTimeout=30000
http.client.maxConnTotal=3000
http.client.maxConnPerRoute=500

#resource-util  diamond
super.diamond.serverIp=172.16.20.58
super.diamond.serverPort=8283
super.diamond.serverEnv=development
super.diamond.projCode=promotion

#resource-util  dubbo
spring.dubbo.appname=github_demo
spring.dubbo.protocol=dubbo
spring.dubbo.registry=zookeeper://172.16.20.171:2181
spring.dubbo.port=22228
spring.dubbo.owner=github_demo
spring.dubbo.version=1.0.0

#resource-util end---------
