package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.models.User;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CourseForm {

    @Min(0)
    @Max(2147483647) // Value of the max integer in postgresql
    @NotNull
    private Long subjectId;

    @Min(1)
    @Max(2)
    @NotNull
    private Integer quarter;

    @NotNull
    @NotBlank
    @Length(max=8)
    private String board;

    @Min(0)
    @NotNull
    private Integer year;

    @NotEmpty
    private List<Integer> teacherIds;

    @NotEmpty
    private List<Integer> studentIds;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<Integer> getTeacherIds() {
        return teacherIds;
    }

    public void setTeacherIds(List<Integer> teacherIds) {
        this.teacherIds = teacherIds;
    }

    public List<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<Integer> studentIds) {
        this.studentIds = studentIds;
    }
}