package org.example.commands;

import org.example.users.UserSession;

public class ShowProfileCommand implements TextCommand {
    @Override
    public boolean canBeApply(UserSession session, String text) {
        return "Show".equals(text);
    }

    @Override
    public CommandResult execute(UserSession session, String text) {
        return new CommandResult("You data: " + session.getName() + "\n" + session.getProfile(),
                ButtonHelper.readyStateButtons);
    }
}
