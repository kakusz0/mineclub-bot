package pl.mineclub.instance;


import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Getter
@Setter
public class BotInstance {

    private ScheduledExecutorService scheduledExecutorService;
    private JDA jda;

    public BotInstance() throws InterruptedException {
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        this.jda = JDABuilder.create("MTAxMzUyNjY0MTkzMjI1MTE0Ng.GMwmrr.0fbSYpbdMRvz1ul1s4Um9Oe2dOr7gRfWkHOKEw", Arrays.asList(GatewayIntent.values()))
                .setActivity(Activity.playing("na serwerze MineClub.PL"))
                .setAutoReconnect(true).build().awaitReady();
    }

}
