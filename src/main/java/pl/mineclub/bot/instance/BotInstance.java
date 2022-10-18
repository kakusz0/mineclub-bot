package pl.mineclub.bot.instance;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import pl.mineclub.bot.events.ButtonEvent;
import pl.mineclub.bot.events.JoinEvent;
import pl.mineclub.bot.events.MessageEvent;
import pl.mineclub.bot.events.PropositionEvent;
import pl.mineclub.bot.managers.CommandManager;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BotInstance {
    private final JDA jda;
    private final EmbedBuilder embedBuilder;
    private final ScheduledExecutorService executorService;
    public final CommandManager commandManager;

    public BotInstance() throws InterruptedException, LoginException {
        jda = JDABuilder.createDefault("MTAxMzUyNjY0MTkzMjI1MTE0Ng.G_yq9-.qwq-85vKm8Wcl2nnu9wvmuqfrrNoDnNvzXz8eM", Arrays.asList(GatewayIntent.values()))
                .setAutoReconnect(true)
                .setActivity(Activity.listening("dc.kcode.pl"))
              .build().awaitReady();

        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.commandManager = new CommandManager(this);
        jda.addEventListener(this.commandManager, new MessageEvent(), new JoinEvent(), new PropositionEvent(this), new ButtonEvent(this));
        //this.executorService.scheduleAtFixedRate(new UpdateStatsScheduler(this),1,3,TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if(this.executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.out.println("Async shutdown");
                    this.executorService.shutdown();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));

        this.embedBuilder = new EmbedBuilder();


    }

    public JDA getJDA() {
        return jda;
    }

    public EmbedBuilder getEmbedBuilder() {
        return embedBuilder;
    }
}
