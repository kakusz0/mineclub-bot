package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import pl.mineclub.bot.instance.BotInstance;

public class AnkietaCommand implements CommandImpl {

    private final BotInstance discordBot;

    public AnkietaCommand(BotInstance discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void executeCommand(SlashCommandEvent event, Member member, TextChannel textChannel) {


    }
}
