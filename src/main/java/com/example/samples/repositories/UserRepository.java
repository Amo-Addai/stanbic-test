package com.example.samples.repositories;

import com.example.samples.models.User;

import java.util.List;

public interface UserRepository {

    User save(User user);
    User findById(Long id);
    List<User> findAll();

}
