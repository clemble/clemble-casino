package com.gogomaya.server.spring.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

//@Configuration
public class RestExporterWebInitializer {
//  implements WebApplicationInitializer {
//    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Step 0. Creating parent ApplicationContext
        AnnotationConfigApplicationContext parentContext = new AnnotationConfigApplicationContext();
        parentContext.register(WebGenericConfiguration.class);
        parentContext.refresh();
        parentContext.start();
        
        // Registering exporter for API
        
        // Step 1. Create Spring application context
        AnnotationConfigWebApplicationContext apiExporterContext = new AnnotationConfigWebApplicationContext();
        apiExporterContext.register(WebMvcApiConfiguration.class);
        apiExporterContext.setParent(parentContext);
        // Step 2. Registering Context loader listener to properly manage started context
        servletContext.addListener(new ContextLoaderListener(apiExporterContext));
        // Step 3. Generating REST Servlet to export existing Repositories
        DispatcherServlet apiExporter = new DispatcherServlet(apiExporterContext);
        // Step 4. Providing appropriate mappings
        ServletRegistration.Dynamic apiRegistration = servletContext.addServlet("api-rest-exporter", apiExporter);
        apiRegistration.setLoadOnStartup(1);
        apiRegistration.addMapping("/api/*");

        // Registering exporter for SPI
        
        // Step 1. Create Spring application context
        AnnotationConfigWebApplicationContext spiExporterContext = new AnnotationConfigWebApplicationContext();
        spiExporterContext.register(WebMvcSpiConfiguration.class);
        spiExporterContext.setParent(parentContext);
        // Step 2. Registering Context loader listener to properly manage started context
        servletContext.addListener(new ContextLoaderListener(spiExporterContext));
        // Step 3. Generating REST Servlet to export existing Repositories
        DispatcherServlet spiExporter = new DispatcherServlet(spiExporterContext);
        // Step 4. Providing appropriate mappings
        ServletRegistration.Dynamic spiRegistration = servletContext.addServlet("spi-rest-exporter", spiExporter);
        spiRegistration.setLoadOnStartup(2);
        spiRegistration.addMapping("/spi/*");
    }

}
