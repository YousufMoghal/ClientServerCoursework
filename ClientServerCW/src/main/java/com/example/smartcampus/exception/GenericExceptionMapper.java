
package com.example.smartcampus.exception;

import com.example.smartcampus.model.ErrorMessage;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GenericExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.severe("Unhandled exception caught by GenericExceptionMapper: " + exception.getClass().getSimpleName());


        ErrorMessage errorMessage = new ErrorMessage(
                "An internal server error occurred. Please contact the administrator.",
                500,
                "/api/v1"
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .build();
    }
}
