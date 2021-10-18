package ar.edu.itba.paw.models;


import javax.persistence.*;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subjects_subjectid_seq")
    @SequenceGenerator(name="subjects_subjectid_seq", sequenceName = "subjects_subjectid_seq", allocationSize = 1)
    private Long subjectId;

    @Column
    private String code;

    @Column
    private String name;

    /* Defaul */ Subject(){
        // Just for Hibernate
    }
    public Subject(Long subjectId, String code, String name) {
        this.subjectId = subjectId;
        this.code = code;
        this.name = name;
    }
    public Subject(String code, String name) {
        this.subjectId = subjectId;
        this.code = code;
        this.name = name;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
