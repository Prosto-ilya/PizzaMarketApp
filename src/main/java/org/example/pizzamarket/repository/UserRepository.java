package org.example.pizzamarket.repository;

import org.example.pizzamarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findUserById(Long id);
    List<User> deleteUserById(Long id);
}
