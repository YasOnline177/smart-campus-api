/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 *
 * @author yasara
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    private static Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    
    @POST
    public Response createSensor(Sensor sensor) {
        Room room = RoomResource.rooms.get(sensor.getRoomId());
        
        if (room == null) {
            throw new RuntimeException("Room does not exist");
        }
        
        sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        
        return Response.status(201).entity(sensor).build();
    }
    
    @GET 
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {
        // No filter
        if (type == null) {
            return sensors.values();
        }
        
        // Filter by type
        return sensors.values().stream().filter(sensor -> sensor.getType().equalsIgnoreCase(type)).toList();
    }
}
