package org.example.commands;

import org.example.users.UserSession;

import java.util.ArrayList;
import java.util.List;

public class TextCommandHandler {

    private final List<TextCommand> commands = new ArrayList<>();
    private final CommandResult error = new CommandResult("Something going wrong, try again");
    public void addCommand(TextCommand command) {
        commands.add(command);
    }
    public CommandResult processCommand(UserSession session, String text) {
       var command = commands.stream().filter(c -> c.canBeApply(session, text)).findFirst();
       var result = command.map(c->c.execute(session, text));
       return result.orElse(error);
    }
}
