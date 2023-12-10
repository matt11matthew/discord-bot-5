package me.matthewe.discordbot.levelingbot.shop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.entities.Member;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:12 PM for the project LevelingBot
 */
@AllArgsConstructor
@Getter
public enum ShopItems {
    PRIVATE_ROOM("Private Room", 15000L, "(Private Voice Channel And Text Channel)", PrivateRoomShopItem.class),
    RANK_LOUNGE_ACCESS("Rank Lounge Access", 15000L, "Get access to the rank lounge", RankLoungeAccessShopItem.class),
    RANK_UPGRADE("%upgrade%", 0, "Upgrade Rank", RankUpgradeShopItem.class),
    TEMP_UNMUTE("Temp Unmute", 1250, "Temp unmute", TempUnmuteShopItem.class),
    PERM_UNMUTE("Perm Unmute", 1250, "Perm unmute", PermUnmuteShopItem.class),
    UNMUTE_AND_UNBAN_FRIEND("Unmute and unban friend", 5000, "Unmute and unban friend", PermUnmuteAndUnbanFriendShopItem.class),
    CHAT_BOOSTER("Chat Booster", 12250, "Chat booster", ChatBoosterShopItem.class);


    private String name;
    private long cost;
    private String description;
    private Class<? extends ShopItem> clazz;

    public static ShopItems getShopItem(String toUpperCase) {
        for (ShopItems value : values()) {
            if (value.name.equalsIgnoreCase(toUpperCase) || value.toString().replaceAll("_", "").equalsIgnoreCase(toUpperCase)) {
                return value;
            }
        }
        return null;
    }

    public void purchase(LevelingBot bot, LevelUser levelUser, Member member) {
        try {
            ShopItem shopItem = this.clazz.getConstructor(this.getClass()).newInstance(this);
            levelUser.setCoins(levelUser.getCoins() - cost);
            shopItem.purchase(bot, levelUser, member);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
/*
- 15,000 for Private Room (Private Voice Channel And Text Channel)

- 1000 For SwordFish Rank Upgrade,2000 For Upgrade To Orca,5000 For Upgrade To Megalodon,10,000 For Upgrade To Nuclear Submarine,25,000 For Upgrade To Mutant Whale.

- 1250 For Temp Mute Unmute,2000 For Perm Mute Unmute.

- 5000 To Unban/Unmute(All Mutes) Friend.

- 12,250 For Chat Booster (180 Percent More Coins For 6 Hours)

- 15,000 For Rank Lounge Access (Bot Doesn't Grant Access (Staff On Discord Server Does) )
Also budget updated to $40
@MatthewE
 */