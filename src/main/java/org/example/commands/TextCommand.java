package org.example.commands;

import org.example.users.UserSession;

public interface TextCommand {

    boolean canBeApply(UserSession session, String text);

    CommandResult execute(UserSession session, String text);
}
