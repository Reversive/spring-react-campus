package ar.edu.itba.paw.webapp.controllers;


import ar.edu.itba.paw.interfaces.SubjectService;
import ar.edu.itba.paw.models.CampusPage;
import ar.edu.itba.paw.models.Subject;
import ar.edu.itba.paw.models.exception.SubjectNotFoundException;
import ar.edu.itba.paw.webapp.assemblers.SubjectAssembler;
import ar.edu.itba.paw.webapp.constraints.validators.DtoConstraintValidator;
import ar.edu.itba.paw.webapp.dtos.subject.SubjectDto;
import ar.edu.itba.paw.webapp.dtos.subject.SubjectFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Controller
@Path("/api/subjects")
public class SubjectController {

    @Context
    UriInfo uriInfo;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private DtoConstraintValidator dtoValidator;

    @Autowired
    private SubjectAssembler assembler;

    @GET
    @Produces("application/vnd.campus.api.v1+json")
    public Response getSubjects(@QueryParam("page") @DefaultValue("1") Integer page,
                                @QueryParam("page-size") @DefaultValue("10") Integer pageSize) {
        CampusPage<Subject> subjectList = subjectService.list(page, pageSize);
        if (subjectList.isEmpty()){
            return Response.noContent().build();
        }
        List<SubjectDto> subjects = assembler.toResources(subjectList.getContent());
        return Response.ok(new GenericEntity<List<SubjectDto>>(subjects){}).build();
    }

    @POST
    @Consumes("application/vnd.campus.api.v1+json")
    @Produces("application/vnd.campus.api.v1+json")
    public Response getSubject(@Valid SubjectFormDto subjectFormDto){
        dtoValidator.validate(subjectFormDto, "Invalid Body Request");
        Subject subject = subjectService.create(subjectFormDto.getCode(),subjectFormDto.getName());
        URI location = URI.create(uriInfo.getAbsolutePath() + "/" + subject.getSubjectId());
        return Response.created(location).build();
    }

    @GET
    @Path("/{subjectId}")
    @Produces("application/vnd.campus.api.v1+json")
    public Response getSubject(@PathParam("subjectId") Long subjectId) {
        Subject subject = subjectService.findById(subjectId).orElseThrow(SubjectNotFoundException::new);
        return Response.ok(assembler.toResource(subject)).build();
    }
}
