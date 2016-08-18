package com.marcosbarbero.kalah.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web configuration.
 *
 * @author Marcos Barbero
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Configure CORS support for entire application.
     *
     * @param registry The CorsRegistry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .exposedHeaders("Location");
    }
}
