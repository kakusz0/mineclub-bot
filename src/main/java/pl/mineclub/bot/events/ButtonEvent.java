package pl.mineclub.bot.events;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SelectionMenuEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.internal.interactions.ButtonImpl;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;
import pl.mineclub.bot.objects.Ankieta;
import pl.mineclub.bot.runnables.UpdateAnkietaScheduler;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ButtonEvent extends ListenerAdapter {
    private final BotInstance discordBot;

    private final List<Member> activatedTickets;
    public ButtonEvent(BotInstance discordBot) {
        this.discordBot = discordBot;
        this.activatedTickets = new ArrayList<>();
    }

    public Optional<String> findValue(SelectionMenuEvent event, String val){
        for(String str : event.getValues()){
            if(str.equalsIgnoreCase(val)){
                activatedTickets.add(event.getMember());
                return Optional.of(val);
            }
        }
        return Optional.empty();
    }

    public Optional<String> findValue2(SelectionMenuEvent event, String val){
        for(String str : event.getValues()){
            if(str.equalsIgnoreCase(val)){
                return Optional.of(val);
            }
        }
        return Optional.empty();
    }

    private SelectionMenuEvent event;


    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        this.event = event;
        if (event.getMember().getGuild() != discordBot.getJDA().getGuildById(1144959653336330240L)) return;
        if(activatedTickets.contains(event.getMember())) {
            event.reply("Dopiero stworzyłeś ticket!").setEphemeral(true).queue(message -> message.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
            return;
        }
        if (findValue2(event, "regulamindc").isPresent()) {
            event.reply("1. Zakaz szerzenia nienawiści oraz rasizmu.\n" +
                    "2. Zakaz udostępniania jakikolwiek danych innych osób.\n" +
                    "3. Zakaz nadużywania przekleństw na kanałach tekstowych.\n" +
                    "4. Zakaz obrażania lub poniżania osób na kanałach do tego nie przeznaczonych.\n" +
                    "5. Zakaz reklamowania innych serwerów,projektów bądź swoich kanałów na platformie YouTube bez pozwolenia Zarządu.\n" +
                    "6. Zakaz ustawiania sobie obraźliwych pseudonimów/nicków na serwerze discord.\n" +
                    "7. Zakaz umieszczania jakichkolwiek treści NSFW.\n" +
                    "8. Zakaz spamowania, floodowania, masowego oznaczania Administracji oraz osób innych na serwerze discord.\n" +
                    "9. Każda osoba dołączaja na nasz serwer discord musi akceptować oraz przestrzegać regulamin.\n" +
                    "10. Nie przestrzeganie regulaminu będzie skutkować permanentnym banem na naszym serwerze discord.").setEphemeral(true).queue();

        }
        if (findValue2(event, "regulaminmc").isPresent()) {
            event.reply("1. Osoba wchodząc na serwer jest zobowiązana przestrzegać regulaminu i akceptuje go.\n" +
                    "2. Brak znajomości regaminu nie zwalnia z jego przestrzegania.\n" +
                    "3. Zakaz używania wszelkich wspomagaczy ułatwiających rozgrywke tzw. cheaty ,a posiadanie ich jest traktowane jakby ich się używało.\n" +
                    "4. Zakaz używania programów które obciążają łącze w celu uzyskania przewagi.\n" +
                    "5. Na serwerze można używać automatycznego klikania tzw. macro.\n" +
                    "6. Zakaz publikowania danych osobowych, IP bądź adresu zamieszkania.\n" +
                    "7. Zakaz reklamowania innych serwerów, usług czy stron.\n" +
                    "8. Zakaz szantażowania oraz grożenia innym osobom.\n" +
                    "9. Zakaz utrudniania przebiegu edycji.\n" +
                    "10. Błędy serwera należy zgłosić wyższej administracji(ROOT,H@), korzystanie z nich może zakończyć się permanentnym banem na serwerze.\n" +
                    "11. Dozwolone jest bugowanie się perłami, używanie skryptów na kopanie AFK oraz bugowanie blokami\n" +
                    "12. Zakaz jakichkolwiek działań DDOS i tym podobnych w kierunku graczy i samego serwera.\n" +
                    "13. Oszustwo bądź próba oszustwa administratora będzie surowo karane.\n" +
                    "14. Tworzenie multikont jest niedozwolone.\n" +
                    "15. Podczas sprawdzania administrator ma prawo prosić o pobranie niektórych aplikacji typu anydesk.\n" +
                    "16. Administrator ma prawo przeglądać pliki i foldery które mogą mu się wydawać podejrzane.").setEphemeral(true).queue();
        }
        if (findValue(event, "blad").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1147862498951835758").createTextChannel("blad-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
        if (findValue(event, "backup").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1147862498951835758").createTextChannel("backup-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
        if (findValue(event, "strona").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1147862498951835758").createTextChannel("strona-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
        if (findValue(event, "partner").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1147862498951835758").createTextChannel("partnerstwo-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
        if (findValue(event, "media").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1147862498951835758").createTextChannel("media-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
    }


    private final ButtonImpl closedButton = new ButtonImpl("fsdfdsfds", "Zamknij ticket", ButtonStyle.DANGER, true, Emoji.fromUnicode("U+2716"));



    @Override
    public void onButtonClick(ButtonClickEvent event) {
        int i = 0;
        int i2 = 0;
        if (event.getMember().getGuild() != discordBot.getJDA().getGuildById(1144959653336330240L)) return;
        if (event.getComponentId().equals("verify")) {//event.getGuild().getRoleById("1008688936614375544")
            List<Role> roleList = new ArrayList<>(Arrays.asList(event.getGuild().getRoleById("1145581320542629969")));
            roleList.forEach(role -> event.getGuild().addRoleToMember(event.getMember(), role).queue());
            event.reply("Pomyślnie zweryfikowano!").setEphemeral(true).queue(message -> message.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
        }
        if (event.getComponentId().equals("1")) {

        }
        if (event.getComponentId().equals("2")) {

        }
        if (event.getComponentId().equals("close")) {
            activatedTickets.remove(event.getMember());
            event.editButton(closedButton).queue();
            TextChannel textChannel = event.getTextChannel();
            textChannel.sendMessage("Ticket zostanie usunięty w ciągu 5 sekund").queue();
            textChannel.sendMessage("Ticket zostanie usunięty w ciągu 4 sekund").queueAfter(1, TimeUnit.SECONDS);
            textChannel.sendMessage("Ticket zostanie usunięty w ciągu 3 sekund").queueAfter(2, TimeUnit.SECONDS);
            textChannel.sendMessage("Ticket zostanie usunięty w ciągu 2 sekund").queueAfter(3, TimeUnit.SECONDS);
            textChannel.sendMessage("Ticket zostanie usunięty w ciągu 1 sekundy").queueAfter(4, TimeUnit.SECONDS);
            textChannel.delete().queueAfter(5, TimeUnit.SECONDS);
        }
    }


    @Override
    public void onTextChannelCreate(TextChannelCreateEvent event) {
        TextChannel textChannel = event.getChannel();
        if (textChannel.getParent().getName().contains("tickety")) {
            this.event.reply("Zalożono ticket " + textChannel.getAsMention()).setEphemeral(true).queue();
            //discordBot.getJDA().getTextChannelById("944154297615151134").sendMessage("Stworzono ticket " + textChannel.getAsMention()).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
            textChannel.sendMessage(discordBot.getEmbedBuilder().addField("Zamknij ticket",
                                    "Kliknij aby zakonczyc rozmowe z administracja", true)
                            .setColor(ConstantsHelper.color)
                            .setFooter("MineClub.PL - Tickety", ConstantsHelper.imgUrl)
                            .build())
                    .setActionRows(
                            ActionRow.of(
                                    Button.of(ButtonStyle.DANGER, "close", "Zamknij ticket")
                            )).queue();
            discordBot.getEmbedBuilder().clear();
        }
    }

}
