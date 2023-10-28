package org.example.users;

import org.example.database.DatabaseUserProvider;

public interface UserProvider {

    UserSession findUserById(UserId userId);
    class Helper{
       // private static final UserProvider userProvider = new InMemoryUserProvider();
       private static final UserProvider userProvider = DatabaseUserProvider.INSTANCE;

    }
    static UserProvider getDefault() {
        return Helper.userProvider;
    }

}
