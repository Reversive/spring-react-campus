package ar.edu.itba.paw.webapp.controllers;


import ar.edu.itba.paw.interfaces.AnswerService;
import ar.edu.itba.paw.models.Answer;
import ar.edu.itba.paw.models.exception.AnswerNotFoundException;
import ar.edu.itba.paw.webapp.assemblers.AnswerAssembler;
import ar.edu.itba.paw.webapp.constraints.validators.DtoConstraintValidator;
import ar.edu.itba.paw.webapp.dtos.answer.AnswerCorrectionDto;
import ar.edu.itba.paw.webapp.dtos.answer.AnswerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/api/answers")
@Component
public class AnswerController {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private AnswerService answerService;

    @Autowired
    private AnswerAssembler answerAssembler;

    @Autowired
    private DtoConstraintValidator dtoConstraintValidator;

    @GET
    @Path("/{answerId}")
    @Produces("application/vnd.campus.api.v1+json")
    public Response getAnswer(@PathParam("answerId") Long answerId) {
        Answer answer = answerService.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        return Response.ok(new GenericEntity<AnswerDto>(answerAssembler.toResource(answer, false)){}).build();
    }

    @PUT
    @Path("/{answerId}")
    @Produces("application/vnd.campus.api.v1+json")
    public Response editAnswer(@PathParam("answerId") Long answerId,
                               @Valid AnswerCorrectionDto answerDto) {
        dtoConstraintValidator.validate(answerDto, "Malformed body");
        Answer answer = answerService.findById(answerId).orElseThrow(AnswerNotFoundException::new);
        answer.setScore(answerDto.getScore());
        answer.setCorrections(answerDto.getCorrection());
        answerService.update(answerId, answer);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{answerId}")
    @Produces("application/vnd.campus.api.v1+json")
    public Response deleteAnswer(@PathParam("answerId") Long answerId) {
        if(!answerService.delete(answerId)) {
            throw new NotFoundException();
        }
        return Response.noContent().build();
    }
}
