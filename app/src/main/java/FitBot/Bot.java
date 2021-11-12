
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
        //jda.getGuildById("848849465850462281").upsertCommand("track", "track a workout").addOption(OptionType.STRING, "evidence", "Provide evidence of your workout here").queue(); // WAIT TILL DISCORD API IS UPDATED TO SUPPORT ATTACHMENTS IN SLASH COMMAND OPTIONS
        jda.getGuildById("848849465850462281").upsertCommand("break", "take a break if you need time off. e.g. feeling ill or overloaded with work")
            .addOption(OptionType.STRING, "reason", "What is your reason for taking time off?")
            .addOption(OptionType.INTEGER, "days", "How many days do you plan to take off? (you may end your time off early)")
            .queue(); 
        
    }
    
    @Override
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
    } 

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        String username = event.getUser().getName();
        if (!event.getName().equals("break")) return; // make sure we handle the right command
        long time = System.currentTimeMillis();
        //System.out.println(event.getOption("days").);
        try {
            event.reply(String.format("Hi %s, your time off has now started. You have %d days remaining. If you wish to end your break early use the /endbreak command.",username,event.getOption("days").getAsLong()))
            .queue();
        } catch (Exception e) {e.printStackTrace();}
        //System.out.println(event.getCommandString());
        System.out.println(Arrays.toString(event.getOptions().toArray()));
    }
    
}