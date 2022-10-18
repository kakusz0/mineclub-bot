package pl.mineclub.bot.events;

import com.sun.istack.internal.NotNull;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JoinEvent extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        List<Role> roleList = new ArrayList<>(Arrays.asList(event.getGuild().getRoleById("1010815987962093610"),event.getGuild().getRoleById("1010815987962093610")));
        roleList.forEach(role -> event.getGuild().addRoleToMember(event.getMember(), role).queue());
    }
}
