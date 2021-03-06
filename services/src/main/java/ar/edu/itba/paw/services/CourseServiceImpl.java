package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.CourseNotFoundException;
import ar.edu.itba.paw.models.exception.DuplicateCourseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalTime;
import java.util.*;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private  UserDao userDao;
    @Autowired
    private TimetableService timetableService;

    final String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    @Transactional
    @Override
    public Course create(Integer year, Integer quarter, String board, Long subjectId, List<Integer> startTimes,
                                                     List<Integer> endTimes) {
        if(courseDao.exists(year, quarter, board, subjectId))
            throw new DuplicateCourseException.Builder()
                .withYear(year)
                .withQuarter(quarter)
                .withBoard(board)
                .withSubjectId(subjectId)
                .build();

        Course course = courseDao.create(year, quarter, board, subjectId);
        for (int i = 0; i < days.length; i++) {
            Integer startHour = startTimes.get(i);
            Integer endHour = endTimes.get(i);
            if (startHour != null && endHour != null) {
                timetableService.create(course, i, LocalTime.of(startHour, 0, 0),
                        LocalTime.of(endHour, 0, 0));
            }
        }
        return course;
    }

    @Transactional
    @Override
    public boolean update(Long id, Integer year, Integer quarter, String board, Long subjectId, List<Integer> startTimes,
                         List<Integer> endTimes) {

        Course course = findById(id).orElseThrow(CourseNotFoundException::new);
        course.setQuarter(quarter);
        course.setYear(year);
        course.setBoard(board);
        course.setSubject(course.getSubject());
        for (int i = 0; i < days.length; i++) {
            Integer startHour = startTimes.get(i);
            Integer endHour = endTimes.get(i);
            if (startHour != null && endHour != null) {
                timetableService.update(id, i, LocalTime.of(startHour, 0, 0),
                        LocalTime.of(endHour, 0, 0));
            }
        }
        return update(id, course);
    }

    @Transactional
    @Override
    public boolean update(Long id, Course course) {
        return courseDao.update(id, course);
    }

    @Transactional
    @Override
    public boolean delete(Long id) {
        return courseDao.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Course> list() {
        return courseDao.list();
    }

    @Transactional(readOnly = true)
    @Override
    public CampusPage<Course> list(Long userId, Integer page, Integer pageSize) {
        return courseDao.list(userId, new CampusPageRequest(page, pageSize));
    }

    @Override
    public CampusPage<Course> list(Integer page, Integer pageSize) {
        return courseDao.list(new CampusPageRequest(page, pageSize));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Course> listCurrent(Long userId) {
        return courseDao.listCurrent(userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Course> findById(Long id) {
        return courseDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<User, Role> getPrivilegedUsers(Long courseId) {
        return courseDao.getPrivilegedUsers(courseId);
    }

    @Override
    public List<User> getTeachers(Long courseId) {
        return courseDao.getTeachers(courseId);
    }

    @Override
    public List<User> getHelpers(Long courseId) {
        return courseDao.getHelpers(courseId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getStudents(Long courseId) {
        return courseDao.getStudents(courseId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean belongs(Long userId, Long courseId) {
        return courseDao.belongs(userId, courseId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isPrivileged(Long userId, Long courseId) {
        Optional<Role> userRole = userDao.getRole(userId, courseId);
        if(!userRole.isPresent()) return false;
        int roleId = userRole.get().getRoleId();
        return roleId == Permissions.TEACHER.getValue() || roleId == Permissions.HELPER.getValue();
    }

    @Transactional
    @Override
    public void enroll(Long userId, Long courseId, Integer roleId) {
        courseDao.enroll(userId, courseId, roleId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUnenrolledUsers(Long courseId) {
        return courseDao.listUnenrolledUsers(courseId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Course> listWhereStudent(Long userId) {
        return courseDao.listWhereStudent(userId);
    }

    @Override
    public Role getUserRoleInCourse(Long courseId, Long userId) {
        return courseDao.getUserRoleInCourse(courseId, userId);
    }

    @Transactional(readOnly = true)
    @Override
    public CampusPage<Course> listByYearQuarter(Integer year, Integer quarter, Integer page, Integer pageSize) {
        return courseDao.listByYearQuarter(year, quarter, new CampusPageRequest(page, pageSize));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Integer> getAvailableYears() {
        return courseDao.getAvailableYears();
    }

}
