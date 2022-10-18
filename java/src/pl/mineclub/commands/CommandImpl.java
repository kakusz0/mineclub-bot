package pl.mineclub.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandImpl {
    void executeCommand(SlashCommandInteractionEvent event, Member member, TextChannel textChannel);
}
