package me.matthewe.discordbot.api.message;

import me.matthewe.discordbot.utilities.DoubleCallback;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class OnServerMessage {
    private long textChannelId;
    private DoubleCallback<OnServerMessage, GuildMessageReceivedEvent> guildMessageReceivedEventCallback;
    public static List<OnServerMessage> onMessageList = new ArrayList<>();

    public static OnServerMessage listenForMessage( long textChannel, DoubleCallback<OnServerMessage, GuildMessageReceivedEvent> guildMessageReceivedEventCallback) {
        return new OnServerMessage(textChannel, guildMessageReceivedEventCallback).listen();
    }

    public OnServerMessage(long textChannelId,DoubleCallback<OnServerMessage, GuildMessageReceivedEvent> guildMessageReceivedEventCallback) {
        this.textChannelId = textChannelId;
        this.guildMessageReceivedEventCallback = guildMessageReceivedEventCallback;
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public OnServerMessage listen() {
        onMessageList.add(this);
        return this;
    }

    public OnServerMessage remove() {
        onMessageList.remove(this);
        return this;
    }


    public DoubleCallback<OnServerMessage, GuildMessageReceivedEvent> getGuildMessageReceivedEventCallback() {
        return guildMessageReceivedEventCallback;
    }
}
