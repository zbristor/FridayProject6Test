package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by student on 6/28/17.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAllByUsername(String username);

    User findByUsername(String username);
    User findByEmail(String email);
    Long countByEmail(String email);
    Long countByUsername(String username);
}
