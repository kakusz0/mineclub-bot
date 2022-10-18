package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class MessageCommand implements CommandImpl {

    private Optional<Color> color(String name) {
        try {
            Field field = java.awt.Color.class.getDeclaredField(name.toUpperCase());
            if (field.getType() == java.awt.Color.class) {
                return Optional.of((java.awt.Color)field.get(null));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private final BotInstance discordBot;

    public MessageCommand(BotInstance discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void executeCommand(SlashCommandEvent event, Member member, TextChannel textChannel) {
        textChannel.sendMessage(discordBot.getEmbedBuilder()
                .addField(
                        event.getOption("title").getAsString(),
                        event.getOption("content").getAsString().replace("{n}", "\n"),
                        false)
                        .setFooter(event.getOption("footer").getAsString(), ConstantsHelper.imgUrl)
                .setColor(color(event.getOption("color").getAsString()).get()).build()
        ).queue();
        discordBot.getEmbedBuilder().clear();
        event.deferReply().queue(message -> message.deleteOriginal().queueAfter(1, TimeUnit.SECONDS));
    }
}
