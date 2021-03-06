package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CourseDao {
    Course create(Integer year, Integer quarter, String board, Long subjectId);

    boolean update(Long id, Course course);

    boolean delete(Long id);

    List<Course> list();

    CampusPage<Course> list(Long userId, CampusPageRequest pageRequest);

    CampusPage<Course> list(CampusPageRequest pageRequest);

    Role getUserRoleInCourse(Long courseId, Long userId);

    List<Course> listCurrent(Long userId);

    Optional<Course> findById(Long id);

    List<User> getStudents(Long courseId);

    Map<User, Role> getPrivilegedUsers(Long courseId);

    List<User> getTeachers(Long courseId);

    List<User> getHelpers(Long courseId);

    boolean belongs(Long userId, Long courseId);

    boolean enroll(Long userId, Long courseId, Integer roleId);

    List<User> listUnenrolledUsers(Long courseId);

    List<Course> listWhereStudent(Long userId);

    CampusPage<Course> listByYearQuarter(Integer year, Integer quarter, CampusPageRequest campusPageRequest);

    List<Integer> getAvailableYears();

    boolean exists(Integer year, Integer quarter, String board, Long subjectId);

    Long getTotalStudents(Long courseId);
}
