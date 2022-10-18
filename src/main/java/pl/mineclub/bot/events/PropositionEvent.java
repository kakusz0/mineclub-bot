package pl.mineclub.bot.events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PropositionEvent extends ListenerAdapter {

    private final BotInstance discordBot;

    public PropositionEvent(BotInstance discordBot){
        this.discordBot = discordBot;
    }



    public Role findRole(Member member, String name) {
        List<Role> roles = member.getRoles();
        // filter by role name
        // take first result
        // else return null
        return roles.stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
        // else return null

    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (e.getChannel().getName().equalsIgnoreCase("\uD83D\uDCA1・propozycje")) {
            if(!e.getAuthor().isBot()) {
                e.getMessage().delete().queueAfter(2, TimeUnit.SECONDS);
                try {
                    discordBot.getJDA().getTextChannelById("944150551388651562").sendMessage(
                            discordBot.getEmbedBuilder().setTitle(" ")
                                    .addField("Propozycja użytkownika " + e.getAuthor().getAsTag(),
                                            e.getMessage().getContentRaw(),
                                            false).setColor(ConstantsHelper.color)
                                    .setFooter("MineClub.PL - Propozycje", ConstantsHelper.imgUrl)
                                    .setThumbnail(e.getAuthor().getEffectiveAvatarUrl())
                                    .setTimestamp(Instant.now()).build()).queue();
                    //discordBot.getJDA().getGuildById("938877301695852634").getTextChannelById("939257134590345346").sendMessage("" + discordBot.getEmbedBuilder().addField("Propozycja użytkownika " + e.getAuthor().getName(), String.valueOf(e.getMessage()), false).build()).queue();
                    discordBot.getEmbedBuilder().clear();

                } catch (Exception exception) {
                    discordBot.getJDA().getTextChannelById("944150551388651562").sendMessage(e.getAuthor().getAsMention() + " twoja wiadomość nie może zawierać więcej niz 1024 znaki!").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                }
            }
            e.getMessage().addReaction(":yes:1013538698043871303").queue();
            e.getMessage().addReaction(":no:1013538696580051034").queue();


        }
    }


}
