package me.matthewe.discordbot.levelingbot.shop;

import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.rank.Rank;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 6:03 PM for the project LevelingBot
 */
public class RankUpgradeShopItem extends ShopItem {
    private String currentRank;


    public RankUpgradeShopItem(ShopItems item, long userId, long purchaseDate) {
        super(item, userId, purchaseDate);
    }

    public RankUpgradeShopItem(ShopItems item) {
        super(item);
    }

    public void setCurrentRank(String currentRank) {
        this.currentRank = currentRank;
    }

    @Override
    protected void onPurchase(LevelingBot levelingBot, LevelUser levelUser, Member member) {
        String currentRank = null;
        for (ShopItem purchase : levelUser.getShopData().getPurchases()) {
            if (purchase instanceof RankUpgradeShopItem) {
                currentRank = ((RankUpgradeShopItem) purchase).getCurrentRank();
            }
        }
        Rank nextRank = null;
        if (currentRank != null) {
            nextRank = levelingBot.getRankStorage().getNextRank(levelingBot.getRankStorage().getRank(currentRank));
        } else {
            nextRank = levelingBot.getRankStorage().getFirstRank();

        }
        String text = null;
        if (nextRank != null) {
            if (currentRank == null) {
                text = "Rank " + nextRank.getName();
            } else {
                text = "" + currentRank + " to " + nextRank.getName() + " Upgrade";
            }
        }
        if (nextRank != null) {
            this.currentRank = nextRank.getName();
        }
        if (this.currentRank != null && nextRank != null) {
            boolean hasNextRole = false;
            for (Role role : member.getRoles()) {
                if (role.getName().equalsIgnoreCase(nextRank.getRoleName())) {
                    hasNextRole = true;
                }
            }
            Role role = member.getGuild().getRolesByName(nextRank.getRoleName(), true).get(0);
            if (!hasNextRole) {
                member.getGuild().getController().addRolesToMember(member, role).queue();
            }
        }
        levelUser.getShopData().addPurchase(this);

    }

    @Override
    protected void removePurchase(LevelingBot bot, LevelUser levelUser, Member member) {
        levelUser.getShopData().removePurchase(this);
    }

    public String getCurrentRank() {
        return currentRank;
    }
}
