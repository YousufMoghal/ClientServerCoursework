
package com.example.smartcampus.resource;

import com.example.smartcampus.exception.DataNotFoundException;
import com.example.smartcampus.exception.RoomNotEmptyException;
import com.example.smartcampus.model.Room;
import com.example.smartcampus.repository.DatabaseStorage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(DatabaseStorage.ROOMS.values());
    }

    @POST
    public Response addRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null || room.getId() == null || room.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("Room id is required.");
        }

        if (room.getName() == null || room.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Room name is required.");
        }

        if (DatabaseStorage.ROOMS.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("A room with this id already exists.")
                    .build();
        }

        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }

        DatabaseStorage.ROOMS.put(room.getId(), room);

        URI uri = uriInfo.getAbsolutePathBuilder().path(room.getId()).build();
        return Response.created(uri).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        Room room = DatabaseStorage.ROOMS.get(roomId);

        if (room == null) {
            throw new DataNotFoundException("Room with ID " + roomId + " not found.");
        }

        return room;
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DatabaseStorage.ROOMS.get(roomId);

        if (room == null) {
            throw new DataNotFoundException("Room with ID " + roomId + " not found.");
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because it still has sensors assigned.");
        }

        DatabaseStorage.ROOMS.remove(roomId);
        return Response.ok().entity("Room deleted successfully.").build();
    }
}
