package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;

public class UserRegisterForm {

    @Min(0)
    @Max(2147483647) // Value of the max integer in postgresql
    @NotNull
    private int fileNumber;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z ]+") // Must have at leat one caracter and only letters
    private String name;

    @NotBlank
    @Pattern(regexp = "[a-zA-Z ]+") // Must have at leat one caracter and only letters
    private String surname;

    @NotBlank
    @Size(min = 6, max = 50)
    @Pattern(regexp = "[a-zA-Z]+")
    private String username;

    @NotBlank
    @Email(regexp = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$")
    // source: https://www.w3resource.com/javascript/form/email-validation.php
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
    //Minimum eight characters, at least one uppercase letter, one lowercase letter and one number (sourc: https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a )
    private String password;

    @NotBlank
    @NotNull(message = "not match")
    // if passwords do not match, confirmation is forced into a null value
    private String confirmPassword;

    public UserRegisterForm() {}

    public static class Builder {

        private Integer fileNumber;
        private String name;
        private String surname;
        private String username;
        private String email;

        public Builder() {
        }

        Builder(Integer fileNumber, String name, String surname, String username, String email) {
            this.fileNumber = fileNumber;
            this.name = name;
            this.surname = surname;
            this.username = username;
            this.email = email;
        }

        public Builder withFileNumber(Integer fileNumber){
            this.fileNumber = fileNumber;
            return Builder.this;
        }

        public Builder withName(String name){
            this.name = name;
            return Builder.this;
        }

        public Builder withSurname(String surname){
            this.surname = surname;
            return Builder.this;
        }

        public Builder withUsername(String username){
            this.username = username;
            return Builder.this;
        }

        public Builder withEmail(String email){
            this.email = email;
            return Builder.this;
        }

        public UserRegisterForm build() {
            return new UserRegisterForm(this);
        }
    }

    private UserRegisterForm(Builder builder) {
        this.fileNumber = builder.fileNumber;
        this.name = builder.name;
        this.surname = builder.surname;
        this.username = builder.username;
        this.email = builder.email;
    }

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
        checkPasswordValidity();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        checkPasswordValidity();
    }

    private void checkPasswordValidity() {
        if(this.password != null && !this.password.equals(confirmPassword)){
            this.confirmPassword = null;
        }
    }
}
