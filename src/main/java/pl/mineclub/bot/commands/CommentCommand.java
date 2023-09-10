package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import pl.mineclub.bot.instance.BotInstance;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CommentCommand implements CommandImpl {

    private final BotInstance discordBot;

    public CommentCommand(BotInstance discordBot) {
        this.discordBot = discordBot;
    }
    public Role findRole(Member member, String name) {
        return member.getRoles().stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void executeCommand(SlashCommandEvent event, Member member, TextChannel textChannel) {
        if (findRole(Objects.requireNonNull(event.getMember()), "・Zarząd Serwera") == null) {
            event.reply("nie mozesz").setEphemeral(true).queue();
            return;
        }
        textChannel.sendMessage(member.getAsMention() + " napisał: " +  event.getOption("amount").getAsString()).queue();

    }
}
