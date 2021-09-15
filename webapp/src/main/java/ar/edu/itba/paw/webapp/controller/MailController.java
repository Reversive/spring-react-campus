package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaces.CourseService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Announcement;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.auth.AuthFacade;
import ar.edu.itba.paw.webapp.auth.CampusUser;
import ar.edu.itba.paw.webapp.form.AnnouncementForm;
import ar.edu.itba.paw.webapp.form.MailForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MailController extends AuthController{

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/sendmail/{userId}", method = RequestMethod.GET)
    public ModelAndView sendmail(@PathVariable Integer userId, final MailForm mailForm,
                                 String successMessage) {
        User user = userService.findById(userId).orElseThrow(RuntimeException::new);
        ModelAndView mav = new ModelAndView("sendmail");
        mav.addObject("user", user);
        mav.addObject("mailForm",mailForm);
        mav.addObject("successMessage",successMessage);
        return mav;
    }

    @RequestMapping(value = "/sendmail/{userId}", method = RequestMethod.POST)
    public ModelAndView sendmail(@PathVariable Integer userId,
                                         @Valid MailForm mailForm, final BindingResult errors){
        String successMessage = null;
        if (!errors.hasErrors()) {
            mailForm.setSubject("");
            mailForm.setContent("");
            successMessage = "Email enviado exitosamente";
        }
        return sendmail(userId, mailForm, successMessage);
    }

}