package me.matthewe.discordbot.levelingbot.rank;

import me.matthewe.discordbot.levelingbot.DefaultRank;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.utilities.StorageCache;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 5:48 PM for the project LevelingBot
 */
public class RankStorage extends StorageCache<String, Rank> {
    private LevelingBot levelingBot;

    public RankStorage(LevelingBot levelingBot) {
        super("ranks", "ranks.json");
        this.levelingBot = levelingBot;
        if (!new File("ranks.json").exists()) {

            for (DefaultRank defaultRank : DefaultRank.values()) {
                create(defaultRank.getName(), new Rank(defaultRank.getName(), defaultRank.getCost(), defaultRank.getDiscordRole(), defaultRank.getColor(), defaultRank.ordinal()));
            }
            save();
            load();
        }
    }

    public Rank getRank(String name) {
        return get(name);
    }

    public Rank getNextRank(Rank rank) {
        for (Rank value : map.values()) {
            if (rank.getRanking() + 1 == value.getRanking()) {
                return value;
            }
        }
        return null;
    }

    public void checkDiscordRoles() {
        Guild guild = levelingBot.getGuild(levelingBot.getConfig().getGuildId());
        map.values().forEach(rank -> {
            List<Role> rolesByName = guild.getRolesByName(rank.getRoleName(), true);
            if ((rolesByName == null) || rolesByName.isEmpty()) {
                guild.getController().createRole()
                        .setColor(rank.getColor())
                        .setMentionable(false)
                        .setName(rank.getRoleName())
                        .queue(role -> {

                        });
            }
        });
    }

    @Override
    public String parseFromString(String s) {
        return new String(s);
    }

    @Override
    public Class<Rank> getEntityClass() {
        return Rank.class;
    }

    public Rank getFirstRank() {
        List<Rank> ranks = new ArrayList<>(map.values());
        for (Rank rank : ranks) {
            if (rank.getRanking() == 0) {
                return rank;
            }
        }
        ranks.sort(Comparator.comparingInt(Rank::getRanking));
        return ranks.get(0);
    }
}
