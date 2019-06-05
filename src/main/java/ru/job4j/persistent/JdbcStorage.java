package ru.job4j.persistent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import ru.job4j.models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class JdbcStorage implements Repository<Person, Integer> {

    private static final Logger LOG = LogManager.getLogger(JdbcStorage.class);

    // language=sql
    private final String persistPersonSQL =
            "INSERT INTO person (login, password, first_name, email, phone_number, create_user_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    // language=sql
    private final String updatePersonSQL = "UPDATE person SET login = ?, password = ?, email = ?, phone_number = ? " +
            "WHERE  person_id = ?";

    // language=sql
    private final String findPersonByIdSQL = "SELECT * FROM person WHERE person_id = ?";

    // language=sql
    private final String deletePersonSQL = "DELETE FROM person WHERE person_id = ?";

    // language=sql
    private final String findAllPersonSQL = "SELECT * FROM person";

    private Connection connection;

    public JdbcStorage(String driverClassName, String url, String username, String password) {
        try {
            Class.forName(driverClassName);
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void persist(Person entity) {
        try (PreparedStatement st = this.connection.prepareStatement(this.persistPersonSQL)) {
            st.setString(1, entity.getLogin());
            st.setString(2, entity.getPassword());
            st.setString(3, entity.getFirstName());
            st.setString(4, entity.getEmail());
            st.setString(5, entity.getPhoneNumber());
            st.setTimestamp(6, Timestamp.valueOf(entity.getCreateDate().atStartOfDay()));
            try (ResultSet rs = st.getGeneratedKeys();){
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
            st.execute();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void update(Person entity) {
        try (PreparedStatement ps = this.connection.prepareStatement(this.updatePersonSQL)) {
            ps.setString(1, entity.getLogin());
            ps.setString(2, entity.getPassword());
            ps.setString(3, entity.getEmail());
            ps.setString(4, entity.getPhoneNumber());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Optional<Person> findById(Integer id) {
        Optional<Person> candidate = Optional.empty();
        try (PreparedStatement ps = this.connection.prepareStatement(this.findPersonByIdSQL)){
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    candidate = Optional.of(this.createPerson(rs));
                }
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return candidate;
    }

    @Override
    public void delete(Person entity) {
        try (PreparedStatement ps = this.connection.prepareStatement(this.deletePersonSQL)) {
            ps.setInt(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public List<Person> findAll() {
        List<Person> allPerson = new ArrayList<>();
        try (PreparedStatement ps = this.connection.prepareStatement(this.findAllPersonSQL)){
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allPerson.add(this.createPerson(rs));
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return allPerson;
    }

    private Person createPerson(ResultSet rs) {
        var person = new Person();
        try {
            person.setId(rs.getInt("person_id"));
            person.setLogin(rs.getString("login"));
            person.setPassword(rs.getString("password"));
            person.setFirstName(rs.getString("first_name"));
            person.setEmail(rs.getString("email"));
            person.setPhoneNumber(rs.getString("phone_number"));
            person.setCreateDate(rs.getDate("create_user_date").toLocalDate());
        } catch (SQLException e) {
            LOG.error(e.getMessage(), e);
        }
        return person;
    }



}
