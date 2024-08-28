package pl.mineclub.bot.events;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import pl.mineclub.bot.helpers.ConstantsHelper;
import pl.mineclub.bot.instance.BotInstance;

import java.awt.*;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MessageEvent extends ListenerAdapter {

    private static final String COMMAND_ROLE_NAME = "Permisja: Komendy bota";


    public Role findRole(Member member, String name) {
        return member.getRoles().stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        if (!content.isEmpty()) {
            if (event.getMember() == null) return;
            if (findRole(Objects.requireNonNull(event.getMember()), "Permisja: Link") == null) {
                if (event.getMessage().getContentRaw().startsWith("http") || event.getMessage().getContentRaw().startsWith("https") || event.getMessage().getContentRaw().startsWith("www") || event.getMessage().getContentRaw().contains(".pl") || event.getMessage().getContentRaw().contains(".eu") || event.getMessage().getContentRaw().contains(".ru") || event.getMessage().getContentRaw().contains(".com") || event.getMessage().getContentRaw().contains(".br") || event.getMessage().getContentRaw().contains(".fr") || event.getMessage().getContentRaw().contains(".uk") || event.getMessage().getContentRaw().contains("invite/") || event.getMessage().getContentRaw().contains("discord.gg") || event.getMessage().getContentRaw().contains(".gg")) {
                    event.getMessage().delete().queue();
                }
            }
        }
        String message = event.getMessage().getContentRaw();
        if (message.startsWith("!")) {
            if (!hasPermission(event)) {
                event.getMessage().reply("Nie masz uprawnień do wykonania tej komendy")  .queue(msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
                return;
            }
            handleCommand(event, message);
            event.getMessage().delete().queueAfter(1, TimeUnit.SECONDS);
        }
    }


    private void handleCommand(MessageReceivedEvent event, String message) {
        String[] parts = message.substring(1).split(" ", 2);
        String command = parts[0].toLowerCase();

        switch (command) {
            case "say": {
                if (parts.length > 1) {
                    String response = parts[1];
                    event.getChannel().sendMessage(response).queue();
                } else {
                    event.getMessage().reply("Proszę podać wiadomość do powtórzenia")
                            .queue(msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
                }
                break;
            }

            case "embed": {
                if (parts.length > 1) {
                    String response = parts[1];
                    createEmbed(event, response);
                } else {
                    event.getChannel().sendMessage("Proszę podać tytuł i opis dla embed.")
                            .queue(msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
                }
                break;
            }

            default:
                event.getMessage().reply("Nieznana komenda.").queue(msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
        }
    }


    private void createEmbed(MessageReceivedEvent event, String response) {
        String[] embedParts = response.split(" "); // Split the response using spaces

        if (embedParts.length < 2) {
            event.getChannel().sendMessage("Proszę podać przynajmniej tytuł i opis dla embed.")
                    .queue(msg -> msg.delete().queueAfter(3, TimeUnit.SECONDS));
            return;
        }

        String title = embedParts[0]; // First word as title
        String description = String.join(" ", java.util.Arrays.copyOfRange(embedParts, 1, embedParts.length - 1)); // All words except first and last as description
        String lastPart = embedParts[embedParts.length - 1]; // Last word as potential image URL

        String imageUrl = null;
        if (lastPart.startsWith("http://") || lastPart.startsWith("https://")) {
            imageUrl = lastPart; // If the last word is a URL, use it as the image URL
            description = String.join(" ", java.util.Arrays.copyOfRange(embedParts, 1, embedParts.length - 1));
        } else {
            description = String.join(" ", java.util.Arrays.copyOfRange(embedParts, 1, embedParts.length));
        }
        EmbedBuilder embed = BotInstance.getInstance().getEmbedBuilder();
        embed.setTitle(title);
        embed.setDescription(description);
        embed.setColor(new Color(0x0080ff));
        embed.setFooter("MineClub.PL", ConstantsHelper.imgUrl);
        embed.setTimestamp(Instant.now());
        if (imageUrl != null && !imageUrl.isEmpty()) {
            embed.setImage(imageUrl);
        }
        event.getChannel().sendMessageEmbeds(embed.build()).queue();
    }

    private boolean hasPermission(MessageReceivedEvent event) {
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

    private Role getRoleByName(MessageReceivedEvent event, String roleName) {
        return event.getGuild().getRolesByName(roleName, true).stream().findFirst().orElse(null);
    }

    private boolean isHigherRole(net.dv8tion.jda.api.entities.Member member, Role role) {
        return member.getRoles().stream().anyMatch(memberRole -> memberRole.getPosition() > role.getPosition());
    }


    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equals("verify_button")) {
            generateQuestion();
            String question = "Podaj wynik działania: " + num1 + " + " + num2;
            Modal modal = Modal.create("verification_modal", "Weryfikacja")
                    .addActionRow(TextInput.create("answer", question, TextInputStyle.SHORT)
                            .setRequired(true)
                            .build())
                    .build();
            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("verification_modal")) {
            String userInput = event.getValue("answer").getAsString();
            if (answer == Integer.parseInt(userInput)) {
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

    private int num1;
    private int num2;
    private int answer;



    private void generateQuestion() {
        num1 = 4;
        num2 = 3;
        answer = num1 + num2;
    }
}
