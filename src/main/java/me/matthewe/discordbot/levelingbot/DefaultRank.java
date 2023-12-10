package me.matthewe.discordbot.levelingbot;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 5:51 PM for the project LevelingBot
 */
@AllArgsConstructor
@Getter
public enum DefaultRank {

    SWORDFISH("SwordFish", "swordfish", 1000, 0x00d235),
    ORCA("Orca", "orca", 2000,0xffffff),
    MEGALODON("Megalodon", "Megalodon", 5000,0xc0c0c0),
    NUCLEAR_SUBMARINE("Nuclear Submarine", "NuclearSubmarine", 10000,0x00bf00),
    MUTANT_WHALE("Mutant Whale", "mutantwhale", 25000,0x808080);

    private String name;
    private String discordRole;
    private int cost;
    private int color;

}
