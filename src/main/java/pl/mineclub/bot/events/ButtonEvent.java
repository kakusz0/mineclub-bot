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

    private SelectionMenuEvent event;


    @Override
    public void onSelectionMenu(SelectionMenuEvent event) {
        this.event = event;
        if (event.getMember().getGuild() != discordBot.getJDA().getGuildById(943448858917228544L)) return;
        if(activatedTickets.contains(event.getMember())) {
            event.reply("Dopiero stworzyłeś ticket!").setEphemeral(true).queue(message -> message.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
            return;
        }
        if (findValue(event, "blad").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1013814607988670514").createTextChannel("blad-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
        if (findValue(event, "backup").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1013814607988670514").createTextChannel("backup-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
        if (findValue(event, "strona").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1013814607988670514").createTextChannel("strona-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
        if (findValue(event, "partner").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1013814607988670514").createTextChannel("partnerstwo-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
        if (findValue(event, "media").isPresent()) {
            Guild guild = event.getMember().getGuild();
            guild.getCategoryById("1013814607988670514").createTextChannel("media-" + event.getInteraction().getUser().getAsTag().replace("#", ""))
                    .addPermissionOverride(event.getMember(), EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .queue();
        }
    }


    private final ButtonImpl closedButton = new ButtonImpl("fsdfdsfds", "Zamknij ticket", ButtonStyle.DANGER, true, Emoji.fromUnicode("U+2716"));

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getMember().getGuild() != discordBot.getJDA().getGuildById(943448858917228544L)) return;
        if (event.getComponentId().equals("verify")) {
            List<Role> roleList = new ArrayList<>(Arrays.asList(event.getGuild().getRoleById("1008688791009112065"), event.getGuild().getRoleById("1008688936614375544")));
            roleList.forEach(role -> event.getGuild().addRoleToMember(event.getMember(), role).queue());
            event.reply("Pomyślnie zweryfikowano!").setEphemeral(true).queue(message -> message.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
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
        if (textChannel.getParent().getName().contains("ticketów")) {
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
