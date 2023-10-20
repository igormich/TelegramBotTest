package org.example.commands;

import org.example.users.UserSession;
import org.example.users.UserState;

import java.util.List;
import java.util.function.BiConsumer;

public class SimpleTextCommand implements TextCommand {


    private final UserState acceptableState;
    private final CommandResult result;
    private final BiConsumer<UserSession, String> action;

    public SimpleTextCommand(UserState acceptableState, String resultText, BiConsumer<UserSession, String> action) {
        this.acceptableState = acceptableState;
        this.result = new CommandResult(resultText);
        this.action = action;
    }
    public SimpleTextCommand(UserState acceptableState, String resultText, List<String> buttons, BiConsumer<UserSession, String> action) {
        this.acceptableState = acceptableState;
        this.result = new CommandResult(resultText, buttons);
        this.action = action;
    }

    public SimpleTextCommand(UserState acceptableState, String resultText, UserState newState) {
        this.acceptableState = acceptableState;
        this.result = new CommandResult(resultText);
        this.action = (s,t) -> s.setState(newState);
    }

    @Override
    public boolean canBeApply(UserSession session, String text) {
        return session.getState() == acceptableState;
    }

    @Override
    public CommandResult execute(UserSession session, String text) {
        action.accept(session, text);
        return result;
    }
}
