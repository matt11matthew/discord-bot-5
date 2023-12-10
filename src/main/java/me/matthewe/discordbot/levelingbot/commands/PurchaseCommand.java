package me.matthewe.discordbot.levelingbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.rank.Rank;
import me.matthewe.discordbot.levelingbot.shop.RankUpgradeShopItem;
import me.matthewe.discordbot.levelingbot.shop.ShopItem;
import me.matthewe.discordbot.levelingbot.shop.ShopItems;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:14 PM for the project LevelingBot
 */
@CommandInfo(name = "purchase", description = "The purchase command", usage = ">purchase (item)")
public class PurchaseCommand extends Command {

    private LevelingBot levelingBot;

    public PurchaseCommand(LevelingBot levelingBot) {
        this.levelingBot = levelingBot;
        this.name = "purchase";
        this.cooldown = 5;
        this.help = "Purchase an item from the shop";
        this.arguments = "<item>";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        String[] s = event.getArgs().split(" ");
        if (s.length >= 1) {
            String itemName = "";
            for (int i = 0; i < s.length; i++) {
                itemName += s[i] + " ";
            }
            itemName = itemName.trim();
            ShopItems shopItems = ShopItems.getShopItem(itemName.toUpperCase());
            LevelUser user = levelingBot.getUser(event.getAuthor().getIdLong());
            Rank rankPurchase = getRankPurchase(user, itemName);
            String upgrade = "";
            if (rankPurchase != null) {
                shopItems = ShopItems.RANK_UPGRADE;
                upgrade = getRankPurchaseText(user);
                if (user.getCoins() < rankPurchase.getUpgradeCost()) {
                    EmbedBuilder embedBuilder = new EmbedBuilder()
                            .setTitle("Error")
                            .setColor(Color.RED.darker())
                            .setDescription("You don't have enough gold coins.");
                    event.reply(embedBuilder.build());
                    return;
                }
                user.setCoins(user.getCoins()-rankPurchase.getUpgradeCost());
            }
            if (shopItems == null) {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("Error")
                        .setColor(Color.RED.darker())
                        .setDescription("The shop item `" + itemName + "` doesn't exist.");
                event.reply(embedBuilder.build());
                return;
            }
            if (user.getCoins() < shopItems.getCost()) {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("Error")
                        .setColor(Color.RED.darker())
                        .setDescription("You don't have enough gold coins.");
                event.reply(embedBuilder.build());
                return;
            }
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Purchased")
                    .setColor(Color.YELLOW.darker())
                    .setDescription("You have purchased **" + new String(shopItems.getName()).replaceAll("%upgrade%",upgrade) + "**");
            event.reply(embedBuilder.build());
            shopItems.purchase(levelingBot, user, event.getMember());
        }
    }

    private Rank getRankPurchase(LevelUser user, String message) {
        String currentRank = null;
        for (ShopItem purchase : user.getShopData().getPurchases()) {
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
        if (text != null) {
            if (message.equalsIgnoreCase(text)) {
                return nextRank;
            }
        }
        return null;
    }

    private String getRankPurchaseText(LevelUser user) {
        String currentRank = null;
        for (ShopItem purchase : user.getShopData().getPurchases()) {
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
        return text;
    }
}
