package com.waadsoft.testing.user;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * @author Alassani ABODJI <abodjialassani[at]gmail.com>
 */
class UserRepositoryTest {

    private final UserRepository userRepository = new UserRepositoryImpl();

    @Test
    void shouldFindNoUserWhenEmptyRepository() {
        int userId = 1;

        Optional<User> found = userRepository.findById(userId);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindNoUserWhenInvalidUserIdGiven() {
        Integer userId = null;
        User user = givenUserAlaza();
        userRepository.add(user);

        Optional<User> found = userRepository.findById(userId);

        assertThat(found).isEmpty();
    }

    @Test
    void shouldAddWhenGivenValidUser() {
        User user = givenUserAlaza();
        String email = user.getEmail();
        String username = user.getUsername();
        userRepository.add(user);

        Optional<User> found = userRepository.findByEmail(email);

        assertSameEmail(found, email);
        assertSameUsername(found, username);
    }

    @Test
    void shouldThrowExceptionWhenInvalidUserAdded() {
        User user = null;
        String exceptionMsg = "Invalid user specified";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.add(user), exceptionMsg);
    }

    @Test
    void shouldThrowExceptionWhenTwoUsersWithSameEmailAdded() {
        User user1 = givenUserAlaza();
        String email = user1.getEmail();
        User user2 = new User("baba.alaza", email);
        String message = "User already exists";
        userRepository.add(user1);

        assertExceptionThrown(DuplicateUserException.class, () -> userRepository.add(user2), message);
    }

    @Test
    void shouldThrowExceptionWhenTwoUsersWithSameUsernameAdded() {
        User user1 = givenUserAlaza();
        String username = user1.getUsername();
        User user2 = new User(username, "bako@example.com");
        String message = "User already exists";
        userRepository.add(user1);

        assertExceptionThrown(DuplicateUserException.class, () -> userRepository.add(user2), message);
    }

    @Test
    void shouldUpdateWhenUserExists() {
        User user = userRepository.add(givenUserAlaza());
        String newEmail = "modified.user@example.com";

        User updatedUser = userRepository.update(new User(user.getUserId(), user.getUsername(), newEmail));

        assertThat(updatedUser.getEmail()).isNotEqualTo(user.getEmail());
        assertThat(updatedUser.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithInvalidUser() {
        User user = null;
        String message = "Invalid user specified";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.update(user), message);
    }

    @Test
    void shouldThrowExceptionWhenUpdateWithNonExistentUser() {
        User user = givenUserAlaza();
        String message = "User doesn't exist";

        assertExceptionThrown(UserNotFoundException.class, () -> userRepository.update(user), message);
    }

    @Test
    void shouldFindByEmailWhenUserExists() {
        User user = givenUserAlaza();
        userRepository.add(user);

        Optional<User> actual = userRepository.findByEmail(user.getEmail());

        assertSameEmail(actual, user.getEmail());
        assertSameUsername(actual, user.getUsername());
    }

    @Test
    void shouldFindByUsernameWhenUserExists() {
        User user = givenUserAlaza();
        userRepository.add(user);

        Optional<User> actual = userRepository.findByUsername(user.getUsername());

        assertSameEmail(actual, user.getEmail());
        assertSameUsername(actual, user.getUsername());
    }

    @Test
    void shouldCountWhenUsersExist() {
        User user1 = givenUserAlaza();
        User user2 = givenUserBako();
        userRepository.add(user1);
        userRepository.add(user2);

        int actual = userRepository.count();

        assertThat(actual).isEqualTo(2);
    }

    @Test
    void shouldCountWhenNoUserExists() {
        int actual = userRepository.count();

        assertThat(actual).isEqualTo(0);
    }

    @Test
    void shouldFindAllWhenUsersExist() {
        User user1 = givenUserAlaza();
        User user2 = givenUserBako();
        User user3 = givenUserAminou();
        userRepository.add(user1);
        userRepository.add(user2);
        userRepository.add(user3);

        List<User> actual = userRepository.findAll();

        assertThat(actual).isNotEmpty()
                .hasSize(3)
                .containsOnly(user1, user2, user3);
    }

    @Test
    void shouldFindAllByIdsWhenUsersExist() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<Integer> ids = Arrays.asList(user1.getUserId(), user2.getUserId(), user3.getUserId());

        List<User> actual = userRepository.findAllById(ids);

        assertThat(actual).isNotEmpty()
                .hasSize(3)
                .containsOnly(user2, user3, user1);
    }

