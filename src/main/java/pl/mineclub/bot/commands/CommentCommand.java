package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import pl.mineclub.bot.instance.BotInstance;

import java.util.concurrent.TimeUnit;

public class CommentCommand implements CommandImpl {

    private final BotInstance discordBot;

    public CommentCommand(BotInstance discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void executeCommand(SlashCommandEvent event, Member member, TextChannel textChannel) {
        if(!textChannel.getName().contains("\uD83D\uDCA1・propozycje")){
            event.reply("Wejdź na odpowiedni kanał: "+ discordBot.getJDA().getGuildChannelById("944150551388651562").getAsMention()).queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
        } else {
            textChannel.sendMessage(member.getAsMention() + " napisał: " +  event.getOption("amount").getAsString().replace("everyone", "MineClub.PL").replace("here", "MineClub.PL")).queue();
            event.deferReply().queue(message -> message.deleteOriginal().queueAfter(1, TimeUnit.SECONDS));
        }
    }
}
