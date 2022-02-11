package ar.edu.itba.paw.webapp.dto;

public class UserCourseDto {

    private CourseDto course;
    private RoleDto role;

    public UserCourseDto() {
        // For MessageBodyWriter
    }

    public UserCourseDto(CourseDto course, RoleDto role) {
        this.course = course;
        this.role = role;
    }

    public CourseDto getCourse() {
        return course;
    }

    public void setCourse(CourseDto course) {
        this.course = course;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }
}
