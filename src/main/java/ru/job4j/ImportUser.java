package ru.job4j;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import ru.job4j.models.Person;
import ru.job4j.persistent.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ImportUser {

    private static final Logger LOG = LogManager.getLogger(ImportUser.class);
    private Repository<Person, Integer> repository;

    public ImportUser(Repository<Person, Integer> repository) {
        this.repository = repository;
    }

    public void addPerson(Person person) {
        repository.persist(person);
    }

    public Person getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public static void main(String[] args) {
        ApplicationContext context = new GenericXmlApplicationContext("spring-context.xml");
        ImportUser importUser = context.getBean(ImportUser.class);
        boolean flagContinue = false;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (!flagContinue) {
            LOG.info("Pleas input data for register user: login, password, first name, email and phone number");
            LOG.info("If you won't register input 'exit'");

            try {
                String consoleInput = reader.readLine();

                if (consoleInput.equals("exit")) {
                    flagContinue = true;
                    break;
                }
                String[] userData = consoleInput.split(" ");

                if (userData.length == 5) {
                    importUser.addPerson(new Person(userData[0], userData[1], userData[2],
                            userData[3], userData[4], LocalDate.now()));
                    LOG.info("User is added");
                } else {
                    LOG.info("Invalid data, pleas repeat input");
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        LOG.info("Goodbye");
    }
}
