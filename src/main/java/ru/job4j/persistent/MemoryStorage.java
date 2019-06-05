package ru.job4j.persistent;

import ru.job4j.models.Person;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryStorage implements Repository<Person, Integer> {

    private List<Person> personStorage = new CopyOnWriteArrayList<>();
    private AtomicInteger personCount = new AtomicInteger(0);

    @Override
    public void persist(Person entity) {
        entity.setId(personCount.getAndIncrement());
        personStorage.add(entity);
    }

    @Override
    public void update(Person entity) {
        Optional<Person> candidate = personStorage.stream()
                .filter(person -> person.getId().equals(entity.getId())).findFirst();
        if (candidate.isPresent()) {
            Person person = candidate.get();
            person.setLogin(entity.getLogin());
            person.setPassword(entity.getPassword());
            person.setFirstName(entity.getFirstName());
            person.setEmail(entity.getEmail());
            person.setPhoneNumber(entity.getPhoneNumber());
        }
    }

    @Override
    public Optional<Person> findById(Integer id) {
        return personStorage.stream().filter(person -> person.getId().equals(id)).findFirst();
    }

    @Override
    public void delete(Person entity) {
        personStorage.remove(entity);
    }

    @Override
    public List<Person> findAll() {
        return personStorage;
    }
}
