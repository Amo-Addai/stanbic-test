package com.example.samples.repositories;

import com.example.samples.models.Login;
import com.example.samples.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Consumer;

@Repository
public class UserRepoImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // static db-seeding
    static {

        /*

        user:
        id=>id
        username=>username
        first_name=>first index of 'name' split with space
        last_name=>remaining of 'name' after first index of 'name' split with space
        email=>email
        address=>concatenate with space delimitted, (address.street, address.suite, address.city, address.zipcode) with address.geo.lat+':'+address.geo.lng
        company_id=>company_id 'id' FK for 'company' table
        record_date=>current server date

        {
            "id": 1,
            "name": "Leanne Graham",
            "username": "Bret",
            "email": "Sincere@april.biz",
            "address": {
              "street": "Kulas Light",
              "suite": "Apt. 556",
              "city": "Gwenborough",
              "zipcode": "92998-3874",
              "geo": {
                "lat": "-37.3159",
                "lng": "81.1496"
              }
            },
            "phone": "1-770-736-8031 x56442",
            "website": "hildegard.org",
            "company": {
              "name": "Romaguera-Crona",
              "catchPhrase": "Multi-layered client-server neural-net",
              "bs": "harness real-time e-markets"
            }
        }

        */

        UserRepoImpl _this = new UserRepoImpl();
        seedDatabases(_this::save); // * Function reference replaces callback: user -> _this.save(user)
    }

    private static void seedDatabases(Consumer<User> callback) {
        try {
            HttpResponse<String> data = makeRequest("https://jsonplaceholder.typicode.com/users");
            if (data == null) throw new Exception("Seed data request failed");
            List<User> users = serialize(data);
            if (users == null) throw new Exception("User Serialization failed");
            users.forEach(u -> callback.accept(u)); // todo: confirm _this static context exec'd after h2-db setup in-build
        } catch (Exception e) {
            System.out.println("Error making seed-data request");
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    private static HttpResponse<String> makeRequest(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request =
                    HttpRequest.newBuilder()
                               .uri(URI.create(url))
                               .GET()
                               .build();
            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers
                                        .ofString()
                    );
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<User> serialize(HttpResponse<String> data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return List.of(
                    mapper.readValue(
                            data.body(),
                            User[].class
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional
    public Boolean auth(Login login) { // EntityManager auth
        String jpql = "SELECT COUNT(u) FROM User u WHERE u.username = :username AND u.email = :email";
        Long count = entityManager.createQuery(jpql, Long.class)
                                  .setParameter("username", login.username)
                                  .setParameter("email", login.email)
                                  .getSingleResult();
        return count > 0;
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user != null) {
            entityManager.persist(user); // Insert new entity
        } else {
            entityManager.merge(user);   // Update existing entity
        }
        return user;
    }

    @Override
    @Transactional
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public List<User> findAll() {
        return entityManager.createQuery("from User", User.class).getResultList();
    }

}
