/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

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
        readings.putIfAbsent(sensorId, new ArrayList<>());
        readings.get(sensorId).add(reading);
        
        // Update sensor current value
        Sensor sensor = SensorResource.sensors.get(sensorId);
        
        if (sensor != null) {
            sensor.setCurrentValue(reading.getValue());
        }
        
        return Response.status(201).entity(reading).build();
    } 
}
