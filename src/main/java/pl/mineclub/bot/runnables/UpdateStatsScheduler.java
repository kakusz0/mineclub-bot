package pl.mineclub.bot.runnables;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.Color;
import java.time.Instant;


public class UpdateStatsScheduler implements Runnable {
    private final BotInstance discordBot;
    private boolean done = true;
    public UpdateStatsScheduler(BotInstance discordBot) {
        this.discordBot = discordBot;
    }


    @Override
    public void run() {






        if(done) return;

        EmbedBuilder embedBuilder = BotInstance.getInstance().getEmbedBuilder();
        embedBuilder
                .addField("MineClub.pl | Weryfikacja", "**Aby się zweryfikować, kliknij przycisk poniżej**", false)
                .setThumbnail(ConstantsHelper.imgUrl)
                .setColor(new Color(0x0080ff))

                .setFooter("MineClub.pl", ConstantsHelper.imgUrl)
                .setTimestamp(Instant.now());
        BotInstance.getInstance().getJda().getTextChannelById(1260825266020941834L).sendMessageEmbeds(
                        embedBuilder.build())
                .setActionRow(Button.success("verify_button", "✅ Weryfikacja"))
                .queue();
        embedBuilder.clear();
        done = true;



    }
}
