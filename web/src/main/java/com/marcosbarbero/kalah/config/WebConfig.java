package com.marcosbarbero.kalah.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web configuration.
 *
 * @author Marcos Barbero
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final String ROOT_PATH = "/";
    private static final String INDEX_PATH = "/index";
    private static final String VIEW_NAME = "index";

    /**
     * Mapping root to index view.
     *
     * @param registry ViewControllerRegistry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(ROOT_PATH).setViewName(VIEW_NAME);
        registry.addViewController(INDEX_PATH).setViewName(VIEW_NAME);
    }
}
