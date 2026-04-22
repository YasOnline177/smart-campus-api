/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.config;

import com.smartcampus.filter.LoggingRequestFilter;
import com.smartcampus.filter.LoggingResponseFilter;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author yasara
 */
@ApplicationPath("/api/v1")
public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("com.smartcampus");
        
        register(LoggingRequestFilter.class);
        register(LoggingResponseFilter.class);
    }
}
