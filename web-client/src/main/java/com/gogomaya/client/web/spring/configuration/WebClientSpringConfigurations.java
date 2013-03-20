package com.gogomaya.client.web.spring.configuration;

import java.util.List;

import javax.inject.Singleton;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;
import org.springframework.web.servlet.view.xml.MarshallingView;

import com.gogomaya.client.web.controller.SocialNetworkController;
import com.google.common.collect.Lists;

@Configuration
@EnableWebMvc
public class WebClientSpringConfigurations extends WebMvcConfigurerAdapter {
    
    @Bean
    @Singleton
    public SocialNetworkController socialNetworkController(){
        return new SocialNetworkController();
    }

    @Bean
    @Singleton
    public MappingJacksonHttpMessageConverter jsonHttpMessageHandler() {
        return new MappingJacksonHttpMessageConverter();
    }

    @Bean
    @Singleton
    public ViewResolver getViewResolver() {
        ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
        contentNegotiationManager.resolveFileExtensions(MediaType.APPLICATION_JSON);
        contentNegotiationManager.resolveFileExtensions(MediaType.APPLICATION_XML);

        ContentNegotiatingViewResolver negotiatingViewResolver = new ContentNegotiatingViewResolver();
        negotiatingViewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        negotiatingViewResolver.setContentNegotiationManager(contentNegotiationManager);

        List<View> views = Lists.newArrayList();
        views.add(new MappingJacksonJsonView());
        views.add(new MarshallingView(new XStreamMarshaller()));

        negotiatingViewResolver.setDefaultViews(views);
        return negotiatingViewResolver;
    }

    @Bean
    @Singleton
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
