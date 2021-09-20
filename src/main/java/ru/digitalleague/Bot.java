package ru.digitalleague;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface Bot {
    void configChat(Long chatId);
    void editMessage(EditMessageText editMessageTextCmd);
    void sendRemind();
    void sendRepeatRemind();
}
