package org.example.pizzamarket.service;

import org.example.pizzamarket.model.User;
import org.example.pizzamarket.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findUserById(Long id){
        return userRepository.findUserById(id);
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public List<User> deleteUser(Long id){
        return userRepository.deleteUserById(id);
    }
}
