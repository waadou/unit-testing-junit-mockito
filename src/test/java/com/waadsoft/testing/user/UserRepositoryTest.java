package com.waadsoft.testing.user;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Alassani ABODJI <abodjialassani[at]gmail.com>
 */
class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepositoryImpl();
    User bako = new User("bako", "bako@example.com");
    User alaza = new User("alaza", "alaza@example.com");
    User aminou = new User("aminou", "aminou@example.com");

    @Test
    void shouldFindNoUserWhenEmptyRepository() {
        Optional<User> found = userRepository.findById(1);
        assertThat(found).isEmpty();

        found = userRepository.findById(null);
        assertThat(found).isEmpty();
    }

    @Test
    void shouldAddUser() {
        userRepository.add(alaza);
        Optional<User> found = userRepository.findByEmail(alaza.getEmail());

        assertThat(found).isNotEmpty();

        User user = found.get();
        assertThat(user).extracting("email").isEqualTo("alaza@example.com");
        assertThat(user).extracting("username").isEqualTo("alaza");
    }

    @Test
    void shouldThrowExceptionWhenAddWithInvalidUser() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userRepository.add(null))
                .withMessageContaining("Invalid user specified");
    }

    @Test
    void shouldThrowExceptionWhenAddTwoUsersWithSameEmail() {
        User otherAlaza = new User("other.alaza", "alaza@example.com");

        assertThatExceptionOfType(DuplicateUserException.class).isThrownBy(() -> {
                    userRepository.add(alaza);
                    userRepository.add(otherAlaza);
                })
                .withMessageContaining("User already exists");
    }

    @Test
    void shouldThrowExceptionWhenAddTwoUsersWithSameUsername() {
        User otherAlaza = new User("alaza", "bako@example.com");

        assertThatExceptionOfType(DuplicateUserException.class).isThrownBy(() -> {
                    userRepository.add(alaza);
                    userRepository.add(otherAlaza);
                })
                .withMessageContaining("User already exists");
    }

    @Test
    void shouldUpdateUser() {
        userRepository.add(alaza);

        User dbUser = userRepository.findByEmail(alaza.getEmail()).get();
        User modifiedUser = new User(dbUser.getUserId(), dbUser.getUsername(), "modified.alaza@example.com");
        User updatedUser = userRepository.update(modifiedUser);

        assertThat(dbUser.getEmail()).isNotEqualTo(updatedUser.getEmail());
        assertThat(dbUser.getUsername()).isEqualTo(updatedUser.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithInvalidUser() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userRepository.update(null))
                .withMessageContaining("Invalid user specified");
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNonExistentUser() {
        assertThat(userRepository.findAll()).isEmpty();

        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userRepository.update(alaza))
                .withMessageContaining("User doesn't exist");
    }

    @Test
    void shouldFindUserByEmail() {
        userRepository.add(alaza);
        Optional<User> result = userRepository.findByEmail(alaza.getEmail());

        assertThat(result).isNotEmpty()
                .get().extracting("username")
                .isEqualTo(alaza.getUsername());

        assertThat(result).get().extracting("email")
                .isEqualTo(alaza.getEmail());
    }

    @Test
    void shouldFindUserByUsername() {
        userRepository.add(alaza);
        Optional<User> found = userRepository.findByUsername(alaza.getUsername());

        assertThat(found).isNotEmpty()
                .get().extracting("username")
                .isEqualTo(alaza.getUsername());

        assertThat(found).get().extracting("email")
                .isEqualTo(alaza.getEmail());
    }

    @Test
    void shouldCountAllUsers() {
        assertThat(userRepository.count()).isEqualTo(0);

        userRepository.add(alaza);
        userRepository.add(bako);

        assertThat(userRepository.count()).isEqualTo(2);
    }

    @Test
    void shouldFindAllUsers() {
        assertThat(userRepository.findAll()).isEmpty();

        userRepository.add(alaza);
        userRepository.add(bako);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(2)
                .containsOnly(bako, alaza);
    }

    @Test
    void shouldFindAllByIds() {
        List<User> foundUsers = userRepository.findAllById(null);
        assertThat(foundUsers).isEmpty();

        assertThat(userRepository.findAllById(List.of())).isEmpty();

        List<Integer> ids = List.of(1, 3, 8, 2, 58);
        assertThat(userRepository.findAllById(ids)).isEmpty();

        userRepository.add(alaza);
        userRepository.add(bako);

        assertThat(userRepository.findAllById(ids)).isNotEmpty()
                .hasSize(2)
                .containsOnly(alaza, bako);
    }

    @Test
    void shouldFindAllUsersByEmail() {
        List<User> foundUsers = userRepository.findAllByEmail(null);
        assertThat(foundUsers).isEmpty();

        foundUsers = userRepository.findAllByEmail(List.of());
        assertThat(foundUsers).isEmpty();

        List<String> emails = List.of("alaza@example.com", "asmiou@gmail.com", "bako@example.com");
        foundUsers = userRepository.findAllByEmail(emails);
        assertThat(foundUsers).isEmpty();

        userRepository.add(alaza);
        userRepository.add(bako);

        foundUsers = userRepository.findAllByEmail(emails);

        assertThat(foundUsers).isNotEmpty()
                .hasSize(2)
                .containsOnly(alaza, bako);
    }

    @Test
    void shouldFindAllUsersByUsername() {
        List<User> foundUsers = userRepository.findAllByUsername(null);
        assertThat(foundUsers).isEmpty();

        foundUsers = userRepository.findAllByUsername(List.of());
        assertThat(foundUsers).isEmpty();

        List<String> usernames = List.of("alaza", "asmiou", "bako");
        foundUsers = userRepository.findAllByUsername(usernames);
        assertThat(foundUsers).isEmpty();

        userRepository.add(alaza);
        userRepository.add(bako);

        foundUsers = userRepository.findAllByUsername(usernames);

        assertThat(foundUsers).isNotEmpty()
                .hasSize(2)
                .containsOnly(alaza, bako);
    }

    @Test
    void shouldDeleteUser() {
        userRepository.add(bako);
        userRepository.add(alaza);
        User addedAminou = userRepository.add(aminou);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(3)
                .containsOnly(aminou, alaza, bako);

        userRepository.delete(alaza);
        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(2)
                .containsOnly(aminou, bako);

        userRepository.delete(new User("haddy", "bako@example.com"));
        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(aminou);

        userRepository.delete(addedAminou);
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteWithInvalidUser() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userRepository.delete(null))
                .withMessageContaining("Invalid user specified");
    }

    @Test
    void shouldThrowExceptionWhenDeleteWithNonExistentUser() {
        userRepository.add(bako);

        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userRepository.delete(alaza))
                .withMessageContaining("No user found");
    }

    @Test
    void shouldDeleteUserById() {
        User addedUser = userRepository.add(alaza);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);

        userRepository.deleteById(addedUser.getUserId());

        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteByIdWithInvalidUserId() {
        User addedUser = userRepository.add(alaza);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);

        Integer userId1 = null;
        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userRepository.deleteById(userId1))
                .withMessageContaining("User to be deleted doesn't exist");

        Integer userId2 = -25;
        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userRepository.deleteById(userId2))
                .withMessageContaining("User to be deleted doesn't exist");

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);
    }

    @Test
    void shouldDeleteUserByEmail() {
        User addedUser = userRepository.add(alaza);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);

        userRepository.deleteByEmail(addedUser.getEmail());

        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteByEmailWithInvalidEmail() {
        whenWeAddUsers(alaza);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);

        String email = "cxwQD@gdg";
        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userRepository.deleteByEmail(email))
                .withMessageContaining("User to be deleted doesn't exist");

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);
    }

    @Test
    void shouldDeleteUserByUsername() {
        whenWeAddUsers(alaza);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);

        userRepository.deleteByUsername(alaza.getUsername());

        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteByUsernameWithInvalidUsername() {
        whenWeAddUsers(alaza);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);

        String username = "cxwQD@gdg";
        assertThatExceptionOfType(UserNotFoundException.class).isThrownBy(() -> userRepository.deleteByUsername(username))
                .withMessageContaining("User doesn't exist");

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(alaza);
    }

    @Test
    void shouldDeleteAllByIds() {
        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).isEmpty();

        whenWeAddUsers(alaza, bako);

        List<Integer> ids = List.of(1, 2);
        userRepository.deleteAllById(ids);
        assertThat(foundUsers).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteAllByIdsWithoutIds() {
        List<User> foundUsers = userRepository.findAll();
        assertThat(foundUsers).isEmpty();

        List<Integer> ids = List.of();
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userRepository.deleteAllById(ids))
                .withMessageContaining("Invalid collection");
    }

    @Test
    void shouldDeleteAllByEmail() {
        assertThat(userRepository.findAll()).isEmpty();

        whenWeAddUsers(alaza, bako);

        assertThat(userRepository.findAll()).isNotEmpty()
                .containsOnly(alaza, bako);

        List<String> emails = List.of(alaza.getEmail(), bako.getEmail());
        userRepository.deleteAllByEmail(emails);
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteAllByEmailWithoutEmails() {
        assertThat(userRepository.findAll()).isEmpty();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userRepository.deleteAllByEmail(List.of()))
                .withMessageContaining("Invalid collection");
    }

    @Test
    void shouldDeleteAllByUsername() {
        assertThat(userRepository.findAll()).isEmpty();

        whenWeAddUsers(alaza, bako);

        assertThat(userRepository.findAll()).isNotEmpty()
                .containsOnly(alaza, bako);

        List<String> usernames = List.of(alaza.getUsername(), bako.getUsername());
        userRepository.deleteAllByUsername(usernames);
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenDeleteAllByUsernameWithoutUsernames() {
        assertThat(userRepository.findAll()).isEmpty();

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> userRepository.deleteAllByUsername(List.of()))
                .withMessageContaining("Invalid collection");
    }

    private void whenWeAddUsers(User... users) {
        for (User user : users) {
            userRepository.add(user);
        }
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
