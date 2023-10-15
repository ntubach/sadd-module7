package org.ntubach.module7;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Model for Student object
public class Student {

    private Integer id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;

    Student(int id, String firstname, String lastname, LocalDate dateOfBirth, String email) {
        this.id = id;
        this.firstName = firstname;
        this.lastName = lastname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        System.out.println("Call Student constructor with args");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth.toString();
    }

    public Boolean setDateOfBirth(String dateOfBirth) {
        try {
            this.dateOfBirth = LocalDate.parse(dateOfBirth);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmail() {
        return email;
    }

    public Boolean setEmail(String email) {
        if (validateEmail(email)) {
            this.email = email;
            return true;
        } else
            return false;
    }

    public boolean validateEmail(String email) {
        if (email == null) {
            return false;
        }

        return email.matches("^[a-zA-Z0-9_\\.]+@[a-zA-Z0-9_\\.]+\\.[a-zA-Z]{2,}$");
    }
}
