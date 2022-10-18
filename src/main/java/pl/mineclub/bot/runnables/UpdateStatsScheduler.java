package pl.mineclub.bot.runnables;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;


public class UpdateStatsScheduler implements Runnable {
    private final BotInstance discordBot;

    public UpdateStatsScheduler(BotInstance discordBot) {
        this.discordBot = discordBot;
    }


    @Override
    public void run() {
//        discordBot.getJDA().getTextChannelById(943454497768878080L).sendMessage(discordBot.getEmbedBuilder().addField("**Weryfikacja**", "Kliknij przycisk poniżej, aby się zweryfikować!",false)
//                .setColor(Color.getHSBColor(Color.RGBtoHSB(46, 204,112, null)[0], Color.RGBtoHSB(46, 204,112, null)[1], Color.RGBtoHSB(46, 204,112, null)[2]).getRGB())
//                .build())
//                .setActionRows(
//                        ActionRow.of(
//                                Button.of(ButtonStyle.SUCCESS, "verify", "Zweryfikuj")
//                        )).queue();

        discordBot.getJDA().getTextChannelById("944154297615151134").sendMessage(discordBot.getEmbedBuilder().addField("**Aby stworzyć ticket kliknij w przycisk poniżej**",
                "\n" +
                        "\n• Trolowanie ticketami bedzie karane banem,\n" +
                        "• Od razu opisz swój problem, nie czekaj na wiadomość administratora\n",
                false)
                .setColor(ConstantsHelper.color)
                .setFooter("MineClub.PL - Tickety", ConstantsHelper.imgUrl)
                        .setImage("https://media.discordapp.net/attachments/932208812054695968/1031851895930748979/1666027320733.png")
                .build())
                .setActionRows(
                        ActionRow.of(
                                SelectionMenu.create("menu:class").setPlaceholder("Wybierz powód") // shows the placeholder indicating what this menu is for
                                        .setRequiredRange(1, 1) // only one can be selected
                                        .addOption("Błąd na serwerze", "blad")
                                        .addOption("Backup", "backup")
                                        .addOption("Bląd na stronie/itemshop", "strona")
                                        .addOption("Partnerstwo", "partner")
                                        .addOption("Ranga Media", "media")
                                        .build()

                        )
//                        ActionRow.of(
//                                Button.of(ButtonStyle.SUCCESS, "ticket", "Stwórz ticket")
//                        )
                ).queue();


    }
}
