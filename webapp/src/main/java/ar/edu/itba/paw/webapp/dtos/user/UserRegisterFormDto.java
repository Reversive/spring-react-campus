package ar.edu.itba.paw.webapp.dtos.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

public class UserRegisterFormDto {

    @Min(0)
    @Max(2147483647) // Value of the max integer in postgresql
    @NotNull
    private int fileNumber;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z ']+") // Must have at least one character and only letters
    private String name;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z ']+") // Must have at least one character and only letters
    private String surname;

    @NotBlank
    @Size(min = 6, max = 50)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;

    @NotBlank
    @Email(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    // source: https://www.w3resource.com/javascript/form/email-validation.php
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
    @Size(min = 8, max = 50)
    private String password;

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
