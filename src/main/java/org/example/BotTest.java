package org.example;

import org.example.commands.*;
import org.example.telegram.TelegramBot;
import org.example.users.UserState;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BotTest {

    public static void main(String[] args) throws IOException, TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        var data = Files.readAllLines(new File("..\\tele.key").toPath());
        String token = data.get(0);
        var name = data.get(1);
        var bot = new TelegramBot(token, name);


        var textHandler = new TextCommandHandler();
        textHandler.addCommand(new SimpleTextCommand(UserState.NEW_BEE,
                "Hi, enter you name",
                UserState.NO_NAME));
        textHandler.addCommand(new SimpleTextCommand(UserState.NO_NAME,
                "Nice, tell me about yourself",
                (s,t)-> {s.setName(t);s.setState(UserState.NO_PROFILE);}));
        textHandler.addCommand(new SimpleTextCommand(UserState.EDIT_NAME,
                "Name has been changed!",
                ButtonHelper.readyStateButtons,
                (s,t)-> {s.setName(t);s.setState(UserState.READY);}));
        textHandler.addCommand(new SimpleTextCommand(UserState.NO_PROFILE,
                "Nice profile has been created!",
                ButtonHelper.readyStateButtons,
                (s,t)-> {s.setProfile(t);s.setState(UserState.READY);}));
        textHandler.addCommand(new SimpleTextCommand(UserState.EDIT_PROFILE,
                "Profile has been changed!",
                ButtonHelper.readyStateButtons,
                (s,t)-> {s.setProfile(t);s.setState(UserState.READY);}));
        bot.addTextHandler(textHandler);

        var buttonHandler = new TextCommandHandler();
        buttonHandler.addCommand(new ShowProfileCommand());
        buttonHandler.addCommand(new SimpleButtonReadyStateCommand("Edit",
                "Enter new info about yourself",
                UserState.EDIT_PROFILE));
        buttonHandler.addCommand(new SimpleButtonReadyStateCommand("Change name",
                "Enter new name",
                UserState.EDIT_NAME));
        bot.addButtonHandler(buttonHandler);

        telegramBotsApi.registerBot(bot);
    }


}