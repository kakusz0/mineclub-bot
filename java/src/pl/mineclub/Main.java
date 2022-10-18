package pl.mineclub;

import pl.mineclub.instance.BotInstance;

public class Main {
    public static void main(String[] args) {
        try {
            new BotInstance();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
