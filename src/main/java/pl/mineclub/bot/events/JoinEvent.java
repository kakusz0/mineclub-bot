package pl.mineclub.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.*;
import java.time.Instant;

public class JoinEvent extends ListenerAdapter {



    @Override
    public void onGuildMemberJoin(@NotNull net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent event) {
        EmbedBuilder embedBuilder = BotInstance.getInstance().getEmbedBuilder();
        embedBuilder
                .addField("**MineClub.pl | Witamy 👋**", "Witamy na serwerze **MineClub**" + event.getMember().getAsMention() + "\nJesteś **" + event.getGuild().getMembers().size() + "** osobą na serwerze!" , false)
                .setThumbnail(ConstantsHelper.imgUrl)
                .setColor(new Color(0x0080ff))

                .setFooter("MineClub.pl", ConstantsHelper.imgUrl)
                .setTimestamp(Instant.now());
        embedBuilder.clear();
        event.getGuild().getTextChannelById(1260794474217476156L).sendMessageEmbeds(embedBuilder.build()).queue();
    }
}
