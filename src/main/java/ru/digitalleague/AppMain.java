package ru.digitalleague;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class AppMain {
    public static void main(String[] args) {
        System.out.println("initialize bot");
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(new NotificationBot(ApiContext.getInstance(DefaultBotOptions.class)));
            System.out.println("end initialize");
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
