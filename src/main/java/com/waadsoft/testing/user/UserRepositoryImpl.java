package com.waadsoft.testing.user;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

/**
 * @author Alassani ABODJI <abodjialassani[at]gmail.com>
 */
public class UserRepositoryImpl implements UserRepository {

    private final Map<Integer, User> users;

    public UserRepositoryImpl() {
        System.out.println("Instance created...");
        this.users = new HashMap<>();
    }

    @Override
    public User add(User user) {
        if (isNull(user)) {
            throw new IllegalArgumentException("Invalid user specified!");
        }
        if (findByEmail(user.getEmail()).isPresent()) {
            throw new DuplicateUserException("User already exists!");
        }
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new DuplicateUserException("User already exists!");
        }

        Integer id = getNextId();
        User newUser = new User(getNextId(), user.getUsername(), user.getEmail());
        users.put(id, newUser);

        return newUser;
    }

    @Override
    public User update(User user) {
        if (isNull(user)) {
            throw new IllegalArgumentException("Invalid user specified!");
        }
        if (!users.containsKey(user.getUserId())) {
            throw new UserNotFoundException("User doesn't exist!");
        }
        users.remove(user.getUserId());
        users.put(user.getUserId(), user);

        return user;
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findOneBy(user -> user.getEmail().equals(email));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findOneBy(user -> user.getUsername().equals(username));
    }

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public List<User> findAllById(Collection<Integer> ids) {
        if (isEmptyCollection(ids)) {
            return List.of();
        }
        return findAllBy(user -> ids.contains(user.getUserId()));
    }

    @Override
    public List<User> findAllByEmail(Collection<String> emails) {
        if (isEmptyCollection(emails)) {
            return List.of();
        }
        return findAllBy(user -> emails.contains(user.getEmail()));
    }

    @Override
    public List<User> findAllByUsername(Collection<String> usernames) {
        if (isEmptyCollection(usernames)) {
            return List.of();
        }
        return findAllBy(user -> usernames.contains(user.getUsername()));
    }

    @Override
    public void delete(User user) {
        if (isNull(user)) {
            throw new IllegalArgumentException("Invalid user specified!");
        }
        if (users.containsKey(user.getUserId())) {
            users.remove(user.getUserId());
            return;
        }
        Optional<User> found = findByUsername(user.getUsername());
        if (found.isPresent()) {
            users.remove(found.get().getUserId());
            return;
        }
        found = findByEmail(user.getEmail());
        if (found.isPresent()) {
            users.remove(found.get().getUserId());
            return;
        }
        throw new UserNotFoundException("No user found!");
    }

    @Override
    public void deleteById(Integer userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            return;
        }
        throw new UserNotFoundException("User to be deleted doesn't exist!");
    }

    @Override
    public void deleteByEmail(String email) {
        Optional<User> found = findByEmail(email);
        if (found.isEmpty()) {
            throw new UserNotFoundException("User to be deleted doesn't exist!");
        }
        users.remove(found.get().getUserId());
    }

    @Override
    public void deleteByUsername(String username) {
        Optional<User> found = findByUsername(username);
        if (found.isEmpty()) {
            throw new UserNotFoundException("User doesn't exist!");
        }
        deleteById(found.get().getUserId());
    }

    @Override
    public void deleteAllById(Collection<Integer> ids) {
        if (isEmptyCollection(ids)) {
            throw new IllegalArgumentException("Invalid collection of IDs!");
        }
        ids.forEach(this::deleteById);
    }

    @Override
    public void deleteAllByEmail(Collection<String> emails) {
        if (isEmptyCollection(emails)) {
            throw new IllegalArgumentException("Invalid collection of emails!");
        }
        emails.forEach(this::deleteByEmail);
    }

    @Override
    public void deleteAllByUsername(Collection<String> usernames) {
        if (isEmptyCollection(usernames)) {
            throw new IllegalArgumentException("Invalid collection of usernames!");
        }
        usernames.forEach(this::deleteByUsername);
    }

    @Override
    public int count() {
        return users.size();
    }

    private List<User> findAllBy(Predicate<User> predicate) {
        return users.values()
                .stream()
                .filter(predicate)
                .collect(toList());
    }

    private Optional<User> findOneBy(Predicate<User> predicate) {
        return users.values()
                .stream()
                .filter(predicate)
                .findFirst();
    }

    private Integer getNextId() {
        return 1 + users.values().stream()
                .map(User::getUserId)
                .max(Integer::compare)
                .orElse(0);
    }

    private <T> boolean isEmptyCollection(Collection<T> elements) {
        return isNull(elements) || elements.isEmpty();
    }
}

/*=============================================================================
 * Copyright 2023 Waad Soft<https://www.waadsoft.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 =============================================================================*/
