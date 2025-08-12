package org.example.pizzamarket.service;



import org.example.pizzamarket.model.User;
import org.example.pizzamarket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneNumberUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PhoneNumberUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_ExistingUser_ReturnsUserDetails() {

        User testUser = new User();
        testUser.setPhoneNumber("+123456789");
        testUser.setPassword("encodedPassword");
        testUser.setRole(User.Role.USER);
        testUser.setEnabled(true);


        when(userRepository.findByPhoneNumber("+123456789")).thenReturn(Optional.of(testUser));


        UserDetails userDetails = userDetailsService.loadUserByUsername("+123456789");


        assertNotNull(userDetails);
        assertEquals("+123456789", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        verify(userRepository, times(1)).findByPhoneNumber("+123456789");
    }
    @Test
    void loadUserByUsername_NonExistingUser_ThrowsException() {

        when(userRepository.findByPhoneNumber("+000000000")).thenReturn(Optional.empty());


        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("+000000000")
        );


        verify(userRepository, times(1)).findByPhoneNumber("+000000000");
    }
}