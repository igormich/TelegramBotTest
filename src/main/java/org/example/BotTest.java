package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class BotTest extends TelegramLongPollingBot {

    private final String name;

    public BotTest(String token, String name) {
        super(token);
        this.name = name;
    }

    public static void main(String[] args) throws IOException, TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        var data = Files.readAllLines(new File("..\\tele.key").toPath());
        var token = data.get(0);
        var name = data.get(1);
        telegramBotsApi.registerBot(new BotTest(token, name));
    }

    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();
        if (message == null)
            return;
        var chatId = update.getMessage().getChatId();
        if (message.hasText()) {
            SendMessage response = new SendMessage();
            response.setChatId(chatId);
            StringBuilder input1 = new StringBuilder();
            input1.append(message.getText());
            input1.reverse();
            response.setText(input1.toString());
            try {
                execute(response);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                //throw new RuntimeException(e);
            }
            return;
        }
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText("I can process only text messages");
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}