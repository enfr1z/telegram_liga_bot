package ru.digitalleague;

public enum Location {
    Savela("Savela"),
    Voronezh("Voronezh"),
    Home("Home"),
    I_do_not_want_to_work("I_do_not_want_to_work");

    private String code;

    Location(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
