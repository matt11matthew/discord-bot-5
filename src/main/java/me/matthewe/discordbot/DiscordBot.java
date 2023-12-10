package me.matthewe.discordbot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.matthewe.discordbot.api.message.MessageListener;
import me.matthewe.discordbot.config.BotConfig;
import me.matthewe.discordbot.utilities.Callback;
import me.matthewe.discordbot.utilities.JsonUtils;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Matthew Eisenberg on 6/18/2018 at 1:02 PM for the project discordbot
 */
public abstract class DiscordBot<T extends BotConfig> extends ListenerAdapter implements Bot<T> {
    protected JDA jda;
    protected String botName;
    protected ShardManager shardManager;
    protected T config;
    private Class<T> configClass;
    protected String token;
    protected List<ListenerAdapter> listenerAdapterList;
    protected List<Command> commands;

    public DiscordBot(String botName, Class<T> configClass, String token) {
        this.botName = botName;
        this.configClass = configClass;
        this.token = token;
        this.listenerAdapterList = new ArrayList<>();
        this.commands = new ArrayList<>();
    }

    @Override
    public String getBotName() {
        return botName;
    }

    @Override
    public Guild getGuild(long guildId) {
        return jda.getGuildById(guildId);
    }

    @Override
    public void createTextChannel(long guildId, String name, Callback<TextChannel> textChannelCallback) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        guild.getController().createTextChannel(name).queue(channel -> textChannelCallback.call((TextChannel) channel));
    }

    @Override
    public void createCategory(long guildId, String name, Callback<Category> callback) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        guild.getController().createCategory(name).queue(channel -> callback.call((Category) channel));
    }

    @Override
    public void createTextChannel(long guildId, Category category, String name, Callback<TextChannel> textChannelCallback) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        category.createTextChannel(name).queue(channel -> textChannelCallback.call((TextChannel) channel));
    }

    @Override
    public void moveTextChannelToCategory(long guildId, TextChannel textChannel, Category category, Callback<TextChannel> textChannelCallback) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        guild.getController().modifyTextChannelPositions().selectPosition(textChannel).moveTo(category.getPosition()).queue(aVoid -> {
            textChannelCallback.call(textChannel);
        });

    }

    public abstract void onStart();

    public abstract void onShardStart();

    @Override
    public void start() {
        System.out.println("The bot is now starting...");
        this.loadConfig();
        onStart();
        EventWaiter eventWaiter = new EventWaiter();
        registerCommands(eventWaiter);
        CommandClientBuilder commandClientBuilder = new CommandClientBuilder()
                .setOwnerId(getBotOwner())
                .setPrefix(getCommandPrefix())
                .useHelpBuilder(true)
                .setDiscordBotsKey(this.token)
                .addCommands(commands.toArray(new Command[0]));

        try {
            DefaultShardManagerBuilder defaultShardManagerBuilder = new DefaultShardManagerBuilder()
                    .setToken(this.token)
                    .addEventListeners(this)
                    .addEventListeners(eventWaiter)
                    .addEventListeners(commandClientBuilder.build())
                    .addEventListeners(new MessageListener());

            for (ListenerAdapter listenerAdapter : listenerAdapterList) {
                defaultShardManagerBuilder = defaultShardManagerBuilder.addEventListeners(listenerAdapter);
            }
            defaultShardManagerBuilder = setupShardOptions(defaultShardManagerBuilder);
            this.shardManager = defaultShardManagerBuilder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        this.shardManager.getApplicationInfo().queue(applicationInfo -> {
            this.jda = applicationInfo.getJDA();
            onShardStart();
        });

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            this.onConsoleMessage(scanner.nextLine());
        }
    }

    protected abstract DefaultShardManagerBuilder setupShardOptions(DefaultShardManagerBuilder defaultShardManagerBuilder);

    public abstract String getBotOwner();

    public abstract void registerCommands(EventWaiter eventWaiter);

    public abstract void save();

    @Override
    public void shutdown() {
        save();
        this.shardManager.shutdown();
        System.exit(1);
    }

    public void sendPrivateEmbedMessage(long userId, MessageEmbed embedBuilder) {
        User userById = getJDA().getUserById(userId);
        if (userById != null) {
            userById.openPrivateChannel().queue(privateChannel -> {
                privateChannel.sendMessage(embedBuilder).queue();
            });
        }
    }

    public void sendEmbedMessage(TextChannel textChannel, MessageEmbed embedBuilder) {
        textChannel.sendMessage(embedBuilder).queue();
    }

    public abstract void onConsole(String message);

    @Override
    public void onConsoleMessage(String message) {
        onConsole(message);
        switch (message.toLowerCase()) {
            case "stop":
            case "end":
            case "shutdown":
                this.shutdown();
                break;
        }
    }

    public abstract String getCommandPrefix();

    @Override
    public JDA getJDA() {
        return jda;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void loadConfig() {
        this.config = JsonUtils.loadJsonObjectFromFile(new File("config.json"), configClass, getDefaultConfig());
        System.out.println("Loaded config");
    }

    public abstract T getDefaultConfig();

    @Override
    public ShardManager getShardManager() {
        return shardManager;
    }

    @Override
    public T getConfig() {
        return config;
    }
}
