package FitBot;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import java.util.concurrent.*;

import com.google.common.util.concurrent.AbstractScheduledService.Scheduler;

import static java.util.concurrent.TimeUnit.*;

import java.util.ArrayList;

public class TimedEventsHandler {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    Runnable dailyUpdate;
    Runnable weeklyUpdate;
    Runnable eocUpdate;
    ScheduledFuture<?> dailyUpdateHandle;
    ScheduledFuture<?> weeklyUpdateHandle;
    ScheduledFuture<?> endOfCycleUpdateHandle;
    Runnable stopUpdatesForCurrentCycle;
    Runnable endBreak;
    ScheduledFuture<?> endBreakHandle;

    private void dailyUpdate(SlashCommandEvent event) {
        ArrayList<String> kickedUsers = new ArrayList<>();
        String curUser;
        for (int i = 0; i < event.getGuild().getMemberCount(); i++) {
            curUser = event.getGuild().getMembers().get(i).getId();
            if (DatabaseUtility.checkIfUserIsInCycle(curUser)) {
                if (!DatabaseUtility.checkUserDidWorkoutToday(event.getUser().getId())) {
                    DatabaseUtility.decrementUserFreeDays(event.getUser().getId());
                }
                if (DatabaseUtility.checkNegativeFreeDaysForUser(event.getUser().getId())) {
                    kickedUsers.add(event.getUser().getName());
                    DatabaseUtility.removeUser(event.getUser());
                    event.getChannel().sendMessage(String.format("Hi '%s', it looks like you missed your workout for more than two days this week. Goodbye.",event.getUser().getAsMention()))
                    .queue();
                }
            }
        }
        try {
            event.getChannel().sendMessage("<daily update>").queue();
        } catch (Exception e) {e.printStackTrace();}
    }

    private void weeklyUpdate(SlashCommandEvent event) {
        String curUser;
        for (int i = 0; i < event.getGuild().getMemberCount(); i++) {
            curUser = event.getGuild().getMembers().get(i).getId();
            if (DatabaseUtility.checkIfUserIsInCycle(curUser)) {
                DatabaseUtility.refreshFreeDays(event.getGuild());
            }
        }
        DatabaseUtility.refreshFreeDays(event.getGuild());
        try {
            event.getChannel().sendMessage("<weekly update>").queue();
        } catch (Exception e) {e.printStackTrace();} 
    }

    private void endOfCycleUpdate(SlashCommandEvent event) {
        try {
            event.getChannel().sendMessage("<eoc update>").queue();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void cancelTimedEvents() {
        this.dailyUpdateHandle.cancel(false); 
        this.endOfCycleUpdateHandle.cancel(false);
    }

    public void endBreakTimed(User user, int days) {
        Runnable endBreak = () -> DatabaseUtility.deactivateBreak(user);
        this.endBreakHandle = scheduler.schedule(endBreak, days, SECONDS);
    }

    public void endBreakImmediate(User user) {
        // ends break for the specified user, and cancels the timed endbreak triggered by the break command response.
        if (!(this.endBreakHandle == null)) this.endBreakHandle.cancel(false);
        DatabaseUtility.deactivateBreak(user);
    }

    public void resumeUpdate() {
        //TODO
    }

    public void handleUpdates(SlashCommandEvent event) {
        this.weeklyUpdate = () -> weeklyUpdate(event);
        this.dailyUpdate = () -> dailyUpdate(event); 
        this.eocUpdate = () -> endOfCycleUpdate(event);
        this.weeklyUpdateHandle = scheduler.scheduleAtFixedRate(weeklyUpdate, 70, 70, SECONDS);
        this.dailyUpdateHandle = scheduler.scheduleAtFixedRate(dailyUpdate, 10, 10, SECONDS);
        this.endOfCycleUpdateHandle = scheduler.scheduleAtFixedRate(eocUpdate, DatabaseUtility.getCycleDurationInDays(), 10, SECONDS);
        this.stopUpdatesForCurrentCycle = () -> {
            dailyUpdateHandle.cancel(false); 
            endOfCycleUpdateHandle.cancel(false);
            weeklyUpdateHandle.cancel(false);
        };
        scheduler.schedule(stopUpdatesForCurrentCycle, DatabaseUtility.getCycleDurationInDays()*10, SECONDS);
        //event.getGuild().getMembers().forEach((x) -> DatabaseUtility.deactivateBreak(x.getUser()));
    }

}
