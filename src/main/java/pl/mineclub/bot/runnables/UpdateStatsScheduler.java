package pl.mineclub.bot.runnables;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
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
//                .setColor(ConstantsHelper.color)
//                        .setImage("https://cdn.discordapp.com/attachments/932208812054695968/1031851897214214164/1666027057161.png")
//                .build())
//                .setActionRows(
//                        ActionRow.of(
//                                Button.of(ButtonStyle.SUCCESS, "verify", "Zweryfikuj")
//                        )).queue();
//        discordBot.getEmbedBuilder().clear();
//        discordBot.getJDA().getTextChannelById("944154297615151134").sendMessage(discordBot.getEmbedBuilder().addField("**Aby stworzyć ticket kliknij w przycisk poniżej**",
//                "\n" +
//                        "\n• Trolowanie ticketami bedzie karane banem,\n" +
//                        "• Od razu opisz swój problem, nie czekaj na wiadomość administratora\n",
//                false)
//                .setColor(ConstantsHelper.color)
//                .setFooter("MineClub.PL - Tickety", ConstantsHelper.imgUrl)
//                        .setImage("https://media.discordapp.net/attachments/932208812054695968/1031851895930748979/1666027320733.png")
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
//        discordBot.getJDA().getTextChannelById(943451034947301396L).sendMessage(discordBot.getEmbedBuilder().addField("Regulamin", "1. Zakaz szerzenia nienawiści oraz rasizmu.\n" +
//                "2. Zakaz udostępniania jakikolwiek danych innych osób.\n" +
//                "3. Zakaz nadużywania przekleństw na kanałach tekstowych.\n" +
//                "4. Zakaz obrażania lub poniżania osób na kanałach do tego nie przeznaczonych.\n" +
//                "5. Zakaz reklamowania innych serwerów,projektów bądź swoich kanałów na platformie YouTube bez pozwolenia Zarządu.\n" +
//                "6. Zakaz ustawiania sobie obraźliwych pseudonimów/nicków na serwerze discord.\n" +
//                "7. Zakaz umieszczania jakichkolwiek treści NSFW.\n" +
//                "8. Zakaz spamowania, floodowania, masowego oznaczania Administracji oraz osób innych na serwerze discord.\n" +
//                "9. Każda osoba dołączaja na nasz serwer discord musi akceptować oraz przestrzegać regulamin.\n" +
//                "10. Nie przestrzeganie regulaminu będzie skutkować permanentnym banem na naszym serwerze discord.", false).setImage("https://cdn.discordapp.com/attachments/932208812054695968/1031860508468461578/1666026652873.png").setFooter("MineClub.PL - Regulamin").setColor(ConstantsHelper.color).build()).queue();
//        discordBot.getEmbedBuilder().clear();


    }
}
