package pl.mineclub.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collections;

public class ReactionEvent extends ListenerAdapter {


    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("rules-menu")) {
            String selected = event.getValues().get(0);
            String response = switch (selected) {
                case "discord_rules" -> "Regulamin serwera Discord:\n"
                        + "1. Zakaz szerzenia nienawiści oraz rasizmu.\n"
                        + "2. Zakaz udostępniania jakikolwiek danych innych osób.\n"
                        + "3. Zakaz nadużywania przekleństw na kanałach tekstowych.\n"
                        + "4. Zakaz obrażania lub poniżania osób na kanałach do tego nie przeznaczonych.\n"
                        + "5. Zakaz reklamowania innych serwerów, projektów bądź swoich kanałów na platformie YouTube bez pozwolenia Zarządu.\n"
                        + "6. Zakaz ustawiania sobie obraźliwych pseudonimów/nicków na serwerze Discord.\n"
                        + "7. Zakaz umieszczania jakichkolwiek treści NSFW.\n"
                        + "8. Zakaz spamowania, floodowania, masowego oznaczania Administracji oraz innych osób na serwerze Discord.\n"
                        + "9. Każda osoba dołączająca na nasz serwer Discord musi akceptować oraz przestrzegać regulaminu.\n"
                        + "10. Nieprzestrzeganie regulaminu będzie skutkować permanentnym banem na naszym serwerze Discord.";
                case "minecraft_rules" -> "Regulamin serwera Minecraft:\n"
                        + "1. Zakaz używania niedozwolonych modyfikacji.\n"
                        + "2. Zakaz griefingu.\n"
                        + "3. Szanuj innych graczy.\n"
                        + "4. Zakaz używania cheatów.";
                case "chestpvp_rules" -> "Regulamin trybu ChestPVP:\n"
                        + "1. Zakaz teamowania się.\n"
                        + "2. Używaj tylko dozwolonych przedmiotów.\n"
                        + "3. Szanuj przeciwników.";
                default -> "Nieznana opcja.";
            };

            EmbedBuilder embedBuilder = BotInstance.getInstance().getEmbedBuilder();
            embedBuilder
                    .addField("MineClub.pl | Regulaminy", response, false)
                    .setThumbnail(ConstantsHelper.imgUrl)
                    .setColor(new Color(0x0080ff))

                    .setFooter("MineClub.pl", ConstantsHelper.imgUrl)
                    .setTimestamp(Instant.now());
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            embedBuilder.clear();
        }
        if (event.getComponentId().equals("info-menu")) {
            String selected = event.getValues().get(0);
            String response = switch (selected) {
                case "creator_requirements" -> """
                        📽 Wymagania na rangę Twórca:
                        
                        **Wymagania na rangę Twórca jako Youtuber:**
                        > Aby otrzymać rangę Twórca prowadząc kanał na platformie YouTube, musisz posiadać co najmniej 1000 subskrybcji.
                        > Na twoim profilu powinien znajdować się przynajmniej 1 film z naszego serwera, na którym jest umieszczona widoczna wstawka, którą możesz pobrać tutaj.
                        **Wymagania na rangę Twórca jako TikToker:**
                        > Aby otrzymać rangę Twórca prowadząc konto na platformie TikTok, musisz posiadać co najmniej 2000 obserwujących.
                        > Na twoim profilu powinny znajdować się przynajmniej 2 rolki z naszego serwera, na których jest umieszczona widoczna wstawka, którą możesz pobrać tutaj.
                        """;
                case "leader_requirements" -> """
                        ⚔️ Wymagania na rangę Lider:
                        
                        > Aby otrzymać rangę Lider, twoja gildia musi posiadać minimum 10 członków, własny serwer Discord oraz być aktywna na edycji.
                        """;
                case "partner_requirements" -> """
                        🤝 Wymagania na rangę Partner:
                        
                        > Aby otrzymać rangę Partner, skontaktuj się z nami poprzez otwarcie ticketu. Partnerstwa są zawierane indywidualnie i nie posiadamy określonych wymagań.
                        """;
                case "booster_rewards" -> """
                        ❓️ Nagroda za boosta:
                        
                        W zamian za ulepszanie naszego serwera Discord możesz zyskać:
                        > Unikalną rangę Booster
                        > Dostęp do specjalnej strefy
                        > Dostęp do specjalnych konkursów
                        > 50 punktów za każde ulepszenie serwera
                        > 2x większą szansę na wydropienie nagród
                        Wszystkie benefity są na czas trwania boostów.
                        """;
                default -> "Nieznana opcja.";
            };

            // Obsługa wybranej opcji
            EmbedBuilder embedBuilder = BotInstance.getInstance().getEmbedBuilder();
            embedBuilder
                    .addField("MineClub.pl | Informacje", response, false)
                    .setThumbnail(ConstantsHelper.imgUrl)
                    .setColor(new Color(0x0080ff))

                    .setFooter("MineClub.pl", ConstantsHelper.imgUrl)
                    .setTimestamp(Instant.now());
            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
            embedBuilder.clear();
        }
        if (event.getComponentId().equals("ticket-menu")) {
            String selected = event.getValues().get(0);
            Member member = event.getMember();
            Guild guild = event.getGuild();

            if (member == null || guild == null) {
                event.reply("Wystąpił problem z tworzeniem ticketa.").setEphemeral(true).queue();
                return;
            }


            // Kategorie zgłoszeń i odpowiednie odpowiedzi
            String ticketTitle;
            switch (selected) {
                case "minecraft_issue":
                    ticketTitle = "Problem na serwerze Minecraft";
                    break;
                case "discord_issue":
                    ticketTitle = "Problem na serwerze Discord";
                    break;
                case "apply_leader":
                    ticketTitle = "Podanie na rangę LIDER";
                    break;
                case "apply_creator":
                    ticketTitle = "Podanie na rangę TWÓRCA";
                    break;
                case "want_partnership":
                    ticketTitle = "Nawiązanie współpracy";
                    break;
                case "website_issue":
                    ticketTitle = "Problem ze stroną internetową";
                    break;
                case "admin_complaint":
                    ticketTitle = "Skarga na administratora";
                    break;
                case "other_question":
                    ticketTitle = "Inne pytanie";
                    break;
                default:
                    ticketTitle = "Inne zgłoszenie";
                    break;
            }

            // Tworzenie kanału dla ticketa
            Category category = guild.getCategoryById(1268329641471377479L); // Zamień na rzeczywiste ID kategorii
            if (category == null) {
                event.reply("Nie można znaleźć kategorii dla ticketów. Skontaktuj się z administracją.").setEphemeral(true).queue();
                return;
            }

            // Sprawdzenie, czy użytkownik ma już otwarty ticket
            for (TextChannel channel : guild.getTextChannels()) {
                if (channel.getTopic() != null && channel.getName().contains(member.getUser().getAsTag())) {
                    event.reply("Masz już otwarty ticket!").setEphemeral(true).queue();
                    return;
                }
            }

            // Tworzenie kanału tekstowego w ramach wybranej kategorii
            category.createTextChannel("ticket-" + member.getUser().getName())
                    .setTopic(ticketTitle + " - " + member.getUser().getAsTag())
                    .queue(textChannel -> {
                        Role role1 = guild.getRoleById(1260830294844641342L);
                        Role role2 = guild.getRoleById(1260827364666114129L);
                        if (role1 != null && role2 != null) {
                            textChannel.getManager().putPermissionOverride(role1, Collections.singleton(Permission.ADMINISTRATOR), null).queue();
                            textChannel.getManager().putPermissionOverride(role2, Collections.singleton(Permission.ADMINISTRATOR), null).queue();
                        }
                        textChannel.sendMessage(member.getAsMention() + " Twój ticket został utworzony. Opisz swój problem, a administracja wkrótce się z Tobą skontaktuje.").queue();
                        event.reply("Twój ticket został utworzony: " + textChannel.getAsMention()).setEphemeral(true).queue();
                    }, failure -> {
                        event.reply("Nie udało się utworzyć ticketa. Spróbuj ponownie później.").setEphemeral(true).queue();
                    });
        }
    }


}
