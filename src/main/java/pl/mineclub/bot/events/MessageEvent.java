package pl.mineclub.bot.events;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.Color;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MessageEvent extends ListenerAdapter {
    public Role findRole(Member member, String name) {
        return member.getRoles().stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        if (!content.isEmpty()) {
            if (event.getMember() == null) return;
            if (findRole(Objects.requireNonNull(event.getMember()), "Permisja: Link") == null) {
                if (event.getMessage().getContentRaw().startsWith("http") || event.getMessage().getContentRaw().startsWith("https") || event.getMessage().getContentRaw().startsWith("www") || event.getMessage().getContentRaw().contains(".pl") || event.getMessage().getContentRaw().contains(".eu") || event.getMessage().getContentRaw().contains(".ru") || event.getMessage().getContentRaw().contains(".com") || event.getMessage().getContentRaw().contains(".br") || event.getMessage().getContentRaw().contains(".fr") || event.getMessage().getContentRaw().contains(".uk") || event.getMessage().getContentRaw().contains("invite/") || event.getMessage().getContentRaw().contains("discord.gg") || event.getMessage().getContentRaw().contains(".gg")) {
                    event.getMessage().delete().queue();
                }
            }
        }
        Message message = event.getMessage();
        TextChannel channel = event.getChannel().asTextChannel();

        if (channel.getIdLong() == 1260824074658254848L) {
            if(message.getAuthor().isBot()) return;
            EmbedBuilder embedBuilder = BotInstance.getInstance().getEmbedBuilder();
            embedBuilder
                    .addField("MineClub.pl | Propozycja", "", false)
                    .addField("Autor", message.getAuthor().getAsMention(), true)
                    .addField("Treść", message.getContentRaw(), false)
                    .setThumbnail(ConstantsHelper.imgUrl)
                    .setColor(new Color(0x0080ff))

                    .setFooter("MineClub.pl", ConstantsHelper.imgUrl)
                    .setTimestamp(Instant.now());


            channel.sendMessageEmbeds(embedBuilder.build())
                    .setActionRow(
                            Button.success("vote_yes_" + message.getIdLong(), "TAK: 0"),
                            Button.danger("vote_no_" + message.getIdLong(), "NIE: 0")
                    )
                    .queue();

            embedBuilder.clear();

            message.delete().queue();

            channel.createThreadChannel("Dyskusja: " + message.getAuthor().getAsTag())
                    .queueAfter(1, TimeUnit.SECONDS);


            BotInstance.getInstance().getReactionEvent().getVotes().put(message.getIdLong(), new HashMap<>() {{
                put("vote_yes", new HashSet<>());
                put("vote_no", new HashSet<>());
            }});
            BotInstance.getInstance().getReactionEvent().getUserVotes().put(message.getIdLong(), new HashSet<>());
        }
    }



}
