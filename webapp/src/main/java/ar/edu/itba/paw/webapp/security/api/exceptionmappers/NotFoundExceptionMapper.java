package ar.edu.itba.paw.webapp.security.api.exceptionmappers;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Context
    private UriInfo uriInfo;

    public Response toResponse(NotFoundException exception) {
        return ResponseExceptionMapperUtil.toResponse(Response.Status.NOT_FOUND, exception.getMessage(), uriInfo);
    }
}
