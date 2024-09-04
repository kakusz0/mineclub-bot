package pl.mineclub.bot.events;

import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.apache.commons.text.StringEscapeUtils;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Getter
public class ReactionEvent extends ListenerAdapter {

    private final Map<Long, Map<String, Set<String>>> votes;
    private final Map<Long, Set<String>> userVotes;


    public ReactionEvent() {
        this.votes = new ConcurrentHashMap<>();
        this.userVotes = new ConcurrentHashMap<>();
    }


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("verify_button")) {
            String question = "Podaj wynik działania: " + 3 + " + " + 4;
            Modal modal = Modal.create("verification_modal", "Weryfikacja")
                    .addActionRow(TextInput.create("answer", question, TextInputStyle.SHORT)
                            .setRequired(true)
                            .build()).build();
            event.replyModal(modal).queue();
        } else  if (event.getComponentId().equals("read_")) {
            event.reply("Dziękujemy za przeczytanie!").setEphemeral(true).queue();
        } else if (event.getComponentId().equals("ticket-delete")) {
            event.reply("Czy na pewno chcesz usunąć ten kanał?").setEphemeral(true)
                    .addActionRow(
                            Button.danger("confirm_delete", "Tak, usuń ticket"),
                            Button.secondary("cancel_delete", "Anuluj")
                    ).queue();
        }else if (event.getComponentId().equals("confirm_delete")) {
            event.deferEdit().queue();
            event.getChannel().sendMessage("Kanał zostanie usunięty za 5 sekund...").queue();


            TextChannel textChannel = (TextChannel) event.getChannel();
            closeTicket(textChannel);
        } else if (event.getComponentId().equals("cancel_delete")) {
            event.deferEdit().queue();
            event.getMessage().delete().queue();
        }
        else if (event.getComponentId().startsWith("vote_")) {
            String buttonId = event.getComponentId();
            long messageId;

            try {
                messageId = Long.parseLong(buttonId.split("_")[2]);
            } catch (NumberFormatException e) {
                event.reply("Nieprawidłowy identyfikator wiadomości.").setEphemeral(true).queue();
                return;
            }

            // Check if the messageId exists in the votes map
            if (!votes.containsKey(messageId)) {
                event.reply("Wiadomość z tą propozycją nie została znaleziona.").setEphemeral(true).queue();
                return;
            }

            String userId = event.getUser().getId();

            // Check if the user has already voted
            if (userVotes.get(messageId).contains(userId)) {
                event.reply("Już zagłosowałeś!").setEphemeral(true).queue();
                return;
            }

            // Retrieve and update the message
            Message message = event.getMessage();
            List<Button> buttons = message.getComponents().get(0).getButtons(); // Get buttons from the first action row

            Button yesButton = getButtonById(buttons, "vote_yes_" + messageId);
            Button noButton = getButtonById(buttons, "vote_no_" + messageId);

            // Handle voting logic
            if (yesButton != null && buttonId.equals(yesButton.getId())) {
                // User is voting YES
                if (votes.get(messageId).get("vote_no").contains(userId)) {
                    event.reply("Nie możesz głosować na TAK i NIE.").setEphemeral(true).queue();
                    return;
                }

                votes.get(messageId).get("vote_yes").add(userId);
                userVotes.get(messageId).add(userId);

                yesButton = Button.primary("vote_yes_" + messageId, "TAK: " + (getCountFromLabel(yesButton.getLabel()) + 1));
            } else if (noButton != null && buttonId.equals(noButton.getId())) {
                // User is voting NO
                if (votes.get(messageId).get("vote_yes").contains(userId)) {
                    event.reply("Nie możesz głosować na TAK i NIE.").setEphemeral(true).queue();
                    return;
                }

                votes.get(messageId).get("vote_no").add(userId);
                userVotes.get(messageId).add(userId);

                noButton = Button.danger("vote_no_" + messageId, "NIE: " + (getCountFromLabel(noButton.getLabel()) + 1));
            } else {
                event.reply("Nieprawidłowy przycisk głosowania.").setEphemeral(true).queue();
                return;
            }

            // Update the message with the current votes
            message.editMessageComponents(
                    net.dv8tion.jda.api.interactions.components.ActionRow.of(yesButton, noButton)
            ).queue();

            event.reply("Głos dodany!").setEphemeral(true).queue();
        }


    }

    private Button getButtonById(List<Button> buttons, String buttonId) {
        return buttons.stream().filter(button -> button.getId().equals(buttonId)).findFirst().orElse(null);
    }

    private int getCountFromLabel(String label) {
        try {
            String countStr = label.split(": ")[1];
            return Integer.parseInt(countStr);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public void closeTicket(TextChannel channel) {
        List<Message> messages = getAllMessages(channel);

        StringBuilder contentBuilder = new StringBuilder();
        for (Message message : messages) {
            String authorName = message.getAuthor().getName();
            String messageContent = (message.getContentRaw());
            String timestamp = message.getTimeCreated().toLocalDateTime().toString();

            contentBuilder.append("[").append(timestamp).append("] ")
                    .append(authorName + " " + message.getAuthor().getAsMention()).append(": ")
                    .append(messageContent).append("\n");
        }

        saveTicket(channel, contentBuilder.toString());
    }

    private List<Message> getAllMessages(MessageChannel channel) {
        CompletableFuture<List<Message>> futureMessages = new CompletableFuture<>();
        List<Message> allMessages = new java.util.ArrayList<>();

        fetchMessages(channel, allMessages, futureMessages);

        try {
            return futureMessages.get(); // Wait for the future to complete
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return allMessages;
        }
    }


    public void saveTicket(TextChannel channel, String content) {
        String sql = "INSERT INTO mineclub_tickets (channel_id, channel_name, content) VALUES (?, ?, ?)";

        try (Connection connection = BotInstance.getInstance().getMysqlManager().getHikariDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, channel.getIdLong());
            statement.setString(2, channel.getName());
            statement.setString(3, content);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            channel.sendMessage("Nie udało się zapisać ticketu do bazy danych, sprobuj za chwilę").queue();
        } finally {
            channel.delete().queueAfter(5, TimeUnit.SECONDS);
        }
    }

    private void fetchMessages(MessageChannel channel, List<Message> allMessages, CompletableFuture<List<Message>> futureMessages) {
        channel.getHistory().retrievePast(100).queue(messages -> {
            allMessages.addAll(messages);
            if (messages.size() == 100) {
                fetchMessages(channel, allMessages, futureMessages);
            } else {
                futureMessages.complete(allMessages);
            }
        });
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("verification_modal")) {
            String userInput = event.getValue("answer").getAsString();
            if (Integer.parseInt(userInput) == 7) {
                Member member = event.getMember();
                if (member != null) {
                    Role role = event.getGuild().getRoleById(1260841458815209554L);
                    if (role != null) {
                        event.getGuild().addRoleToMember(member, role).queue(
                                success -> event.reply("Weryfikacja zakończona pomyślnie. Rolę przydzielono!").setEphemeral(true).queue(),
                                failure -> event.reply("Wystąpił problem podczas przydzielania roli.").setEphemeral(true).queue()
                        );
                    } else {
                        event.reply("Nie znaleziono roli.").setEphemeral(true).queue();
                    }
                } else {
                    event.reply("Nie udało się znaleźć użytkownika.").setEphemeral(true).queue();
                }
            } else {
                event.reply("Błędna odpowiedź. Spróbuj ponownie!").setEphemeral(true).queue();
            }
        }
    }
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        String selected = event.getValues().get(0);
        if (event.getComponentId().equals("rules-menu")) {
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
        else if (event.getComponentId().equals("info-menu")) {
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
        else if (event.getComponentId().equals("ticket-menu")) {
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
            if (guild.getTextChannels().stream().anyMatch(channel -> channel.getName().contains(member.getUser().getName()))) {
                event.reply("Masz już otwarty ticket!").setEphemeral(true).queue();
                return;
            }

            // Tworzenie kanału tekstowego w ramach wybranej kategorii
            category.createTextChannel("ticket-" + member.getUser().getName())
                    .setTopic(ticketTitle + " - " + member.getUser().getAsTag())
                    .queue(textChannel -> {
                        Role role1 = guild.getRoleById(1260830294844641342L);
                        Role role2 = guild.getRoleById(1260827364666114129L);
                        EmbedBuilder embed = BotInstance.getInstance().getEmbedBuilder();
                        embed.setTitle("MineClub | Ticket");
                        embed.setDescription("**Ticket utworzony przez: " + member.getAsMention() + "**" +
                                "\nKategoria: " + ticketTitle);
                        embed.setColor(new Color(0x0080ff));
                        embed.setFooter("MineClub.PL", ConstantsHelper.imgUrl);
                        embed.setTimestamp(Instant.now());
                        if (role1 != null && role2 != null) {
                            textChannel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singleton(Permission.VIEW_CHANNEL)).queue();
                            textChannel.getManager().putPermissionOverride(member, Collections.singleton(Permission.VIEW_CHANNEL), null).queue();
                            textChannel.getManager().putPermissionOverride(role1, Collections.singleton(Permission.ADMINISTRATOR), null).queue();
                            textChannel.getManager().putPermissionOverride(role2, Collections.singleton(Permission.ADMINISTRATOR), null).queue();
                            textChannel.sendMessageEmbeds(embed.build()).setActionRow(Button.danger("ticket-delete", "Usuń ticket")).queue();

                            textChannel.sendMessage(role1.getAsMention()).queue();
                            textChannel.sendMessage(role2.getAsMention()).queue();
                        }
                        textChannel.sendMessage(member.getAsMention() + " Opisz swój problem, a administracja wkrótce się z Tobą skontaktuje.").queue();



                        event.reply("Twój ticket został utworzony: " + textChannel.getAsMention()).setEphemeral(true).queue();
                    }, failure -> {
                        event.reply("Nie udało się utworzyć ticketa. Spróbuj ponownie później.").setEphemeral(true).queue();
                    });
        }
    }


}
