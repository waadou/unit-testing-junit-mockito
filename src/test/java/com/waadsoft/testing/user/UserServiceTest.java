package com.waadsoft.testing.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldAddUser() {
        User user = givenUserAlaza();
        when(userRepository.add(user)).thenReturn(user);

        User addedUser = userService.addUser(user);

        assertEqualUser(user, addedUser);
        verify(userRepository, atMostOnce()).add(user);
    }

    @Test
    void shouldUpdateUser() {
        User user = givenUserAlaza();
        when(userRepository.update(user)).thenReturn(user);
        User updatedUser = userService.updateUser(user);

        assertEqualUser(updatedUser, user);
        verify(userRepository, atMostOnce()).update(user);
    }

    @Test
    void shouldGetUserById() {
        Integer userId = 1;
        Optional<User> expectedUser = Optional.of(givenUserAlaza());
        when(userRepository.findById(userId)).thenReturn(expectedUser);

        Optional<User> actualUser = userService.getUserById(userId);

        assertEqualUser(expectedUser, actualUser);
        verify(userRepository, atMostOnce()).findById(userId);
    }

    @Test
    void shouldGetUserByEmail() {
        String email = "alaza@example.com";
        Optional<User> expectedUser = Optional.of(givenUserAlaza());
        when(userRepository.findByEmail(email)).thenReturn(expectedUser);

        Optional<User> actualUser = userService.getUserByEmail(email);

        assertEqualUser(expectedUser, actualUser);
        verify(userRepository, atMostOnce()).findByEmail(email);
    }

    @Test
    void shouldGetUserByUsername() {
        String username = "alaza";
        Optional<User> expectedUser = Optional.of(givenUserAlaza());
        when(userRepository.findByUsername(username)).thenReturn(expectedUser);

        Optional<User> actualUser = userService.getUserByUsername(username);

        assertEqualUser(expectedUser, actualUser);
        verify(userRepository, atMostOnce()).findByUsername(username);
    }

    @Test
    void shouldGetUsers() {
        List<User> expectedUsers = Arrays.asList(givenUserAlaza(), givenUserAminou());
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsers();

        assertEqualUsers(expectedUsers, actualUsers);
        verify(userRepository, atMostOnce()).findAll();
    }

    @Test
    void shouldGetUsersById() {
        List<Integer> ids = List.of(1, 2);
        List<User> expectedUsers = Arrays.asList(givenUserAlaza(), givenUserAminou());
        when(userRepository.findAllById(ids)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsersById(ids);

        assertEqualUsers(expectedUsers, actualUsers);
        verify(userRepository, atMostOnce()).findAllById(ids);
    }

    @Test
    void shouldGetUsersByEmail() {
        List<String> emails = List.of("alaza@example.com", "bako@example.com");
        List<User> expectedUsers = Arrays.asList(givenUserAlaza(), givenUserAminou());
        when(userRepository.findAllByEmail(emails)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsersByEmail(emails);

        assertEqualUsers(expectedUsers, actualUsers);
        verify(userRepository, atMostOnce()).findAllByEmail(emails);
    }

    @Test
    void getUsersByUsername() {
        List<String> usernames = List.of("alaza", "bako");
        List<User> expectedUsers = Arrays.asList(givenUserAlaza(), givenUserAminou());
        when(userRepository.findAllByUsername(usernames)).thenReturn(expectedUsers);

        List<User> actualUsers = userService.getUsersByUsername(usernames);

        assertEqualUsers(expectedUsers, actualUsers);
        verify(userRepository, atMostOnce()).findAllByUsername(usernames);
    }

    @Test
    void deleteUser() {
        User user = givenUserBako();
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(user);

        verify(userRepository, atMostOnce()).delete(user);
    }

    @Test
    void deleteUserById() {
        Integer userId = 1;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUserById(userId);

        verify(userRepository, atMostOnce()).deleteById(userId);
    }

    @Test
    void deleteUserByEmail() {
        String email = "alaza@example.com";
        doNothing().when(userRepository).deleteByEmail(email);

        userService.deleteUserByEmail(email);

        verify(userRepository, atMostOnce()).deleteByEmail(email);
    }

    @Test
    void deleteUserByUsername() {
        String username = "alaza";
        doNothing().when(userRepository).deleteByUsername(username);

        userService.deleteUserByUsername(username);

        verify(userRepository, atMostOnce()).deleteByUsername(username);
    }

    @Test
    void deleteUsersByEmail() {
        List<String> emails = List.of("alaza@example.com", "bako@example.com");
        doNothing().when(userRepository).deleteAllByEmail(emails);

        userService.deleteUsersByEmail(emails);

        verify(userRepository, atMostOnce()).deleteAllByEmail(emails);
    }

    @Test
    void deleteUsersById() {
        List<Integer> ids = List.of(1, 2);
        doNothing().when(userRepository).deleteAllById(ids);

        userService.deleteUsersById(ids);

        verify(userRepository, atMostOnce()).deleteAllById(ids);
    }

    @Test
    void deleteUsersByUsername() {
        List<String> usernames = List.of("alaza", "bako");
        doNothing().when(userRepository).deleteAllByUsername(usernames);

        userService.deleteUsersByUsername(usernames);

        verify(userRepository, atMostOnce()).deleteAllByUsername(usernames);
    }

    @Test
    void countUsers() {
        int expected = 2;
        when(userRepository.count()).thenReturn(expected);

        int actual = userService.countUsers();

        assertThat(actual).isEqualTo(expected);
        verify(userRepository, atMostOnce()).count();
    }

    private void assertEqualUser(User expectedUser, User actualUser) {
        assertThat(actualUser).isNotNull()
                .isEqualTo(expectedUser);

        assertThat(actualUser.getEmail()).isEqualTo(expectedUser.getEmail());
        assertThat(actualUser.getUsername()).isEqualTo(expectedUser.getUsername());
    }

    private void assertEqualUser(Optional<User> expectedUser, Optional<User> actualUser) {
        assertThat(actualUser).isNotEmpty()
                .isEqualTo(expectedUser);

        assertThat(actualUser.get().getEmail()).isEqualTo(expectedUser.get().getEmail());
        assertThat(actualUser.get().getUsername()).isEqualTo(expectedUser.get().getUsername());
    }

    private void assertEqualUsers(List<User> expectedUsers, List<User> actualUsers) {
        assertThat(actualUsers).isNotEmpty();
        assertThat(actualUsers.size()).isEqualTo(expectedUsers.size());

        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEqualUser(expectedUsers.get(i), actualUsers.get(i));
        }
    }


    private User givenUserAlaza() {
        return new User("alaza", "alaza@alaza.com");
    }

    private User givenUserBako() {
        return new User("bako", "bako@bako.com");
    }

    private User givenUserAminou() {
        return new User("aminou", "aminou@aminou.com");
    }
}