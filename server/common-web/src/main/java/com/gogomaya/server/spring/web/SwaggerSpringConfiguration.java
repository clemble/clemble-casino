package com.gogomaya.server.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;

import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogomaya.server.spring.common.SpringConfiguration;
import com.mangofactory.swagger.DefaultDocumentationTransformer;
import com.mangofactory.swagger.DocumentationTransformer;
import com.mangofactory.swagger.SwaggerConfiguration;
import com.mangofactory.swagger.SwaggerConfigurationExtension;
import com.mangofactory.swagger.configuration.DefaultConfigurationModule;
import com.mangofactory.swagger.configuration.ExtensibilityModule;
import com.mangofactory.swagger.models.DocumentationSchemaProvider;
import com.mangofactory.swagger.models.Jackson2SchemaDescriptor;
import com.mangofactory.swagger.models.SchemaDescriptor;
import com.mangofactory.swagger.spring.controller.DocumentationController;

abstract public class SwaggerSpringConfiguration implements SpringConfiguration {

    @Bean
    public DocumentationController documentationController() {
        return new DocumentationController();
    }

    @Bean
    @Autowired
    abstract public SwaggerConfiguration swaggerConfiguration(DefaultConfigurationModule defaultConfig, ExtensibilityModule extensibility);

    @Bean
    public DefaultConfigurationModule defaultConfigurationModule() {
        return new DefaultConfigurationModule();
    }

    @Bean
    public ExtensibilityModule extensibilityModule() {
        return new ExtensibilityModule();
    }

    @Bean
    public SwaggerConfigurationExtension swaggerConfigurationExtension() {
        return new SwaggerConfigurationExtension();
    }

    @Bean
    @Autowired
    public DocumentationTransformer documentationTransformer() {
        return new DefaultDocumentationTransformer(null, null);
    }

    @Bean
    @Autowired
    public DocumentationSchemaProvider documentationSchemaProvider(TypeResolver typeResolver, SchemaDescriptor schemaDescriptor) {
        return new DocumentationSchemaProvider(typeResolver, schemaDescriptor);
    }

    @Bean
    @Autowired
    public SchemaDescriptor schemaDescriptor(@Qualifier("objectMapper") ObjectMapper objectMapper) {
        return new Jackson2SchemaDescriptor(objectMapper);
    }

    @Bean
    public TypeResolver typeResolver() {
        return new TypeResolver();
    }

}
