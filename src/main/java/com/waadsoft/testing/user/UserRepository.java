package com.waadsoft.testing.user;

import java.util.List;
import java.util.Optional;
import java.util.Collection;

/**
 *
 * @author Alassani ABODJI <abodjialassani[at]gmail.com>
 */
public interface UserRepository {

    User add(User user);

    User update(User user);

    Optional<User> findById(Integer userId);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findAll();

    List<User> findAllById(Collection<Integer> ids);

    List<User> findAllByEmail(Collection<String> emails);

    List<User> findAllByUsername(Collection<String> usernames);

    void delete(User user);

    void deleteById(Integer userId);

    void deleteByEmail(String email);

    void deleteByUsername(String username);

    void deleteAllByEmail(Collection<String> emails);

    void deleteAllById(Collection<Integer> ids);

    void deleteAllByUsername(Collection<String> usernames);

    int count();
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
