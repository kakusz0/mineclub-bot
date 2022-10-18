package pl.mineclub.bot;

import pl.mineclub.bot.instance.BotInstance;

import javax.security.auth.login.LoginException;

public class Main {

    public static void main(String[] args) {
        try {
            new BotInstance();
        } catch (InterruptedException | LoginException e) {
            throw new RuntimeException(e);
        }
    }
}
