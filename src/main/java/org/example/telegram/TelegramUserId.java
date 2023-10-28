package org.example.telegram;

import org.example.users.UserId;

import java.util.Objects;

public class TelegramUserId implements UserId {


    private final Long id;

    public TelegramUserId(Long id){
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramUserId that = (TelegramUserId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public Long getId() {
        return id;
    }
}
