package com.tglh.newscanningproject.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;


/*
vocs:
    fileUpload:
      rootSavePath: D:\\图片上传位置\\upload\\
      rootHttpPath: /uploadPicName/**
      mustReadSavePath: D:\\必读文件位置\\fileListMustRead
      mustReadHttpPath: /mustReadpath/***/
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${vocs.fileUpload.rootSavePath}")
    private String path;

    @Value("${vocs.fileUpload.rootHttpPath}")
    private String staticPath;


    @Value("${vocs.fileUpload.mustReadSavePath}")
    private String mustReadPath;

    @Value("${vocs.fileUpload.mustReadHttpPath}")
    private String staticMustReadHttpPath;

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器，配置拦截地址
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/*","/home/*","/safeScan/*","/uploadPicName/**","/mustReadpath/*");
    }


    // 如果访问http://localhost:8080/upload/image/test.jpg
    // 实则访问H:/Business/image/test.jpg

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticPath).addResourceLocations("file:"+path);
        registry.addResourceHandler(staticMustReadHttpPath).addResourceLocations("file:"+mustReadPath);
        super.addResourceHandlers(registry);
    }
}
