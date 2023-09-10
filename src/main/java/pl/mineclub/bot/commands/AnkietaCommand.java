package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.helpers.DataHelper;
import pl.mineclub.bot.instance.BotInstance;
import pl.mineclub.bot.objects.Ankieta;
import pl.mineclub.bot.runnables.UpdateAnkietaScheduler;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class AnkietaCommand implements CommandImpl {

    private final BotInstance discordBot;

    public AnkietaCommand(BotInstance discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void executeCommand(SlashCommandEvent event, Member member, TextChannel textChannel) {

        MessageEmbed message = discordBot.getEmbedBuilder()
                .addField("**Ankieta**",
                        "**Treść:**\n > " + event.getOption("tresc").getAsString()
                                + "\n\n **Odpowiedzi:**\n\n**1)** "
                                + event.getOption("odpowiedz_1").getAsString()
                                + "\n**2)** "
                                + event.getOption("odpowiedz_2").getAsString()
                                + "\n\n"
                                + "Koniec ankiety: " + DataHelper.getDate(DataHelper.parseDateDiff(event.getOption("czas").getAsString(), true)) +
                                "\nAby zagłosować wybierz przycisk poniżej!"


                        , false)
                .setColor(ConstantsHelper.color)
                .setFooter("MineClub.PL - Ankiety", ConstantsHelper.imgUrl)
                .setTimestamp(Instant.now())

                .build();

        Ankieta ankieta = new Ankieta(message, 0, 0, DataHelper.parseDateDiff(event.getOption("czas").getAsString(), true));
        discordBot.getJDA()
                .getTextChannelById(1145546113517093004L)
                .sendMessage(message)
                .setActionRows(
                        ActionRow.of(
                                Button.of(ButtonStyle.DANGER, "1", "1"),
                                Button.of(ButtonStyle.DANGER, "2", "2")
                        )
                ).queue();
        discordBot.getEmbedBuilder().clear();
        discordBot.ankietaManager.ankiets.put(ankieta, ankieta);
        event.deferReply().queue(messagee -> messagee.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));

    }
}