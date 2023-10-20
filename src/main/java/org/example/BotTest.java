package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotTest extends TelegramLongPollingBot {

    private final Map<Long, UserSession> data = new HashMap<>();
    private final String name;

    public BotTest(String token, String name) {
        super(token);
        this.name = name;
        System.out.println(name);
    }

    public static void main(String[] args) throws IOException, TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        var data = Files.readAllLines(new File("..\\tele.key").toPath());
        String token = data.get(0);
        var name = data.get(1);
        telegramBotsApi.registerBot(new BotTest(token, name));
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            var message = update.getMessage();
            var chatId = update.getMessage().getChatId();
            var session = data.computeIfAbsent(chatId, l -> new UserSession());
            switch (session.getState()) {
                case NEW_BEE -> {
                    responseText("Hi, enter you name", chatId);
                    session.setState(UserState.NO_NAME);
                }
                case NO_NAME -> {
                    session.setName(message.getText());
                    responseText("Nice, tell me about yourself", chatId);
                    session.setState(UserState.NO_PROFILE);
                }
                case EDIT_NAME -> {
                    session.setName(message.getText());
                    responseTextAndKeyboard("Name has been changed!", chatId,
                            "Show", "Edit", "Change name");
                    session.setState(UserState.READY);
                }
                case NO_PROFILE -> {
                    session.setProfile(message.getText());
                    responseTextAndKeyboard("Nice profile has been created!", chatId,
                            "Show", "Edit", "Change name");
                    session.setState(UserState.READY);
                }
                case EDIT_PROFILE -> {
                    session.setProfile(message.getText());
                    responseTextAndKeyboard("Profile has been changed!", chatId,
                            "Show", "Edit", "Change name");
                    session.setState(UserState.READY);
                }

            }
        }
        if (update.hasCallbackQuery()) {
            var callbackQuery = update.getCallbackQuery();
            var chatId = callbackQuery.getMessage().getChatId();
            var session = data.computeIfAbsent(chatId, l -> new UserSession());
            var command = callbackQuery.getData();
            if (session.getState() == UserState.READY) {
                switch (command) {
                    case "Show" -> responseTextAndKeyboard(
                            "You data: " + session.getName() + "\n" + session.getProfile(),
                            chatId,
                            "Show", "Edit", "Change name");
                    case "Edit" -> {
                        responseText("Enter new info about yourself", chatId);
                        session.setState(UserState.EDIT_PROFILE);
                    }
                    case "Change name" -> {
                        responseText("Enter new name", chatId);
                        session.setState(UserState.EDIT_NAME);
                    }
                }
            }
        }
    }

    private void responseText(String str, Long chatId) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(str);

        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void responseTextAndKeyboard(String str, Long chatId, String... buttons) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId);
        response.setText(str);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        for (String text : buttons) {
            var button = new InlineKeyboardButton();
            button.setText(text);
            button.setCallbackData(text);
            rowInline.add(button);
        }
        rowsInline.add(rowInline);
        markupInline.setKeyboard(rowsInline);
        response.setReplyMarkup(markupInline);
        try {
            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }
}