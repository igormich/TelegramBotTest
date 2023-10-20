package org.example.users;

public interface UserProvider {

    UserSession findUserById(UserId userId);
    class Helper{
        private static final UserProvider userProvider = new InMemoryUserProvider();
    }
    static UserProvider getDefault() {
        return Helper.userProvider;
    }

}
