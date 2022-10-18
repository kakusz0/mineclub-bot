package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface CommandImpl
{
    void executeCommand(final SlashCommandEvent p0, final Member p1, final TextChannel p2);
}
