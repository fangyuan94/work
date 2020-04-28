package com.fc.work.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fc.work.intercepts.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Bean
    public SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor(stringRedisTemplate);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //验签拦截器
        registry.addInterceptor(securityInterceptor());
    }

}
