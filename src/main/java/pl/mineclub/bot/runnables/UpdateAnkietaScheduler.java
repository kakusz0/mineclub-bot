package pl.mineclub.bot.runnables;

import pl.mineclub.bot.instance.BotInstance;
import pl.mineclub.bot.objects.Ankieta;

public class UpdateAnkietaScheduler implements Runnable {

    private final BotInstance discordBot;

    public UpdateAnkietaScheduler(BotInstance discordBot) {

        this.discordBot = discordBot;
    }


    @Override
    public void run() {
        for(Ankieta ankieta : discordBot.ankietaManager.ankiets.values()){

        }
    }
}
