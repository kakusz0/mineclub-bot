package pl.mineclub.bot.runnables;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.time.Instant;


public class UpdateStatsScheduler implements Runnable {
    private final BotInstance discordBot;

    public UpdateStatsScheduler(BotInstance discordBot) {
        this.discordBot = discordBot;
    }


    @Override
    public void run() {
//        discordBot.getJDA().getTextChannelById(1145445363956859003L).sendMessage(discordBot.getEmbedBuilder().addField("**Weryfikacja**", "Kliknij przycisk poniżej, aby się zweryfikować!",false)
//                .setColor(ConstantsHelper.color)
//                        //.setImage("https://cdn.discordapp.com/attachments/932208812054695968/1031851897214214164/1666027057161.png")
//                .build())
//                .setActionRows(
//                        ActionRow.of(
//                                Button.of(ButtonStyle.SUCCESS, "verify", "Zweryfikuj")
//                        )).queue();
//        discordBot.getEmbedBuilder().clear();
//        discordBot.getJDA().getTextChannelById("1145546354324680796").sendMessage(discordBot.getEmbedBuilder().addField("**Aby stworzyć ticket kliknij w przycisk poniżej**",
//                "\n" +
//                        "\n• Trolowanie ticketami bedzie karane banem,\n" +
//                        "• Od razu opisz swój problem, nie czekaj na wiadomość administratora\n",
//                false)
//                .setColor(ConstantsHelper.color)
//                .setFooter("MineClub.PL - Tickety", ConstantsHelper.imgUrl)
//                        .setImage("https://media.discordapp.net/attachments/932208812054695968/1147861820376358994/1693307564959.png")
//                .build())
//                .setActionRows(
//                        ActionRow.of(
//                                SelectionMenu.create("menu:class").setPlaceholder("Wybierz powód") // shows the placeholder indicating what this menu is for
//                                        .setRequiredRange(1, 1) // only one can be selected
//                                        .addOption("Błąd na serwerze", "blad")
//                                        .addOption("Backup", "backup")
//                                        .addOption("Bląd na stronie/itemshop", "strona")
//                                        .addOption("Partnerstwo", "partner")
//                                        .addOption("Ranga Media", "media")
//                                        .build()
//
//                        )
////                        ActionRow.of(
////                                Button.of(ButtonStyle.SUCCESS, "ticket", "Stwórz ticket")
////                        )
//                ).queue();
//        discordBot.getJDA().getTextChannelById(1144964007380394005L).sendMessage(discordBot.getEmbedBuilder().addField("Regulaminy", "Wybierz z listy interesujący cie regulamin", false).setImage("https://media.discordapp.net/attachments/932208812054695968/1147861819004821626/1693248378000.png").setFooter("MineClub.PL - Regulamin").setColor(ConstantsHelper.color)                .build())
//                .setActionRows(
//                        ActionRow.of(
//                                SelectionMenu.create("menu:class").setPlaceholder("Wybierz regulamin") // shows the placeholder indicating what this menu is for
//                                        .setRequiredRange(1, 1) // only one can be selected
//                                        .addOption("Regulamin discorda", "regulamindc")
//                                        .addOption("Regulamin serwera minecraft", "regulaminmc")
//                                        .build()
//
//                        )).queue();



    }
}
