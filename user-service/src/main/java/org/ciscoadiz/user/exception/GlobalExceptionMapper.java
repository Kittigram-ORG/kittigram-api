package org.ciscoadiz.user.exception;

import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOG.errorf(exception, "Exception caught: %s", exception.getMessage());

        if (exception instanceof UserNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(
                            Response.Status.NOT_FOUND.getStatusCode(),
                            exception.getMessage()
                    ))
                    .build();
        }

        if (exception instanceof ForbiddenException) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse(
                            Response.Status.FORBIDDEN.getStatusCode(),
                            exception.getMessage()
                    ))
                    .build();
        }

        if (exception instanceof IllegalArgumentException) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse(
                            Response.Status.CONFLICT.getStatusCode(),
                            exception.getMessage()
                    ))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(
                        Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                        "An unexpected error occurred"
                ))
                .build();
    }
}