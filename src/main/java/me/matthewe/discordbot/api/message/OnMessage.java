package me.matthewe.discordbot.api.message;

import me.matthewe.discordbot.utilities.DoubleCallback;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class OnMessage {
    private long textChannelId;
    private long userId;
    private DoubleCallback<OnMessage, GuildMessageReceivedEvent> guildMessageReceivedEventCallback;
    public static List<OnMessage> onMessageList = new ArrayList<>();

    public static OnMessage  listenForMessage(long user, long textChannel, DoubleCallback<OnMessage, GuildMessageReceivedEvent> guildMessageReceivedEventCallback) {
        return new OnMessage(textChannel, user,  guildMessageReceivedEventCallback).listen();
    }

    public OnMessage(long textChannelId, long userId, DoubleCallback<OnMessage, GuildMessageReceivedEvent> guildMessageReceivedEventCallback) {
        this.textChannelId = textChannelId;
        this.userId = userId;
        this.guildMessageReceivedEventCallback = guildMessageReceivedEventCallback;
    }

    public long getTextChannelId() {
        return textChannelId;
    }

    public OnMessage listen() {
        onMessageList.add(this);
        return this;
    }

    public OnMessage remove() {
        onMessageList.remove(this);
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public DoubleCallback<OnMessage, GuildMessageReceivedEvent> getGuildMessageReceivedEventCallback() {
        return guildMessageReceivedEventCallback;
    }
}
