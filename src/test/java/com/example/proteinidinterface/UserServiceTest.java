package com.example.proteinidinterface;

import com.example.proteinidinterface.dto.CredentialsDto;
import com.example.proteinidinterface.dto.SignUpDto;
import com.example.proteinidinterface.dto.UserDto;
import com.example.proteinidinterface.exception.AppException;
import com.example.proteinidinterface.model.User;
import com.example.proteinidinterface.repository.UserRepository;
import com.example.proteinidinterface.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }
    @Test
    public void whenValidUsernameThenUserShouldBeFound() {
        String username = "testUser";
        User user = new User(username, "email","password");

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername(username);

        assertEquals(username, foundUser.getUsername());
    }

    @Test
    public void whenValidLoginCredentialsThenUserShouldBeLoggedIn() {
        String username = "testUser";
        String password = "password";
        User user = new User(username, "email",password);

        Mockito.when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(CharBuffer.wrap(password), user.getPassword())).thenReturn(true);

        UserDto loggedInUser = userService.login(new CredentialsDto(username, password.toCharArray()));

        assertEquals(username, loggedInUser.getUsername());
    }

    @Test(expected = AppException.class)
    public void whenUsernameExists_thenAppExceptionShouldBeThrown() {
        SignUpDto signUpDto = new SignUpDto("existingUser", "email@example.com", "password".toCharArray());
        User existingUser = new User("existingUser", "existingEmail@example.com","password");

        Mockito.when(userRepository.findByUsername(signUpDto.username())).thenReturn(Optional.of(existingUser));

        userService.register(signUpDto);
    }

    @Test(expected = AppException.class)
    public void whenEmailExists_thenAppExceptionShouldBeThrown() {
        SignUpDto signUpDto = new SignUpDto("newUser", "existingEmail@example.com", "password".toCharArray());
        User existingUser = new User("existingUser", "existingEmail@example.com","password");

        Mockito.when(userRepository.findByEmail(signUpDto.email())).thenReturn(Optional.of(existingUser));

        userService.register(signUpDto);
    }

    @Test
    public void whenUsernameAndEmailAreUnique_thenNewUserShouldBeRegistered() {
        SignUpDto signUpDto = new SignUpDto("newUser", "newEmail@example.com", "password".toCharArray());
        User newUser = new User("newUser","email" ,"encodedPassword");

        Mockito.when(userRepository.findByUsername(signUpDto.username())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(signUpDto.email())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(CharBuffer.wrap(signUpDto.password()))).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserDto registeredUser = userService.register(signUpDto);

        assertEquals(newUser.getUsername(), registeredUser.getUsername());
        assertEquals(newUser.getEmail(), registeredUser.getEmail());
    }
}
