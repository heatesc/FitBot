
package FitBot;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.StackWalker.Option;
import java.util.Arrays;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class Bot extends ListenerAdapter
{
    public static void main(String[] args) throws LoginException
    {
        final String TOKEN = "ODk3MzQ3OTY1MzY1MTMzMzU1.YWUWag.-4JwT_DD6siMFViv0RwHZgX4feE";
        // We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
        // All other events will be disabled.
        JDA jda  = JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
            .addEventListeners(new Bot())
            .build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //jda.upsertCommand("track", "track a workout").queue(); // This can take up to 1 hour to show up in the client
        jda.getGuildById("848849465850462281").upsertCommand("track", "track a workout")
            .addOption(OptionType.STRING, "description", "briefly describe your workout")
            .queue(); // WAIT TILL DISCORD API IS UPDATED TO SUPPORT ATTACHMENTS IN SLASH COMMAND OPTIONS
        jda.getGuildById("848849465850462281").upsertCommand("break", "take a break if you need time off. e.g. feeling ill or overloaded with work")
            .addOption(OptionType.STRING, "reason", "What is your reason for taking time off?")
            .addOption(OptionType.INTEGER, "days", "How many days do you plan to take off? (you may end your time off early)")
            .queue(); 
        jda.getGuildById("848849465850462281").upsertCommand("end-break", "take a break if you need time off. e.g. feeling ill or overloaded with work")
            .queue(); 
        jda.getGuildById("848849465850462281").upsertCommand("quit", "quit the server (potentially irreversible)")
            .addOption(OptionType.STRING, "security", "Are you sure (yes/no)?")
            .queue(); 
        jda.getGuildById("848849465850462281").upsertCommand("suggestion", "provide an improvement suggestion for me")
            .addOption(OptionType.STRING, "suggestion", "how should I improve?")
            .queue();
        jda.getGuildById("848849465850462281").upsertCommand("set-aim", "what is your aim for this cycle?")
            .addOption(OptionType.STRING, "aim", "describe your aim")
            .queue();
        jda.getGuildById("848849465850462281").upsertCommand("change-aim", "change your aim for the cycle")
            .addOption(OptionType.STRING, "new-aim", "describe your new aim")
            .queue();
        jda.getGuildById("848849465850462281").upsertCommand("get-data", "what is your aim for this cycle?")
            .queue();
    }
    
    /* @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message msg = event.getMessage();
        
        User user = event.getAuthor();
        Guild server = event.getGuild();
        //System.out.println(msg.getContentDisplay());
        //System.out.println(msg.getContentRaw().matches("(.*)/track(.*)"));
        if (msg.getContentRaw().matches("(.*)/track(.*)") && !msg.getAuthor().isBot())
        {
            
            //System.out.println(msg.getAuthor());
            //System.out.println("ok");
            MessageChannel channel = event.getChannel();
            //System.out.println(channel);
            //long time = System.currentTimeMillis();
            channel.addReactionById(event.getMessageId(), "minn:245267426227388416");
            channel.sendMessage(String.format("Hi %s, your workout has been tracked.",user.getName())).complete();
            //System.out.println("A");
            //System.out.println(server.getSelfMember().hasPermission(Permission.MESSAGE_WRITE));
        }
    }  */

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        System.out.println(event.getName());
        String username = event.getUser().getAsMention();
        if (event.getName().equals("break")) {
            try {
                event.reply(String.format("Hi %s, your time off has now started. You have %d days remaining. If you wish to end your break early use the /endbreak command.",username,event.getOption("days").getAsLong()))
                .queue();
            } catch (Exception e) {e.printStackTrace();}
        } else if (event.getName().equals("track")) {
            try {
                event.reply(String.format("Hi %s, your workout has been tracked.",username)).queue();
            } catch (Exception e) {e.printStackTrace();}
        } else if (event.getName().equals("end-break")) {
            try {
                event.reply(String.format("Hi %s, your break is over.",username)).queue();
            } catch (Exception e) {e.printStackTrace();}
        } else if (event.getName().equals("quit")) {
            try {
                event.reply(String.format("I haven't got the permission to kick server members, so feel free to hop off. Goodbye, %s",username)).queue();
            } catch (Exception e) {e.printStackTrace();}
        } else if (event.getName().equals("suggestion")) {
            try {
                event.reply(String.format("Thanks %s, I will take your suggestion into consideration.",username)).queue();
            } catch (Exception e) {e.printStackTrace();}
        } else if (event.getName().equals("set-aim")) {
            try {
                event.reply(String.format("Hi %s, your aim has been set!\nAim: %s\n\nGood Luck %s!!!",username,event.getOption("aim").getAsString(),username)).queue();
            } catch (Exception e) {e.printStackTrace();}
        } else if (event.getName().equals("change-aim")) {
            try {
                event.reply(String.format("Hi %s, your aim has been modified.",username)).queue();
            } catch (Exception e) {e.printStackTrace();}
        } else if (event.getName().equals("get-data")) {
            try {
                event.reply(String.format("Hi %s, here is your data.",username)).queue();
            } catch (Exception e) {e.printStackTrace();}
        } 

        //System.out.println(event.getCommandString());}
        
    }
    
}