package pl.mineclub.bot.instance;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import pl.mineclub.bot.commands.CommandManager;
import pl.mineclub.bot.events.*;
import pl.mineclub.bot.managers.MySQLManager;
import pl.mineclub.bot.runnables.UpdateAnkietaScheduler;
import pl.mineclub.bot.runnables.UpdateStatsScheduler;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class BotInstance  {
    private final JDA jda;
    private final EmbedBuilder embedBuilder;
    private final ScheduledExecutorService executorService;
    private final MySQLManager mysqlManager;
    private int currentIndex = 0;
    private ReactionEvent reactionEvent;

    @Getter
    private static BotInstance instance;


    public BotInstance() throws InterruptedException, LoginException {
        instance = this;
        this.jda = JDABuilder.create(
                  "MTI4MDk4Mzc2OTg3MTk0NTc4MA.GeCZJb.bqSmOPYKY4Xe-gLx9lgDW40wnnd0ZZXr-UrE1c",
                        EnumSet.allOf(GatewayIntent.class))
                .setAutoReconnect(true)
                .setActivity(Activity.playing("na serwerze MineClub.PL"))
                .build().awaitReady();



        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.mysqlManager = new MySQLManager("localhost", 3306, "trzeci", "24039rgtnsddeasef2w3", "mineclub");

        this.executorService.scheduleWithFixedDelay(() -> {
            if(currentIndex == 0) {
                jda.getPresence().setActivity(Activity.playing("na serwerze mineclub.pl"));
            } else {
                jda.getPresence().setActivity(Activity.watching("stronę mineclub.pl"));
            }
            currentIndex++;
            if (this.currentIndex > 1) currentIndex = 0;
        }, 0, 30, TimeUnit.SECONDS);


        NumberEvent numberEvent = new NumberEvent();
        CommandManager commandManager = new CommandManager();
        this.reactionEvent = new ReactionEvent();
        this.jda.addEventListener(new UserEvent(), commandManager, this.reactionEvent, new MessageEvent(), new JoinEvent(), numberEvent);
        commandManager.registerCommands();
        this.executorService.scheduleAtFixedRate(new UpdateStatsScheduler(this),1,3,TimeUnit.SECONDS);
        this.executorService.scheduleAtFixedRate(new UpdateAnkietaScheduler(this),1,3,TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutdown hook initiated.");

            numberEvent.saveNumbersToDatabase();
            executorService.shutdown();

            try {
                if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                    System.out.println("Executor did not terminate in the specified time.");
                    executorService.shutdownNow();

                    if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                        System.err.println("Executor did not terminate after shutdownNow.");
                    }
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted during shutdown.");
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }));

        this.embedBuilder = new EmbedBuilder();
        try {
            numberEvent.loadNumbersFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}