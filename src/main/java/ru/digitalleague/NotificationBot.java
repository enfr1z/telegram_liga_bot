package ru.digitalleague;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NotificationBot extends TelegramLongPollingCommandBot implements Bot {
    private static final String BOT_NAME = "phosagro_liga_bot";
    private static final String BOT_TOKEN = "2033948807:AAGOzvzj_UgIfVbFkFQhiIwkNnvhX65-4PI";
    private static final String CONSTANTA2 = "PIZDEC KAKAYA OSHIBKA!";
    private Set<Long> chatIds = new HashSet<>();

    public NotificationBot(DefaultBotOptions options) {
        super(options);
    }

    public static void setUserWorkplaceKeyboard() {
        System.out.println(CONSTANTA2);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> userWorkButton = Arrays.asList(
                new InlineKeyboardButton().setText(Location.Savela.getCode()),
                new InlineKeyboardButton().setText(Location.Voronezh.getCode()),
                new InlineKeyboardButton().setText(Location.Home.getCode())
        );
        List<InlineKeyboardButton> userDontWorkButton = Collections.singletonList(
                new InlineKeyboardButton().setText(Location.I_do_not_want_to_work.getCode())
        );

        List<List<InlineKeyboardButton>> rowButtonsList = Arrays.asList(
          userWorkButton,
          userDontWorkButton
        );
        inlineKeyboardMarkup.setKeyboard(rowButtonsList);
    }

    @Override
    public void processNonCommandUpdate(Update update) {

    }

    @Override
    public void configChat(Long chatId) {
        chatIds.add(chatId);
    }

    @Override
    public void editMessage(EditMessageText editMessageTextCmd) {

    }

    @Override
    public void sendRemind() {

    }

    @Override
    public void sendRepeatRemind() {

    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
