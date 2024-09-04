package pl.mineclub.bot.events;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.mineclub.bot.instance.BotInstance;

public class UserEvent extends ListenerAdapter {

    private final long ROLE_ID = 1260836309518254091L;    // Zamień na ID roli, którą chcesz przyznawać

    @Override
    public void onUserUpdateActivities(UserUpdateActivitiesEvent event) {
        String activityStatus = event.getMember().getActivities().stream()
                .filter(activity -> {
                    activity.getName();
                    return true;
                })
                .map(Activity::getName)
                .findFirst()
                .orElse("");


        if (activityStatus.equals("dc.mineclub.pl")) {
            assignRole(event.getMember());
        } else {
            removeRole(event.getMember());
        }
    }

    private void assignRole(Member member) {
        Guild guild = BotInstance.getInstance().getJda().getGuilds().get(0);
        if (guild != null) {
            guild.addRoleToMember(member, guild.getRoleById(ROLE_ID)).queue();
        }
    }

    private void removeRole(Member member) {
        Guild guild = BotInstance.getInstance().getJda().getGuilds().get(0);
        if (guild != null) {
            guild.removeRoleFromMember(member, guild.getRoleById(ROLE_ID)).queue();
        }
    }


}
