package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.constraint.annotation.MaxFileSize;
import ar.edu.itba.paw.webapp.constraint.annotation.NotEmptyFile;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ExamFormDto {

    @Size(min=2,max=50)
    private String title;

    @Size(min=2,max=255)
    private String content;

    @NotEmptyFile
    @MaxFileSize(50)
    private CommonsMultipartFile file;

    @NotNull
    @NotBlank
    private String startTime;

    @NotNull
    @NotBlank
    private String endTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommonsMultipartFile getFile() {
        return file;
    }

    public void setFile(CommonsMultipartFile file) {
        this.file = file;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}