    @Test
    void shouldReturnNothingWhenFindAllByIdsWithInvalidIdCollection() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<Integer> ids = null; // Invalid ID collection

        List<User> actual = userRepository.findAllById(ids);

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnNothingWhenFindAllByIdsWithEmptyIdCollection() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<Integer> ids = Collections.emptyList(); // Empty ID collection

        List<User> actual = userRepository.findAllById(ids);

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldFindAllByEmailWhenUsersExist() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<String> emails = Arrays.asList(user1.getEmail(), user2.getEmail(), user3.getEmail());

        List<User> actual = userRepository.findAllByEmail(emails);

        assertThat(actual).isNotEmpty()
                .hasSize(3)
                .containsOnly(user2, user3, user1);
    }

    @Test
    void shouldReturnNothingWhenFindAllByEmailWithInvalidEmailCollection() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<String> emails = null; // Invalid email collection

        List<User> actual = userRepository.findAllByEmail(emails);

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnNothingWhenFindAllByEmailWithEmptyEmailCollection() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<String> emails = Collections.emptyList(); // Empty email collection

        List<User> actual = userRepository.findAllByEmail(emails);

        assertThat(actual).isEmpty();
    }


    @Test
    void shouldFindAllByUsernameWhenUsersExist() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<String> usernames = Arrays.asList(user1.getUsername(), user2.getUsername(), user3.getUsername());

        List<User> actual = userRepository.findAllByUsername(usernames);

        assertThat(actual).isNotEmpty()
                .hasSize(3)
                .containsOnly(user2, user3, user1);
    }

    @Test
    void shouldReturnNothingWhenFindAllByUsernameWithInvalidUsernameCollection() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<String> usernames = null; // Invalid collection of usernames

        List<User> actual = userRepository.findAllByUsername(usernames);

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldReturnNothingWhenFindAllByUsernameWithEmptyUsernameCollection() {
        User user1 = userRepository.add(givenUserAlaza());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAminou());
        List<String> usernames = Collections.emptyList(); // Empty collection of usernames

        List<User> actual = userRepository.findAllByUsername(usernames);

        assertThat(actual).isEmpty();
    }

    @Test
    void shouldDeleteWhenUserExists() {
        User alaza = givenUserAlaza();
        User bako = givenUserBako();
        userRepository.add(bako);
        userRepository.add(alaza);
        User aminou = userRepository.add(givenUserAminou());

        userRepository.delete(bako);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(2)
                .containsOnly(alaza, aminou);
    }

    @Test
    void shouldThrowExceptionWhenDeleteWithInvalidUser() {
        User user = null;
        String message = "Invalid user specified";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.delete(user), message);
    }

    @Test
    void shouldThrowExceptionWhenDeleteWithNonExistentUser() {
        User user1 = givenUserBako();
        User user2 = givenUserAminou();
        userRepository.add(user1);
        String message = "No user found";

        assertExceptionThrown(UserNotFoundException.class, () -> userRepository.delete(user2), message);
    }

    @Test
    void shouldDeleteByIdWhenUserExists() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        Integer userId = user1.getUserId();

        userRepository.deleteById(userId);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(user2);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByIdWithNoUserId() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        Integer userId = null;
        String message = "User to be deleted doesn't exist";

        assertExceptionThrown(UserNotFoundException.class, () -> userRepository.deleteById(userId), message);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByIdWithInvalidUserId() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        Integer userId = -1000;
        String message = "User to be deleted doesn't exist";

        assertExceptionThrown(UserNotFoundException.class, () -> userRepository.deleteById(userId), message);
    }

    @Test
    void shouldDeleteByEmailWhenUserExists() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        String email = user1.getEmail();

        userRepository.deleteByEmail(email);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(user2);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByEmailWithNoEmail() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        String email = null;
        String message = "User to be deleted doesn't exist";

        assertExceptionThrown(UserNotFoundException.class, () -> userRepository.deleteByEmail(email), message);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByEmailWithInvalidEmail() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        String email = "azllammzmmzmm@vcggdffgs.com";
        String message = "User to be deleted doesn't exist";

        assertExceptionThrown(UserNotFoundException.class, () -> userRepository.deleteByEmail(email), message);
    }

    @Test
    void shouldDeleteByUsernameWhenUserExists() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        String username = user1.getUsername();

        userRepository.deleteByUsername(username);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(user2);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByUsernameWithNoUsername() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        String username = null;
        String message = "User doesn't exist";

        assertExceptionThrown(UserNotFoundException.class, () -> userRepository.deleteByUsername(username), message);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByUsernameWithInvalidUsername() {
        User user1 = userRepository.add(givenUserBako());
        User user2 = userRepository.add(givenUserAminou());
        String username = "azllammzmmzmm";
        String message = "User doesn't exist";

        assertExceptionThrown(UserNotFoundException.class, () -> userRepository.deleteByUsername(username), message);
    }

    @Test
    void shouldDeleteAllByIdsWhenUsersExist() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<Integer> ids = Arrays.asList(user1.getUserId(), user2.getUserId());

        userRepository.deleteAllById(ids);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(user3);
    }

    @Test
    void shouldThrowExceptionWhenDeleteAllByIdsWithInvalidIdCollection() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<Integer> ids = null;
        String message = "Invalid collection";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.deleteAllById(ids), message);
    }

    @Test
    void shouldThrowExceptionWhenDeleteAllByIdsWithEmptyCollection() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<Integer> ids = Collections.emptyList();
        String message = "Invalid collection";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.deleteAllById(ids), message);
    }

    @Test
    void shouldDeleteAllByEmailWhenUsersExist() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<String> emails = Arrays.asList(user1.getEmail(), user2.getEmail());

        userRepository.deleteAllByEmail(emails);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(user3);
    }


    @Test
    void shouldThrowExceptionWhenDeleteAllByEmailWithInvalidEmailCollection() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<String> emails = null;
        String message = "Invalid collection";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.deleteAllByEmail(emails), message);
    }

    @Test
    void shouldThrowExceptionWhenDeleteAllByEmailWithEmptyCollection() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<String> emails = Collections.emptyList();
        String message = "Invalid collection";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.deleteAllByEmail(emails), message);
    }


    @Test
    void shouldDeleteAllByUsernameWhenUsersExist() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<String> usernames = Arrays.asList(user1.getUsername(), user2.getUsername());

        userRepository.deleteAllByUsername(usernames);

        assertThat(userRepository.findAll()).isNotEmpty()
                .hasSize(1)
                .containsOnly(user3);
    }

    @Test
    void shouldThrowExceptionWhenDeleteAllByUsernameWithInvalidUsernameCollection() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<String> usernames = null;
        String message = "Invalid collection";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.deleteAllByUsername(usernames), message);
    }

    @Test
    void shouldThrowExceptionWhenDeleteAllByUsernameWithEmptyCollection() {
        User user1 = userRepository.add(givenUserAminou());
        User user2 = userRepository.add(givenUserBako());
        User user3 = userRepository.add(givenUserAlaza());
        List<String> usernames = Collections.emptyList();
        String message = "Invalid collection";

        assertExceptionThrown(IllegalArgumentException.class, () -> userRepository.deleteAllByUsername(usernames), message);
    }

    private <T> void assertExceptionThrown(Class<? extends RuntimeException> exceptionClass, ThrowableAssert.ThrowingCallable throwingCallable, String message) {
        assertThatExceptionOfType(exceptionClass).isThrownBy(throwingCallable) // Adding second user
                .withMessageContaining(message);
    }

    private void assertSameUsername(User user, String expectedUsername) {
        assertThat(user.getUsername()).isEqualTo(expectedUsername);
    }

    private void assertSameUsername(Optional<User> user, String expectedUsername) {
        assertThat(user).isNotEmpty();
        assertSameUsername(user.get(), expectedUsername);
    }

    private void assertSameEmail(User user, String expectedEmail) {
        assertThat(user.getEmail()).isEqualTo(expectedEmail);
    }

    private void assertSameEmail(Optional<User> user, String expectedEmail) {
        assertThat(user).isNotEmpty();
        assertSameEmail(user.get(), expectedEmail);
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
