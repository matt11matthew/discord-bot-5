package me.matthewe.discordbot.levelingbot.booster;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 6:37 PM for the project LevelingBot
 */
@AllArgsConstructor
@Getter
@Setter
public class Booster {
    private UUID uuid;
    private long userId;
    private long time;
    private double multiplier;
    private int queuePosition;
}
