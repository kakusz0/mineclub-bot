package pl.mineclub.bot.instance;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import pl.mineclub.bot.events.JoinEvent;
import pl.mineclub.bot.events.MessageEvent;
import pl.mineclub.bot.events.NumberEvent;
import pl.mineclub.bot.events.ReactionEvent;
import pl.mineclub.bot.managers.MySQLManager;
import pl.mineclub.bot.runnables.UpdateAnkietaScheduler;
import pl.mineclub.bot.runnables.UpdateStatsScheduler;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class BotInstance {
    private final JDA jda;
    private final EmbedBuilder embedBuilder;
    private final ScheduledExecutorService executorService;
    private final MySQLManager mysqlManager;
    private int currentIndex = 0;

    @Getter
    private static BotInstance instance;

    public BotInstance() throws InterruptedException, LoginException {
        instance = this;
        this.jda = JDABuilder.createDefault("MTI3MjkzOTM0MTk3NDMzOTY2OA.GcoaZZ.CEPv0uogkgZ4aaYEd3mM-40Vv9gRS_9UCiy2_k", Arrays.asList(GatewayIntent.values()))
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
        this.jda.addEventListener(new ReactionEvent(), new MessageEvent(), new JoinEvent(), numberEvent);
        this.executorService.scheduleAtFixedRate(new UpdateStatsScheduler(this),1,3,TimeUnit.SECONDS);
        this.executorService.scheduleAtFixedRate(new UpdateAnkietaScheduler(this),1,3,TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                try {
                    numberEvent.saveNumbersToDatabase();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if (this.executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Async shutdown");

                    this.executorService.shutdown();

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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