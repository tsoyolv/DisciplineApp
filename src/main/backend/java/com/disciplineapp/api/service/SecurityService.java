package com.disciplineapp.api.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
