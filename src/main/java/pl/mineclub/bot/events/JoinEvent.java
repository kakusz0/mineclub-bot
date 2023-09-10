package pl.mineclub.bot.events;

import com.sun.istack.internal.NotNull;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinEvent extends ListenerAdapter {

    private final BotInstance discordBot;

    public JoinEvent(BotInstance discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        discordBot.getJDA().getTextChannelById(1145445171580911756L)
                .sendMessage(discordBot.getEmbedBuilder().setColor(ConstantsHelper.color)
                        .setFooter("MineClub.PL", ConstantsHelper.imgUrl)
                        .setImage("https://media.discordapp.net/attachments/932208812054695968/1147861819373912064/1693307494133.png")
                        .setThumbnail(event.getMember().getEffectiveAvatarUrl())
                        .setTimestamp(Instant.now()).addField(
                                event.getMember().getAsMention(), "Witaj na serwerze **MineClub.pl**, jesteś naszym **"
                                        + discordBot.getJDA().getGuildById(1144959653336330240L).getMembers().size() + "** graczem!\n" +
                                        "Mamy nadzieję, że zostaniesz z nami na dłużej!", true).build()).queue();

        discordBot.getEmbedBuilder().clear();
        //List<Role> roleList = new ArrayList<>(Arrays.asList(event.getGuild().getRoleById("1010815987962093610"),event.getGuild().getRoleById("1010815987962093610")));
        //roleList.forEach(role -> event.getGuild().addRoleToMember(event.getMember(), role).queue());
    }
}
