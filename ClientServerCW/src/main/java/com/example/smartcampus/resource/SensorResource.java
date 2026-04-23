
package com.example.smartcampus.resource;

import com.example.smartcampus.exception.DataNotFoundException;
import com.example.smartcampus.exception.LinkedResourceNotFoundException;
import com.example.smartcampus.model.Sensor;
import com.example.smartcampus.repository.DatabaseStorage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(DatabaseStorage.SENSORS.values());

        if (type != null && !type.trim().isEmpty()) {
            sensors = sensors.stream()
                    .filter(sensor -> sensor.getType() != null
                    && sensor.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return Response.ok(sensors).build();
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DatabaseStorage.SENSORS.get(sensorId);

        if (sensor == null) {
            throw new DataNotFoundException("Sensor with ID " + sensorId + " not found.");
        }

        return sensor;
    }

    @POST
    public Response addSensor(Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null) {
            throw new IllegalArgumentException("Sensor data is required.");
        }

        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Sensor id is required.");
        }

        if (sensor.getType() == null || sensor.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Sensor type is required.");
        }

        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("Sensor status is required.");
        }

        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            throw new IllegalArgumentException("roomId is required.");
        }

        if (DatabaseStorage.SENSORS.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A sensor with this id already exists.")
                    .build();
        }

        if (!DatabaseStorage.ROOMS.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Referenced room does not exist for roomId: " + sensor.getRoomId());
        }

        DatabaseStorage.SENSORS.put(sensor.getId(), sensor);
        DatabaseStorage.ROOMS.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        DatabaseStorage.SENSOR_READINGS.put(sensor.getId(), new ArrayList<>());

        URI uri = uriInfo.getAbsolutePathBuilder().path(sensor.getId()).build();
        return Response.created(uri).entity(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
