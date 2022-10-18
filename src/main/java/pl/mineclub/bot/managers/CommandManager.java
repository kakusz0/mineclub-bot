package pl.mineclub.bot.managers;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import pl.mineclub.bot.commands.*;
import pl.mineclub.bot.instance.BotInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class CommandManager extends ListenerAdapter {

    private final Map<String, CommandImpl> commandMap;
    private final BotInstance discordBot;

    public CommandManager(BotInstance discordBot) {
        this.discordBot = discordBot;
        this.commandMap = new ConcurrentHashMap<>();
        this.commandMap.put("clear", new ClearCommand());
        this.commandMap.put("ogloszenie", new OgloszenieCommand(discordBot));
        this.commandMap.put("komentarz", new CommentCommand(discordBot));
        this.commandMap.put("message", new MessageCommand(discordBot));
//        this.commandMap.put("changelog", new ChangelogCommand(discordBot));
        CommandListUpdateAction commands = discordBot.getJDA().updateCommands();
        commands.addCommands(
                new CommandData("clear", "Wiadomosci do usuniecia").addOption(OptionType.INTEGER, "amount", "ilosc wiadomosci do usuniecia", true),
                new CommandData("ogloszenie", "ODSTĘP MIEDZY WIADOMOSCIAMI: {n}").addOption(OptionType.STRING, "amount", "ODSTĘP MIEDZY WIADOMOSCIAMI: {n}", true),
                new CommandData("message", "ODSTĘP MIEDZY WIADOMOSCIAMI: {n}").addOption(OptionType.STRING, "title", "tytul embeda", true).addOption(OptionType.STRING, "content", "ODSTĘP MIEDZY WIADOMOSCIAMI: {n}", true).addOption(OptionType.STRING, "footer", "opis footera", true).addOption(OptionType.STRING, "color", "kolor", true),
                new CommandData("komentarz", "no zarycz no").addOption(OptionType.STRING, "amount", "ODSTĘP MIEDZY WIADOMOSCIAMI: {n}", true)
//                new CommandData("changelog", "ODSTĘP MIEDZY WIADOMOSCIAMI: {n}").addOption(OptionType.STRING, "amount", "ODSTĘP MIEDZY WIADOMOSCIAMI: {n}", true)



        );
        commands.queueAfter(1, TimeUnit.SECONDS);
    }


    @Override
    public void onSlashCommand(SlashCommandEvent event){
        String commandName = event.getName();
        CommandImpl command = commandMap.get(commandName);
        if(command != null) {
            //System.out.println("wykonano komende " + commandName);
            if (event.getMember().getGuild() != discordBot.getJDA().getGuildById(943448858917228544L)) return;
            command.executeCommand(event, event.getMember(), event.getTextChannel());
            discordBot.getEmbedBuilder().clear();
        }
    }
}
