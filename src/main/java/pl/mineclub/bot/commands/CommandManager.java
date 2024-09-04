package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.Color;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CommandManager extends ListenerAdapter {
    private static final String COMMAND_ROLE_NAME = "Permisja: Komendy bota";

    public Role findRole(Member member, String name) {
        return member.getRoles().stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
    }
    private boolean hasPermission(SlashCommandInteractionEvent event) {
        net.dv8tion.jda.api.entities.Member member = event.getMember();
        if (member == null) {
            return false;
        }
        List<Role> roles = member.getRoles();
        Role commandRole = getRoleByName(event, COMMAND_ROLE_NAME);
        if (commandRole == null) {
            return false;
        }
        return roles.contains(commandRole) || isHigherRole(member, commandRole);
    }
    private Role getRoleByName(SlashCommandInteractionEvent event, String roleName) {
        return event.getGuild().getRolesByName(roleName, true).stream().findFirst().orElse(null);
    }

    private boolean isHigherRole(net.dv8tion.jda.api.entities.Member member, Role role) {
        return member.getRoles().stream().anyMatch(memberRole -> memberRole.getPosition() > role.getPosition());
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String command = event.getName();
        if (!hasPermission(event)) {
            event.reply("Nie masz uprawnień do wykonania tej komendy")  .queue(msg -> msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
            return;
        }
        if (command.equals("clear")) {
            OptionMapping option = event.getOption("amount");
            int type = option.getAsInt();

            TextChannel channel = event.getChannel().asTextChannel();
            int amount;
            try {
                amount = type;
                if (amount < 2 || amount > 100) {
                    event.reply("Możesz usunąć od 2 do 100 wiadomości.").setEphemeral(true)
                            .queue(msg -> msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
                    return;
                }
            } catch (NumberFormatException e) {
                event.reply("Niepoprawny format liczby.").setEphemeral(true)
                        .queue(msg -> msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
                return;
            }
            channel.getHistory().retrievePast(amount).queue(
                    messages -> {
                        // Oddzielanie wiadomości starszych i młodszych niż 2 tygodnie
                        List<Message> oldMessages = messages.stream()
                                .filter(msg -> msg.getTimeCreated().isBefore(java.time.OffsetDateTime.now().minusWeeks(2)))
                                .collect(Collectors.toList());
                        List<Message> recentMessages = messages.stream()
                                .filter(msg -> !oldMessages.contains(msg))
                                .collect(Collectors.toList());

                        // Usuwanie wiadomości młodszych niż 2 tygodnie
                        if (!recentMessages.isEmpty()) {
                            channel.deleteMessages(recentMessages).queue(
                                    success -> event.reply("Usunięto " + recentMessages.size() + " wiadomości.").setEphemeral(true).queue(msg -> msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS)),
                                    failure -> event.reply("Nie udało się usunąć wiadomości.").setEphemeral(true).queue(msg -> msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS))
                            );
                        }

                        // Usuwanie wiadomości starszych niż 2 tygodnie, pojedynczo
                        AtomicInteger counter = new AtomicInteger(oldMessages.size());
                        for (Message oldMessage : oldMessages) {
                            oldMessage.delete().queue(
                                    success -> {
                                        if (counter.decrementAndGet() == 0) {
                                            // Wysłanie odpowiedzi po usunięciu wszystkich starszych wiadomości
                                            event.reply("Usunięto wszystkie starsze wiadomości.").setEphemeral(true)
                                                    .queue(msg -> msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
                                        }
                                    },
                                    failure -> {
                                        if (counter.decrementAndGet() == 0) {
                                            // Wysłanie odpowiedzi po zakończeniu próby usunięcia wszystkich starszych wiadomości, nawet jeśli niektóre się nie powiodły
                                            event.reply("Niektóre starsze wiadomości nie mogły zostać usunięte.").setEphemeral(true)
                                                    .queue(msg -> msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
                                        }
                                    }
                            );
                        }
                    },
                    failure -> event.reply("Nie udało się pobrać historii wiadomości").setEphemeral(true).queue(msg -> msg.deleteOriginal().queueAfter(3, TimeUnit.SECONDS))
            );

        }
        else if (command.equals("say")) {
            OptionMapping option = event.getOption("tresc");
            String type = option.getAsString();
            event.getChannel().sendMessage(type).queue();
            event.deferReply().queue(msg -> msg.deleteOriginal().queueAfter(1, TimeUnit.SECONDS));
        }
        else if (command.equals("embed")) {
            OptionMapping tytulOption = event.getOption("tytul");
            String tytul = tytulOption.getAsString();

            OptionMapping opisOption = event.getOption("opis");
            String opis = opisOption.getAsString();


            String url;
            OptionMapping urlOption = event.getOption("url");
            url = urlOption == null ? null : urlOption.getAsString();


            OptionMapping buttonOption = event.getOption("button");
            createEmbed(event, tytul, opis, url, buttonOption == null ? null : buttonOption.getAsBoolean(), 1);





            event.deferReply().queue(msg -> msg.deleteOriginal().queueAfter(1, TimeUnit.SECONDS));
        }
        else if (command.equals("ogloszenie")) {
            OptionMapping tytulOption = event.getOption("tytul");
            String tytul = tytulOption.getAsString();

            OptionMapping opisOption = event.getOption("opis");
            String opis = opisOption.getAsString();


            String url;
            OptionMapping urlOption = event.getOption("url");
            url = urlOption == null ? null : urlOption.getAsString();


            OptionMapping buttonOption = event.getOption("button");
            createEmbed(event, tytul, opis, url, buttonOption == null ? null : buttonOption.getAsBoolean(), 2);





            event.deferReply().queue(msg -> msg.deleteOriginal().queueAfter(1, TimeUnit.SECONDS));  }
        else if (command.equals("emote")) {
            OptionMapping option = event.getOption("type");
            String type = option.getAsString();

            String replyMessage = "";
            switch (type.toLowerCase()) {
                case "hug" -> replyMessage = "You hug the closest person to you.";
                case "laugh" -> replyMessage = "You laugh hysterically at everyone around you.";
                case "cry" -> replyMessage = "You can't stop crying";
            }
            event.reply(replyMessage).queue();
        }
    }

    private void createEmbed(SlashCommandInteractionEvent event, String tytul, String opis, String url, Boolean button, int type) {
        EmbedBuilder embed = BotInstance.getInstance().getEmbedBuilder();
        embed.setTitle(tytul);
        embed.setDescription(opis);
        embed.setColor(new Color(0x0080ff));
        embed.setFooter("MineClub.PL", ConstantsHelper.imgUrl);
        embed.setTimestamp(Instant.now());
        if (url != null && !url.isEmpty() && !url.isBlank()) {
            embed.setImage(url);
        }

        switch (type) {
            case 1 -> {
                if (button != null && button) {
                    Button buttonClick = Button.success("read_", "✅");
                    event.getChannel().sendMessageEmbeds(embed.build()).setActionRow(buttonClick).queue();
                } else {
                    event.getChannel().sendMessageEmbeds(embed.build()).queue();
                }
            }
            case 2 -> {
                Button buttonClick = Button.primary("read_", "Przeczytane");
                event.getGuild().getTextChannelById(1260820866015694930L).sendMessageEmbeds(embed.build()).setActionRow(buttonClick).queue();
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }


        embed.clear();
    }




    public void registerCommands() {

        List<CommandData> commandData = new ArrayList<>();

        commandData.add(Commands.slash("clear", "usun wiadomosci").addOptions(
                new OptionData(OptionType.STRING, "amount", "liczba wiadomosci", true)));


        commandData.add(Commands.slash("say", "napisz wiadomosc przez bota").addOptions(
                new OptionData(OptionType.STRING, "tresc", "tresc wiadomosci", true)));

        commandData.add(Commands.slash("embed", "napisz embed przez bota").addOptions(
                new OptionData(OptionType.STRING, "tytul", "tytul embeda", true),
                new OptionData(OptionType.STRING, "opis", "opis embeda", true),
                new OptionData(OptionType.STRING, "url", "url obrazka do embeda", false),
                new OptionData(OptionType.BOOLEAN, "button", "przycisk do embeda", false)


        ));


        commandData.add(Commands.slash("ogloszenie", "Wysyła ogłoszenie w formie embedu").addOptions(
                new OptionData(OptionType.STRING, "tytul", "tytul embeda", true),
                new OptionData(OptionType.STRING, "opis", "opis embeda", true),
                new OptionData(OptionType.STRING, "url", "url obrazka do embeda", false)    ));



        BotInstance.getInstance().getJda().getGuilds().get(0).updateCommands().addCommands(commandData).queue();
    }

}
