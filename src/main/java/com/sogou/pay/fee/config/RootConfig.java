package com.sogou.pay.fee.config;

import com.sogou.pay.fee.service.blueplus.BpService;
import commons.saas.RestNameService;
import commons.spring.LoggerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;

@Configuration
@EnableScheduling
@ComponentScan({ProjectInfo.PKG_PREFIX + ".api", ProjectInfo.PKG_PREFIX + ".manager"})
@PropertySource(value = "classpath:application-default.properties", ignoreResourceNotFound = true)
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class RootConfig {
    @Autowired
    Environment env;

    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        MBeanServerFactoryBean mbeanServer = new MBeanServerFactoryBean();
        mbeanServer.setDefaultDomain(ProjectInfo.PKG_PREFIX);
        mbeanServer.setLocateExistingServerIfPossible(true);
        return mbeanServer;
    }

    @Bean
    public AnnotationMBeanExporter annotationMBeanExporter() {
        return new AnnotationMBeanExporter();
    }

    @Bean
    public LoggerFilter loggerFilter() {
        return new LoggerFilter(env);
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(
                env.getRequiredProperty("redis.url"),
                env.getRequiredProperty("redis.port", Integer.class));
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(Integer.parseInt(env.getProperty("threadpool.max", "1")));
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    @Bean
    public RestNameService restNameService() {
        return new RestNameService(env);
    }

    @Bean
    public BpService bpRestService() {
        return new BpService(env);
    }

    @Bean
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(Integer.parseInt(env.getProperty("rest.timeout.connect", "1000")));
        factory.setReadTimeout(Integer.parseInt(env.getProperty("rest.timeout.read", "10000")));
        return new RestTemplate(factory);
    }

    @Bean
    public RestTemplate bpRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM));
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
        return restTemplate;

    }

}
