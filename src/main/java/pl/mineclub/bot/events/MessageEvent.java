package pl.mineclub.bot.events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class MessageEvent extends ListenerAdapter {

    public Role findRole(Member member, String name) {
        return member.getRoles().stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
    }


    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if(!event.getMessage().getContentRaw().isEmpty()) {
            if (event.getMember() == null) return;
            if (findRole(Objects.requireNonNull(event.getMember()), "perm.link") == null) {
                if (event.getMessage().getContentRaw().startsWith("http") || event.getMessage().getContentRaw().startsWith("https") || event.getMessage().getContentRaw().startsWith("www") || event.getMessage().getContentRaw().contains(".pl") || event.getMessage().getContentRaw().contains(".eu") || event.getMessage().getContentRaw().contains(".ru") || event.getMessage().getContentRaw().contains(".com") || event.getMessage().getContentRaw().contains(".br") || event.getMessage().getContentRaw().contains(".fr") || event.getMessage().getContentRaw().contains(".uk") || event.getMessage().getContentRaw().contains("invite/")|| event.getMessage().getContentRaw().contains("discord.gg")|| event.getMessage().getContentRaw().contains(".gg")) {
                    if (!event.getChannel().getName().startsWith("t-")) return;
                    event.getMessage().delete().queue();
                }
            }
        }
    }
}
