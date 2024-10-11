package com.example.samples.repositories;

import com.example.samples.models.Company;
import com.example.samples.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class DatabaseSeeder {

    // static db-seeding
    static {

        /*

        user:
        id=>id
        username=>username
        first_name=>first index of 'name' split with space
        last_name=>remaining of 'name' after first index of 'name' split with space
        email=>email
        address=>concatenate with space delimited, (address.street, address.suite, address.city, address.zipcode) with address.geo.lat+':'+address.geo.lng
        company_id=>company_id 'id' FK for 'company' table
        record_date=>current server date

        company:
        id=>system generated id
        name=>company.name
        info=>company.catchPhrase
        code=>company.bs
        record_date=>company.record_date

        {
            "id": 1, // todo: convert from int to long if required
            "name": "Leanne Graham", // todo: split into first & last names
            "username": "Bret",
            "email": "Sincere@april.biz",
            "address": { // todo: concatenate into address string
              "street": "Kulas Light",
              "suite": "Apt. 556",
              "city": "Gwenborough",
              "zipcode": "92998-3874",
              "geo": {
                "lat": "-37.3159",
                "lng": "81.1496"
              }
            },
            // todo: leave out next 2
            "phone": "1-770-736-8031 x56442",
            "website": "hildegard.org",

            "company": { // todo: seed Company Table
              "name": "Romaguera-Crona",
              "catchPhrase": "Multi-layered client-server neural-net",
              "bs": "harness real-time e-markets"
            }
        }

        */

        UserRepoImpl userRepo = new UserRepoImpl();
        CompanyRepoImpl companyRepo = new CompanyRepoImpl();

        seedDatabases( // * Function reference replaces callback: obj -> _Repo.save(obj)
                userRepo::save,
                companyRepo::save
        );

    }

    private static <T, U> void seedDatabases(
            Consumer<T> callback1,
            Consumer<U> callback2
    ) {
        try {
            HttpResponse<String> data = makeRequest("https://jsonplaceholder.typicode.com/users");
            if (data == null) throw new Exception("Seed data request failed");
            HashMap<T, U> objects = serialize(data);
            if (objects == null) throw new Exception("Data Serialization failed");
            // todo: confirm _this static context exec'd after h2-db setup in-build
            objects.forEach((user, company) -> {
                callback1.accept(user);
                callback2.accept(company);
            });
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

    private static <T, U> HashMap<T, U> serialize(HttpResponse<String> data) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            /** json-data & T-U (User-Company) class definitions do not 100%-match
            return List.of(
                    mapper.readValue(
                            data.body(),
                            T[].class
                            // TODO: can extend User model class with other props - phone, website, & (Company) companyData
                            // TODO: & prune these unnecessary props on database seed
                            // TODO: match Company id after companyRepo.save() with User company_id before userRepo.save()
                    )
            );
            */

            // parse manually
            JsonNode root = mapper.readTree(data.body());
            HashMap<User, Company> seedData = new HashMap<>();

            // * don't define dummy user/company objects outside loop
            // * need user-object's key reference as HashMap key (no unintentional object-reference mixups)
            String[] fullName; JsonNode address; JsonNode company;

            for (JsonNode node : root) {

                fullName = node.get("name").asText().split("\s");
                address = node.get("name");
                company = node.get("company");

                User user = new User(
                        node.get("username").asText(),
                        fullName[0],
                        fullName[1],
                        node.get("email").asText(),

                        // * building address string
                        "( "
                        + address.get("street").asText()
                        + ", " + address.get("suite").asText()
                        + ", " + address.get("city").asText()
                        + ", " + address.get("zipcode").asText()
                        + ", " + address.get("geo").get("lat").asText()
                        + ":" + address.get("geo").get("lng").asText()
                        + " )",

                        // TODO: Using .company.catchPhrase as pseudo - company foreign key
                        // * FunctionalInterface callback for extra logic & console-log
                        ((Function<JsonNode, Long>) (JsonNode comp) -> {
                            String catchPrase = comp.get("catchPhrase").asText();
                            System.out.println("Returning CatchPhrase as Long id -> " + catchPrase);
                            return Long.valueOf(catchPrase);
                            /**
                             * NOTE: optimal way to seed company data 1st, then assign all ids as users' foreign keys
                             */
                        }).apply(company),

                        Date.from(Instant.now())

                        /*
                        // todo: phone & website props left out
                        "phone": "1-770-736-8031 x56442",
                        "website": "hildegard.org",
                         */
                );

                Company companyValue = new Company(
                        company.get("name").asText(),
                        company.get("bs").asText(),
                        company.get("catchPrase").asText(),
                        Date.from(Instant.now())
                );

                seedData.put(user, companyValue); // * careful of company data-duplicates, as deep-populated props of each user-object json-node
                // todo: sub-optimal solution: handle all discrepancies in .save() methods, in _RepositoryImplementation classes
                // only save new object if it doesn't already exist in database
            }

            return (HashMap<T, U>) seedData; // * risky cast - should break if wrong/unexpected generic-method call
            // * <T - User, U - Company> - on specific generic seedDatabase(cb<T = User>, cb<U = Company>) call
            // so should be fine for now

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
