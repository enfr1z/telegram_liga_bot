package ru.digitalleague.Bot;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface Bot {

    String CHOOSE_WORKPLACE = "When you work today ?";
    String UPDATE_WORKPLACE_CALLBACK = "update_place";
    String RELEASE_CONFIG = "release_config";

    void configChat(Long chatId);
    void editMessage(EditMessageText editMessageTextCmd);
    void sendRemind();
    void sendRepeatRemind();
}
