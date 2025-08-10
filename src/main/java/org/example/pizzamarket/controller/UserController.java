package org.example.pizzamarket.controller;

import org.example.pizzamarket.model.User;
import org.example.pizzamarket.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/{id}")
    public List<User> getUserById( @PathVariable Long id){
        return userService.findUserById(id);
    }

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }
    @DeleteMapping("/deleteUser")
    public void deleteUser(@RequestBody User user){
        userService.deleteUser(user.getId());
    }
}
