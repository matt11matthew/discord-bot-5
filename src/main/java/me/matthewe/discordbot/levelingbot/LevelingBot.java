package me.matthewe.discordbot.levelingbot;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.matthewe.discordbot.DiscordBot;
import me.matthewe.discordbot.levelingbot.booster.BoosterStorage;
import me.matthewe.discordbot.levelingbot.commands.*;
import me.matthewe.discordbot.levelingbot.rank.RankStorage;
import me.matthewe.discordbot.levelingbot.shop.PrivateRoomShopItem;
import me.matthewe.discordbot.levelingbot.shop.ShopItem;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import me.matthewe.discordbot.levelingbot.user.LevelUsers;
import me.matthewe.discordbot.utilities.JsonUtils;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static me.matthewe.discordbot.utilities.JsonUtils.saveJsonObjectToFile;

/**
 * Created by Matthew Eisenberg on 7/27/2018 at 6:13 PM for the project LevelingBot
 */
public class LevelingBot extends DiscordBot<LevelingBotConfig> {
    private Map<Long, LevelUser> userMap;
    private RankStorage rankStorage;
    private BoosterStorage boosterStorage;

    public LevelingBot() {
        super("LevelingBot", LevelingBotConfig.class, "");

        this.userMap = new ConcurrentHashMap<>();
        this.loadUsers();
        this.rankStorage = new RankStorage(this);
        this.rankStorage.load();

        this.boosterStorage = new BoosterStorage();
        this.boosterStorage.load();
        this.boosterStorage.loadMap();

        listenerAdapterList.add(this.rankStorage);
    }

    public RankStorage getRankStorage() {
        return rankStorage;
    }

    @Override
    public void onStart() {
        this.token = config.getDiscord().getToken();

    }

    public BoosterStorage getBoosterStorage() {
        return boosterStorage;
    }

    public void loadUsers() {
        this.userMap = new ConcurrentHashMap<>();

        LevelUsers groupifyUsers = JsonUtils.loadJsonObjectFromFile(new File("users.json"), LevelUsers.class, new LevelUsers(new ConcurrentHashMap<>()));
        for (LevelUser groupifyUser : groupifyUsers) {
            userMap.put(groupifyUser.getUserId(), groupifyUser);
        }
        System.out.println("Loaded " + userMap.keySet().size() + " users");
    }

    private void loadUser(Member member, Runnable onReady) {
        if (member.getUser().isBot()) {
            return;
        }
        if (!userMap.containsKey(member.getUser().getIdLong())) {
            LevelUser groupifyUser = new LevelUser(member.getUser().getIdLong(), member.getUser().getName() + "#" + member.getUser().getDiscriminator(), 1, 0, 0, 0L, new LevelUser.ShopData(new ArrayList<>()));
            this.userMap.put(groupifyUser.getUserId(), groupifyUser);
            System.out.println("Created user " + groupifyUser.getDisplayName());
            checkUser(groupifyUser);
            onReady.run();
        } else {
            LevelUser user = this.getUser(member.getUser().getIdLong());
            user.setDisplayName(member.getUser().getName() + "#" + member.getUser().getDiscriminator());
            System.out.println("Loaded user " + user.getDisplayName());

            checkUser(user);
            onReady.run();
        }
    }

    private void checkUser(LevelUser user) {
        for (ShopItem purchase : user.getShopData().getPurchases()) {
            if (purchase instanceof PrivateRoomShopItem) {
                Category category = getGuild(config.getGuildId()).getCategoryById(((PrivateRoomShopItem) purchase).getCategory());
                List<Long> toAdd = new ArrayList<>();
                if (category == null || category.getPermissionOverrides() == null) {
                    continue;
                }
                for (PermissionOverride permissionOverride : category.getPermissionOverrides()) {
                    if (permissionOverride.isMemberOverride()) {
                        if (permissionOverride.getAllowed().contains(Permission.MESSAGE_READ) && (purchase.getUserId() != permissionOverride.getMember().getUser().getIdLong())) {
                            toAdd.add(permissionOverride.getMember().getUser().getIdLong());
                        }
                    }
                }
                ((PrivateRoomShopItem) purchase).clearMembers();
                for (Long aLong : toAdd) {
                    ((PrivateRoomShopItem) purchase).addMember(aLong);

                }
            }
        }
    }


    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild : this.jda.getGuilds()) {
            for (Member member : guild.getMembers().stream().filter(member -> !member.getUser().isBot()).collect(Collectors.toList())) {
                loadUser(member, () -> {

                });
            }
        }
        this.rankStorage.checkDiscordRoles();
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        loadUser(event.getMember(), () -> {
        });
    }

    public LevelUser getUser(long idLong) {
        return userMap.get(idLong);
    }


    @Override
    public void onShardStart() {
        System.out.println("[" + botName + "] My shard is now online");
    }

    @Override
    protected DefaultShardManagerBuilder setupShardOptions(DefaultShardManagerBuilder defaultShardManagerBuilder) {
        return defaultShardManagerBuilder;
    }

    @Override
    public String getBotOwner() {
        return "158445315412852736";
    }

    @Override
    public void registerCommands(EventWaiter eventWaiter) {
        this.commands.add(new ShopCommand(this));
        this.commands.add(new PurchaseCommand(this));
        this.commands.add(new CoinsCommand(this));
        this.commands.add(new BoosterCommand(this));
        this.commands.add(new XpToCoinsCommand(this));
        this.commands.add(new RoomRemoveCommand(this));
        this.commands.add(new GiveXpCommand(this));
        this.commands.add(new GiveCoinsCommand(this));
        this.commands.add(new XpCommand(this));
        this.commands.add(new RoomAddCommand(this));


    }

    @Override
    public void save() {
        saveJsonObjectToFile(new File("users.json"), new LevelUsers(userMap));
        this.boosterStorage.saveQueue();
        this.boosterStorage.save();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (this.config.getRewardChannels().contains(event.getMessage().getChannel().getIdLong()) && !event.getAuthor().isBot()) {
            LevelUser user = getUser(event.getAuthor().getIdLong());
            if (System.currentTimeMillis() > user.getLastMessage()) {
                user.setLastMessage(System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(config.getCoinDelay()));
                double amount = config.getCoinGiveAmount();
                if (boosterStorage.getActiveBooster() != null) {
                    amount *= boosterStorage.getActiveBooster().getMultiplier();
                }
                user.setCoins((long) (user.getCoins() +amount));
                user.setExp((long) (user.getExp() +config.getXpGiveAmount()));
            }
        }

    }

    @Override
    public void onConsole(String input) {

    }

    @Override
    public String getCommandPrefix() {
        return ">";
    }


    @Override
    public LevelingBotConfig getDefaultConfig() {
        return new LevelingBotConfig(new LevelingBotConfig.Discord(""), new ArrayList<>(), 25L, 25L,25L,5,0L, "STAFF", 12L);
    }
}
