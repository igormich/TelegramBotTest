package org.example.users;

public interface UserSession {
    UserState getState();

    void setState(UserState state);

    String getName();

    void setName(String name);

    String getProfile();

    void setProfile(String profile);
}
