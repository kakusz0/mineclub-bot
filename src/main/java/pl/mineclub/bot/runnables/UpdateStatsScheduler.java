package pl.mineclub.bot.runnables;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.*;
import java.time.Instant;


public class UpdateStatsScheduler implements Runnable {
    private final BotInstance discordBot;
    private boolean done = false;
    public UpdateStatsScheduler(BotInstance discordBot) {
        this.discordBot = discordBot;
    }


    @Override
    public void run() {






        if(done) return;

        EmbedBuilder embedBuilder = BotInstance.getInstance().getEmbedBuilder();
        embedBuilder
                .addField("MineClub.pl | Tickety", "**Aby otworzyć ticket wybierz kategorię poniżej**", false)
                .setThumbnail(ConstantsHelper.imgUrl)
                .setColor(new Color(0x0080ff))

                .setFooter("MineClub.pl", ConstantsHelper.imgUrl)
                .setTimestamp(Instant.now());

        StringSelectMenu menu = StringSelectMenu.create("ticket-menu")
                .addOption("Problem na serwerze Minecraft", "minecraft_issue")
                .addOption("Problem na serwerze Discord", "discord_issue")
                .addOption("Podanie na rangę LIDER", "apply_leader")
                .addOption("Podanie na rangę TWÓRCA", "apply_creator")
                .addOption("Chce nawiązać współpracę", "want_partnership")
                .addOption("Problem ze stroną internetową", "website_issue")
                .addOption("Skarga na administratora", "admin_complaint")
                .addOption("Inne pytanie", "other_question")
                .setPlaceholder("Wybierz kategorię ticketa...")
                .build();

        // Wysyłanie wiadomości z menu
        BotInstance.getInstance().getJda().getTextChannelById(1260821483333357619L)
                .sendMessageEmbeds(
                        embedBuilder.build())
                .setActionRow(ActionRow.of(menu).getComponents())
                .queue();
        embedBuilder.clear();
        done = true;



    }
}
