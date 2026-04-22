/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.exception.SensorUnavailableException;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author yasara
 */
public class SensorReadingResource {
    private static Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();
    private String sensorId;
    
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getReadings() {
        return readings.getOrDefault(sensorId, new ArrayList<>());
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        Sensor sensor = SensorResource.sensors.get(sensorId);
        
        // Check if the sensor exists
        if (sensor == null) {
            throw new LinkedResourceNotFoundException("Sensor not found");
        }
        
        // Check if the sensor is active
        if (!sensor.getStatus().equalsIgnoreCase("ACTIVE")) {
            throw new SensorUnavailableException("Sensor is not active");
        }
        
        // Store reading
        readings.putIfAbsent(sensorId, new ArrayList<>());
        readings.get(sensorId).add(reading);
        
        return Response.status(201).entity(reading).build();
    } 
}
