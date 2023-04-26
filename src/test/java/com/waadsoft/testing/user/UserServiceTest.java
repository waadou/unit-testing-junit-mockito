package com.waadsoft.testing.user;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceTest extends BaseTestCase {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    User bako = new User("bako", "bako@example.com");
    User alaza = new User("alaza", "alaza@example.com");
    User aminou = new User("aminou", "aminou@example.com");
    private Optional<User> optAlaza = Optional.of(alaza);
    private List<User> users = List.of(alaza, bako);

    @Test
    void shouldAddUser() {
        when(userRepository.add(alaza)).thenReturn(alaza);
        User user = userService.addUser(alaza);

        assertEqualUser(alaza, user);
        verify(userRepository, times(1)).add(alaza);
    }

    @Test
    void shouldUpdateUser() {
        when(userRepository.update(alaza)).thenReturn(alaza);
        User user = userService.updateUser(alaza);

        assertEqualUser(alaza, user);
        verify(userRepository, times(1)).update(alaza);
    }

    @Test
    void shouldGetUserById() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(optAlaza);
        Optional<User> found = userService.getUserById(userId);

        assertEqualUser(optAlaza, found);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldGetUserByEmail() {
        String email = "alaza@example.com";
        when(userRepository.findByEmail(email)).thenReturn(optAlaza);
        Optional<User> found = userService.getUserByEmail(email);

        assertEqualUser(optAlaza, found);
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void shouldGetUserByUsername() {
        String username = "alaza";
        when(userRepository.findByUsername(username)).thenReturn(optAlaza);
        Optional<User> found = userService.getUserByUsername(username);

        assertEqualUser(optAlaza, found);
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void shouldGetUsers() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> actualUsers = userService.getUsers();

        assertEqualUsers(users, actualUsers);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldGetUsersById() {
        List<Integer> ids = List.of(1, 2);
        when(userRepository.findAllById(ids)).thenReturn(users);
        List<User> actualUsers = userService.getUsersById(ids);

        assertEqualUsers(users, actualUsers);
        verify(userRepository, times(1)).findAllById(ids);
    }

    @Test
    void shouldGetUsersByEmail() {
        List<String> emails = List.of("alaza@example.com", "bako@example.com");
        when(userRepository.findAllByEmail(emails)).thenReturn(users);
        List<User> actualUsers = userService.getUsersByEmail(emails);

        assertEqualUsers(users, actualUsers);
        verify(userRepository, times(1)).findAllByEmail(emails);
    }

    @Test
    void getUsersByUsername() {
        List<String> usernames = List.of("alaza", "bako");
        when(userRepository.findAllByUsername(usernames)).thenReturn(users);
        List<User> actualUsers = userService.getUsersByUsername(usernames);

        assertEqualUsers(users, actualUsers);
        verify(userRepository, times(1)).findAllByUsername(usernames);
    }

    @Test
    void deleteUser() {
        doNothing().when(userRepository).delete(alaza);
        userService.deleteUser(alaza);

        verify(userRepository, times(1)).delete(alaza);
    }

    @Test
    void deleteUserById() {
        Integer userId = 1;
        doNothing().when(userRepository).deleteById(userId);
        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteUserByEmail() {
        String email = "alaza@example.com";
        doNothing().when(userRepository).deleteByEmail(email);
        userService.deleteUserByEmail(email);

        verify(userRepository, times(1)).deleteByEmail(email);
    }

    @Test
    void deleteUserByUsername() {
        String username = "alaza";
        doNothing().when(userRepository).deleteByUsername(username);
        userService.deleteUserByUsername(username);

        verify(userRepository, times(1)).deleteByUsername(username);
    }

    @Test
    void deleteUsersByEmail() {
        List<String> emails = List.of("alaza@example.com", "bako@example.com");
        doNothing().when(userRepository).deleteAllByEmail(emails);
        userService.deleteUsersByEmail(emails);

        verify(userRepository, times(1)).deleteAllByEmail(emails);
    }

    @Test
    void deleteUsersById() {
        List<Integer> ids = List.of(1, 2);
        doNothing().when(userRepository).deleteAllById(ids);
        userService.deleteUsersById(ids);

        verify(userRepository, times(1)).deleteAllById(ids);
    }

    @Test
    void deleteUsersByUsername() {
        List<String> usernames = List.of("alaza", "bako");
        doNothing().when(userRepository).deleteAllByUsername(usernames);
        userService.deleteUsersByUsername(usernames);

        verify(userRepository, times(1)).deleteAllByUsername(usernames);
    }

    @Test
    void countUsers() {
        when(userRepository.count()).thenReturn(2);

        int countUsers = userService.countUsers();

        assertThat(countUsers).isEqualTo(2);
        verify(userRepository, times(1)).count();
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
        assertThat(actualUsers).isNotEmpty()
                .hasSize(2)
                .isEqualTo(expectedUsers);

        assertEqualUser(expectedUsers.get(0), actualUsers.get(0));
        assertEqualUser(expectedUsers.get(1), actualUsers.get(1));
    }
}