package org.example.users;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserProvider implements UserProvider {

    private final Map<UserId, UserSession> data = new HashMap<>();
    @Override
    public UserSession findUserById(UserId userId) {
        return data.computeIfAbsent(userId, l -> new InMemoryUserSession());
    }
}
