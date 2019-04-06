package ru.itmo.webmail.web.page;

import java.util.Map;

public class IndexPage extends Page {
    private void action() {
        // No operations.
    }

    private void registrationDone(Map<String, Object> view) {
        view.put("message", "You have been registered");
    }

    private void loginDone(Map<String, Object> view) {
        view.put("message", "Login successful");
    }

    private void logoutDone(Map<String, Object> view) {
        view.put("message", "Logged out");
    }
}
