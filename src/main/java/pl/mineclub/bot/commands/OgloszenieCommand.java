package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class OgloszenieCommand implements CommandImpl {

    private final BotInstance discordBot;

    public OgloszenieCommand(BotInstance discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void executeCommand(SlashCommandEvent event, Member member, TextChannel textChannel) {
        if(!textChannel.equals(discordBot.getJDA().getTextChannelById(1145545704836702278L))){
            event.reply("nie mozesz, wykonaj ta komende na kanale " +  discordBot.getJDA().getTextChannelById(1145545704836702278L).getAsMention()).setEphemeral(true).queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        if (!member.getPermissions().contains(Permission.ADMINISTRATOR)) {
            event.reply("nie mozesz").setEphemeral(true).queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
        } else {
            event.reply("Wykonano").setEphemeral(true).queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
        }

        discordBot.getJDA().getTextChannelById(1145545704836702278L).sendMessage(discordBot.getEmbedBuilder().setColor(ConstantsHelper.color).setFooter("MineClub.PL - Ogłoszenia", ConstantsHelper.imgUrl).setTimestamp(Instant.now()).addField("**Ogłoszenie**", event.getOption("amount").getAsString().replace("{n}", "\n"), true).build()).queue();
        discordBot.getEmbedBuilder().clear();
    }
}
