package com.waadsoft.testing.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(Integer userId);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    List<User> getUsers();

    List<User> getUsersById(Collection<Integer> ids);

    List<User> getUsersByEmail(Collection<String> emails);

    List<User> getUsersByUsername(Collection<String> usernames);

    void deleteUser(User user);

    void deleteUserById(Integer userId);

    void deleteUserByEmail(String email);

    void deleteUserByUsername(String username);

    void deleteUsersByEmail(Collection<String> emails);

    void deleteUsersById(Collection<Integer> ids);

    void deleteUsersByUsername(Collection<String> usernames);

    int countUsers();
}
