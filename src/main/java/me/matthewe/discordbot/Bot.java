package me.matthewe.discordbot;

import me.matthewe.discordbot.config.BotConfig;
import me.matthewe.discordbot.utilities.Callback;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Category;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * Created by Matthew Eisenberg on 6/18/2018 at 1:02 PM for the project discordbot
 */
public interface Bot<T extends BotConfig> {
    void start();

    void shutdown();

    String getBotName();

    void createTextChannel(long guildId, String name, Callback<TextChannel> textChannelCallback);

    void createTextChannel(long guildId, Category category, String name, Callback<TextChannel> textChannelCallback);

    void moveTextChannelToCategory(long guildId, TextChannel textChannel, Category category, Callback<TextChannel> textChannelCallback);

    void createCategory(long guildId, String name, Callback<Category> callback);

    Guild getGuild(long guildId);

    void onConsoleMessage(String message);

    String getToken();

    void loadConfig();

    JDA getJDA();

    ShardManager getShardManager();

    T getConfig();
}
