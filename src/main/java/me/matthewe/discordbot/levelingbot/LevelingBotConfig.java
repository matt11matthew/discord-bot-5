package me.matthewe.discordbot.levelingbot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.matthewe.discordbot.config.BotConfig;

import java.util.List;

/**
 * Created by Matthew Eisenberg on 7/27/2018 at 6:13 PM for the project LevelingBot
 */

@AllArgsConstructor
@Getter
public class LevelingBotConfig extends BotConfig {
    private Discord discord;
    private List<Long> rewardChannels;
    private long xpGiveAmount;
    private long coinGiveAmount;
    private long coinCost;
    private long coinCostAmount;
    private long guildId;
    private String staffRole;
    private long coinDelay;

    @AllArgsConstructor
    @Getter
    public static class Discord {
        private String token;


    }
}
