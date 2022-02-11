package ar.edu.itba.paw.webapp.controllers;
import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.CampusPage;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.webapp.security.service.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.format.DateTimeFormatter;

@Controller
public class PortalController extends AuthController{

    private final CourseService courseService;
    protected final RoleService roleService;
    protected final SubjectService subjectService;
    protected final AnnouncementService announcementService;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private static final int ANNOUNCEMENT_SIZE = 5;


    @Autowired
    public PortalController(AuthFacade authFacade, CourseService courseService,
                            RoleService roleService, SubjectService subjectService, AnnouncementService announcementService) {
        super(authFacade);
        this.courseService = courseService;
        this.roleService = roleService;
        this.subjectService = subjectService;
        this.announcementService = announcementService;
    }

    @RequestMapping("/")
    public String rootRedirect() {
        if(Boolean.TRUE.equals(authFacade.getCurrentUser().isAdmin())) {
            return "redirect:/admin/portal";
        }
        return "redirect:/portal";
    }
    @Autowired
    AnswerService answerService;
    @RequestMapping("/portal")
    public ModelAndView portal(@RequestParam(value = "page", required = false, defaultValue = "1")
                                           Integer page,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10")
                                           Integer pageSize) {
        ModelAndView mav = new ModelAndView("portal");
        Long userId = authFacade.getCurrentUser().getUserId();
        CampusPage<Course> courses = courseService.list(userId, page, pageSize);
        mav.addObject("courseList", courses.getContent());
        mav.addObject("coursesAsStudent", courseService.listWhereStudent(userId));
        mav.addObject("currentCourses",courseService.listCurrent(userId));
        mav.addObject("currentPage", courses.getPage());
        mav.addObject("maxPage", courses.getTotal());
        mav.addObject("pageSize", courses.getSize());
        mav.addObject("announcements", announcementService.listByUser(userId,1,ANNOUNCEMENT_SIZE));
        mav.addObject("dateTimeFormatter",dateTimeFormatter);
        return mav;
    }

}