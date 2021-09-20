package ru.digitalleague.Bot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.digitalleague.ChatInfo;
import ru.digitalleague.Location;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.Math.toIntExact;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static ru.digitalleague.Bot.Bot.CHOOSE_WORKPLACE;
import static ru.digitalleague.Bot.Bot.UPDATE_WORKPLACE_CALLBACK;
import static ru.digitalleague.Utils.formattingString;

@Component
public class NotificationBot extends TelegramLongPollingBot {
    private final Map<Long, ChatInfo> chatInfos = new HashMap<>();
    private final InlineKeyboardMarkup inlineKeyboardWithLocationMarkup = createInlineKeyboardWithLocation();
    private final ReplyKeyboardMarkup keyboardWitConfigReleaseMarkup = createKeyboardWithConfigRelease();

    @Value("${bot.name}") private static String BOT_NAME;
    @Value("${bot.token") private static String BOT_TOKEN;

    private ReplyKeyboardMarkup createKeyboardWithConfigRelease() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add("Установить ссылку на релиз");
        firstRow.add("Установить ссылку на инструкцию");
        firstRow.add("Установить ссылку на ретроспективу");
        keyboard.add(firstRow);
        return new ReplyKeyboardMarkup().setKeyboard(keyboard);
    }

    private InlineKeyboardMarkup createInlineKeyboardWithLocation() {
        List<InlineKeyboardButton> firstRowLine = List.of(
                new InlineKeyboardButton()
                        .setText(Location.Savela.getCode())
                        .setCallbackData(formattingString(UPDATE_WORKPLACE_CALLBACK, Location.Savela.getCode())),
                new InlineKeyboardButton()
                        .setText(Location.Voronezh.getCode())
                        .setCallbackData(formattingString(UPDATE_WORKPLACE_CALLBACK, Location.Voronezh.getCode())),
                new InlineKeyboardButton()
                        .setText(Location.Home.getCode())
                        .setCallbackData(formattingString(UPDATE_WORKPLACE_CALLBACK, Location.Home.getCode()))
        );

        List<InlineKeyboardButton> secondRowLine = Collections.singletonList(
                new InlineKeyboardButton()
                        .setText(Location.I_do_not_want_to_work.getCode())
                        .setCallbackData(formattingString(UPDATE_WORKPLACE_CALLBACK, Location.I_do_not_want_to_work.getCode()))
        );

        List<List<InlineKeyboardButton>> rowsInline = List.of(
                firstRowLine,
                secondRowLine
        );
        return new InlineKeyboardMarkup().setKeyboard(rowsInline);
    }

    @GetMapping(value = "/remind")
    private void remindMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        try {
            Integer chooseWorkPlaceMessageId = execute(
                    new SendMessage()
                            .setChatId(chatId)
                            .setText(buildWorkplaceString(chatId))
                            .setReplyMarkup(this.inlineKeyboardWithLocationMarkup)
            ).getMessageId();
            chatInfos.put(chatId, new ChatInfo(chooseWorkPlaceMessageId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void remindCallback(Update update, String[] callDataArray) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        addUserToMap(update, callDataArray[1], chatId);

        String answer = buildWorkplaceString(chatId);
        try {
            execute(new EditMessageText()
                    .setChatId(chatId)
                    .setMessageId(toIntExact(chatInfos.get(chatId).getChooseWorkPlaceMessageId()))
                    .setText(answer)
                    .setReplyMarkup(this.inlineKeyboardWithLocationMarkup));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void configurationReleaseUrls(Update update) {
        long chatId = update.getMessage().getChatId();
        try {
            execute(
                    new SendMessage()
                            .setChatId(chatId)
                            .setText("Hello world")
                            .setReplyMarkup(this.keyboardWitConfigReleaseMarkup)
            );
            this.keyboardWitConfigReleaseMarkup.getKeyboard().clear();;
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            if (text.equals("/remind")) {
                remindMessage(update);
            } else if (text.equalsIgnoreCase("/release_config") 
                    || text.equalsIgnoreCase("/release config")
                    || text.equalsIgnoreCase("/releaseConfig")) {
                configurationReleaseUrls(update);
            }
        } else if (update.hasCallbackQuery()) {
            String[] callDataArray = update.getCallbackQuery().getData().split(" ");
            if (callDataArray[0].equals(UPDATE_WORKPLACE_CALLBACK)) {
                remindCallback(update, callDataArray);
            }
        }
    }

    private String buildWorkplaceString(long chatId) {
        StringBuilder savelaString = new StringBuilder();
        StringBuilder voronezhString = new StringBuilder();
        StringBuilder homeString = new StringBuilder();
        StringBuilder doNotWorkingToday = new StringBuilder();
        Map<String, String> users = Optional.ofNullable(chatInfos.get(chatId)).map(ChatInfo::getUsers).orElse(new HashMap<>());

        for (String key : users.keySet()) {
            if (users.get(key).equals(Location.Savela.getCode())) {
                if (savelaString.length() == 0) {
                    savelaString = new StringBuilder("In savela working today:");
                }
                savelaString.append(" ").append(key);
            } else if (users.get(key).equals(Location.Voronezh.getCode())) {
                if (voronezhString.length() == 0) {
                    voronezhString = new StringBuilder("In voronezh working today:");
                }
                voronezhString.append(" ").append(key);
            } else if (users.get(key).equals(Location.Home.getCode())) {
                if (homeString.length() == 0) {
                    homeString = new StringBuilder("In home working today:");
                }
                homeString.append(" ").append(key);
            } else if (users.get(key).equals(Location.I_do_not_want_to_work.getCode())) {
                if (doNotWorkingToday.length() == 0) {
                    doNotWorkingToday = new StringBuilder("Do not want working today:");
                }
                doNotWorkingToday.append(" ").append(key);
            }
        }
        return formattingWorkplaceString(
                isEmpty(savelaString) ? new StringBuilder() : savelaString.append("\n"),
                isEmpty(voronezhString) ? new StringBuilder() : voronezhString.append("\n"),
                isEmpty(homeString) ? new StringBuilder() : homeString.append("\n"),
                doNotWorkingToday
        );
    }

    private String formattingWorkplaceString(StringBuilder savela, StringBuilder voronezh, StringBuilder home, StringBuilder doNotWorkingToday) {
        return MessageFormat.format("{0}\n{1}{2}{3}{4}", CHOOSE_WORKPLACE, savela, voronezh, home, doNotWorkingToday);
    }


    private String getUserName(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        User user = callbackQuery.getFrom();
        String name;

        if (isNotEmpty(user.getUserName())) {
            name = "@" + user.getUserName();
        } else if (isNotEmpty(user.getFirstName()) && isNotEmpty(user.getLastName())) {
            name =  formattingString(user.getFirstName(), user.getLastName());
        } else {
            name = user.getFirstName();
        }
        return name;
    }

    private void addUserToMap(Update update, String place, long chatId) {
        chatInfos.get(chatId).addUserToMap(getUserName(update), place);
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
