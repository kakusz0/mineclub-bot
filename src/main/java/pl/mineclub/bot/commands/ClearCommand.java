package pl.mineclub.bot.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.internal.entities.RoleImpl;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class ClearCommand implements CommandImpl {

    private final OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

    public Role findRole(Member member, String name) {
        return member.getRoles().stream().filter(role -> role.getName().equals(name)).findFirst().orElse(null);
    }
    @Override
    public void executeCommand(SlashCommandEvent event, Member member, TextChannel textChannel) {
        Role role = new RoleImpl(1008389781736140932L, member.getGuild());
        if (findRole(Objects.requireNonNull(event.getMember()), "\u2063           MineClub Team") == null) {
            event.reply("nie mozesz").queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        List<Message> messages = textChannel.getHistory().retrievePast(Integer.parseInt(event.getOption("amount").getAsString())).complete();
        messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));
        if(messages.isEmpty() || messages.size() < 2 || messages.size() > 100){
            event.reply("Mozesz usunac wiecej niz 1 wiadomosc lub mniej niz 100").queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        textChannel.deleteMessages(messages).queue();
        event.reply("Usunięto " + messages.size() + " wiadomości").queue(message -> message.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
    }
}
