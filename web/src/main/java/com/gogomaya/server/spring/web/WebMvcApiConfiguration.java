package com.gogomaya.server.spring.web;

import java.util.Arrays;

import javax.inject.Singleton;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.repository.UriToDomainObjectUriResolver;
import org.springframework.data.rest.repository.context.ValidatingRepositoryEventListener;
import org.springframework.data.rest.repository.jpa.JpaRepositoryExporter;
import org.springframework.data.rest.webmvc.BaseUriMethodArgumentResolver;
import org.springframework.data.rest.webmvc.PagingAndSortingMethodArgumentResolver;
import org.springframework.data.rest.webmvc.RepositoryAwareMappingHttpMessageConverter;
import org.springframework.data.rest.webmvc.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerAdapter;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerMapping;
import org.springframework.data.rest.webmvc.ServerHttpRequestMethodArgumentResolver;
import org.springframework.data.rest.webmvc.json.JsonSchemaController;
import org.springframework.data.rest.webmvc.json.RepositoryAwareJacksonModule;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

@Configuration
public class WebMvcApiConfiguration {

    /**
     * {@link org.springframework.data.rest.repository.RepositoryExporter} implementation for exporting JPA repositories.
     */
    @Autowired(required = false)
    protected JpaRepositoryExporter customJpaRepositoryExporter;

    /**
     * {@link org.springframework.context.ApplicationListener} implementation for invoking {@link org.springframework.validation.Validator} instances assigned
     * to specific domain types.
     */
    @Autowired(required = false)
    protected ValidatingRepositoryEventListener validatingRepositoryEventListener;

    /**
     * Main configuration for the REST exporter.
     */
    @Autowired(required = false)
    protected RepositoryRestConfiguration repositoryRestConfig = RepositoryRestConfiguration.DEFAULT;

    /**
     * For getting access to the {@link javax.persistence.EntityManagerFactory}.
     * 
     * @return
     */
    @Bean
    @Singleton
    public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
        return new PersistenceAnnotationBeanPostProcessor();
    }

    /**
     * Use the pre-defined {@link JpaRepositoryExporter} defined by the user or create a default one.
     * 
     * @return
     */
    @Bean
    @Singleton
    public JpaRepositoryExporter jpaRepositoryExporter() {
        if (null == customJpaRepositoryExporter) {
            return new JpaRepositoryExporter();
        }

        return customJpaRepositoryExporter;
    }

    /**
     * Use the pre-defined {@link ValidatingRepositoryEventListener} defined by the user or create a default one.
     * 
     * @return
     */
    @Bean
    @Singleton
    public ValidatingRepositoryEventListener validatingRepositoryEventListener() {
        return (null == validatingRepositoryEventListener ? new ValidatingRepositoryEventListener() : validatingRepositoryEventListener);
    }

    /**
     * A special Jackson {@link org.codehaus.jackson.map.Module} implementation that configures converters for entities.
     * 
     * @return
     */
    @Bean
    @Singleton
    public RepositoryAwareJacksonModule jacksonModule() {
        return new RepositoryAwareJacksonModule();
    }

    /**
     * Special Repository-aware {@link org.springframework.http.converter.HttpMessageConverter} that can deal with
     * entities and links.
     * 
     * @return
     */
    @Bean
    @Singleton
    public RepositoryAwareMappingHttpMessageConverter mappingHttpMessageConverter() {
        return new RepositoryAwareMappingHttpMessageConverter();
    }

    /**
     * A {@link org.springframework.data.rest.core.UriResolver} implementation that takes a {@link java.net.URI} and
     * turns
     * it
     * into a top-level domain object.
     * 
     * @return
     */
    @Bean
    @Singleton
    public UriToDomainObjectUriResolver domainObjectResolver() {
        return new UriToDomainObjectUriResolver();
    }

    /**
     * The main REST exporter Spring MVC controller.
     * 
     * @return
     * 
     * @throws Exception
     */
    @Bean
    @Singleton
    public RepositoryRestController repositoryRestController() throws Exception {
        return new RepositoryRestController();
    }

    @Bean
    @Singleton
    public JsonSchemaController jsonSchemaController() {
        return new JsonSchemaController();
    }

    @Bean
    @Singleton
    public BaseUriMethodArgumentResolver baseUriMethodArgumentResolver() {
        return new BaseUriMethodArgumentResolver();
    }

    @Bean
    @Singleton
    public PagingAndSortingMethodArgumentResolver pagingAndSortingMethodArgumentResolver() {
        return new PagingAndSortingMethodArgumentResolver();
    }

    @Bean
    @Singleton
    public ServerHttpRequestMethodArgumentResolver serverHttpRequestMethodArgumentResolver() {
        return new ServerHttpRequestMethodArgumentResolver();
    }

    /**
     * Special {@link org.springframework.web.servlet.HandlerAdapter} that only recognizes handler methods defined in the {@link RepositoryRestController}
     * class.
     * 
     * @return
     */
    @Bean
    @Singleton
    public RepositoryRestHandlerAdapter repositoryExporterHandlerAdapter() {
        return new RepositoryRestHandlerAdapter();
    }

    /**
     * Special {@link org.springframework.web.servlet.HandlerMapping} that only recognizes handler methods defined in the {@link RepositoryRestController}
     * class.
     * 
     * @return
     */
    @Bean
    @Singleton
    public RepositoryRestHandlerMapping repositoryExporterHandlerMapping() {
        return new RepositoryRestHandlerMapping();
    }

    /**
     * Bean for looking up methods annotated with {@link org.springframework.web.bind.annotation.ExceptionHandler}.
     * 
     * @return
     */
    @Bean
    @Singleton
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver er = new ExceptionHandlerExceptionResolver();
        er.setCustomArgumentResolvers(Arrays.<HandlerMethodArgumentResolver> asList(new ServerHttpRequestMethodArgumentResolver()));
        return er;
    }

}
