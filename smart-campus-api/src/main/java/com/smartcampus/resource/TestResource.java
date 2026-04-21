/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

/**
 *
 * @author yasara
 */
@Path("/")
public class TestResource {

    @GET
    public String test() {
        return "API WORKING";
    }
}
