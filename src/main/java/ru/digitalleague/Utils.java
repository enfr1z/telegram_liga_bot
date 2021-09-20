package ru.digitalleague;

import java.text.MessageFormat;

public class Utils {

    public static String formattingString(String callbackCode, String workPlace) {
        return MessageFormat.format("{0} {1}", callbackCode, workPlace);
    }
}
