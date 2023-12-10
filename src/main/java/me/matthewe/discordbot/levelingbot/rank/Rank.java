package me.matthewe.discordbot.levelingbot.rank;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 5:47 PM for the project LevelingBot
 */
@AllArgsConstructor
@Getter
public class Rank {
    private final String name;
    private final int upgradeCost;
    private final String roleName;
    private final int color;
    private final int ranking;


}
