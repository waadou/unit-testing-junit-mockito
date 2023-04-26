package com.waadsoft.testing.user;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return userRepository.add(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.update(user);
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersById(Collection<Integer> ids) {
        return userRepository.findAllById(ids);
    }

    @Override
    public List<User> getUsersByEmail(Collection<String> emails) {
        return userRepository.findAllByEmail(emails);
    }

    @Override
    public List<User> getUsersByUsername(Collection<String> usernames) {
        return userRepository.findAllByUsername(usernames);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public void deleteUserByUsername(String username) {
        userRepository.deleteByUsername(username);
    }

    @Override
    public void deleteUsersByEmail(Collection<String> emails) {
        userRepository.deleteAllByEmail(emails);
    }

    @Override
    public void deleteUsersById(Collection<Integer> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public void deleteUsersByUsername(Collection<String> usernames) {
        userRepository.deleteAllByUsername(usernames);
    }

    @Override
    public int countUsers() {
        return userRepository.count();
    }
}
