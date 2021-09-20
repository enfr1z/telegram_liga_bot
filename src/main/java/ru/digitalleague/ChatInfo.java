package ru.digitalleague;

import com.google.common.collect.Maps;

import java.util.Map;

public class ChatInfo {
    private Integer chooseWorkPlaceMessageId;
    private final Map<String, String> users = Maps.newHashMap();

    private String urlReleaseJira;
    private String urlManualConfluence;
    private String urlRetrospectiveDashboard;


    public ChatInfo(Integer chooseWorkPlaceMessageId) {
        this.chooseWorkPlaceMessageId = chooseWorkPlaceMessageId;
    }

    public void addUserToMap(String userName, String location) {
        this.users.put(userName, location);
    }

    public void updateMessageId(Integer messageId) {
        this.chooseWorkPlaceMessageId = messageId;
    }

    public Integer getChooseWorkPlaceMessageId() {
        return chooseWorkPlaceMessageId;
    }

    public Map<String, String> getUsers() {
        return users;
    }
}
