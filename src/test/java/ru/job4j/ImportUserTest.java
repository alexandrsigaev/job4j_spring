package ru.job4j;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import ru.job4j.models.Person;

import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class ImportUserTest {

    @Test
    public void addPerson() {
        ApplicationContext context = new GenericXmlApplicationContext("spring-context.xml");
        ImportUser importUser = context.getBean(ImportUser.class);
        var person = new Person("login", "passwrd", "name"
                , "mail@mail.ru", "199858509888", LocalDate.now());
        importUser.addPerson(person);
        assertThat(person, is(importUser.getById(person.getId())));
    }
}