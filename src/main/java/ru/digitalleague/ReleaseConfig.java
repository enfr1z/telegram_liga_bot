package ru.digitalleague;

public enum ReleaseConfig {
    Jira_url("Jira_url"),
    Confluence_url("Confluence_url"),
    Retrospective_dashboard_url("Retrospective_dashboard_url");

    private String code;

    ReleaseConfig(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
