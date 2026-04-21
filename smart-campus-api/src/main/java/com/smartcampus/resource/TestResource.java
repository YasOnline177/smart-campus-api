/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author yasara
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    @GET
    public Map<String, Object> getApiInfo() {
        Map<String, Object> response = new HashMap<>();

        response.put("version", "v1");
        response.put("contact", "admin@smartcampus.com");

        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");

        response.put("links", links);

        return response;
    }
}
