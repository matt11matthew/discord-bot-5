package me.matthewe.discordbot.api.message;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by Matthew Eisenberg on 6/18/2018 at 1:04 PM for the project discordbot
 */
public class MessageListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        for (OnServerMessage onMessage : OnServerMessage.onMessageList) {
            if (onMessage.getTextChannelId() == event.getChannel().getIdLong()) {
                onMessage.getGuildMessageReceivedEventCallback().call(onMessage, event);
            }
        }
        for (OnMessage onMessage : OnMessage.onMessageList) {
            if (onMessage.getUserId() == event.getAuthor().getIdLong() && onMessage.getTextChannelId() == event.getChannel().getIdLong()) {
                onMessage.getGuildMessageReceivedEventCallback().call(onMessage, event);
            }
        }
    }
}
