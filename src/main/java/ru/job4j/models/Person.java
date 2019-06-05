package ru.job4j.models;

import java.time.LocalDate;
import java.util.Objects;

public class Person {
    private Integer id;

    private String login;

    private String password;

    private String firstName;

    private String email;

    private String phoneNumber;

    private LocalDate createDate;

    public Person() {
    }

    public Person(String login, String password, String firstName, String email, String phoneNumber, LocalDate createDate) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createDate = createDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return Objects.equals(login, person.login)
                && Objects.equals(password, person.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
