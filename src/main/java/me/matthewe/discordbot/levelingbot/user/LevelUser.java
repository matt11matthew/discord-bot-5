package me.matthewe.discordbot.levelingbot.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.matthewe.discordbot.levelingbot.shop.ShopItem;

import java.util.List;

@Getter
@Setter
public class LevelUser {
    private long userId;
    private String displayName = "unknown";
    private long level;
    private long exp;
    private long coins;

    private long lastMessage;
    private ShopData shopData;

    public LevelUser( long userId, String displayName, long level, long exp, long coins, long lastMessage, ShopData shopData) {
        this.userId = userId;
        this.displayName = displayName;
        this.level = level;
        this.exp = exp;
        this.coins = coins;
        this.lastMessage = lastMessage;
        this.shopData = shopData;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class ShopData {
        private List<ShopItem> purchases;


        public <V extends ShopItem> void addPurchase(V shopItem) {
            purchases.add(shopItem);
        }

        public <V extends ShopItem> void removePurchase(V shopItem) {
            purchases.remove(shopItem);
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