package com.waadsoft.testing.user;

import java.util.Objects;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 *
 * @author Alassani ABODJI <abodjialassani[at]gmail.com>
 */
public class User {

    private final Integer userId;

    private final String email;
    private final String username;

    public User(String username, String email) {
        this(0, username, email);
    }

    public User(Integer userId, String username, String email) {
        this.userId = userId;
        this.email = requireNonNull(email);
        this.username = requireNonNull(username);
    }

    /*---------------------------------------------------------
    |       A C C E S S O R S    /    M O D I F I E R S       |
    ==========================================================*/
    public Integer getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    /*---------------------------------------------------------
    |   H A S H C O D E  /  E Q U A L S  /  T O S T R I N G   |
    ==========================================================*/
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (isNull(obj) || !(obj instanceof User)) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals(this.username, other.username);
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
