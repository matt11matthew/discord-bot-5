package me.matthewe.discordbot.levelingbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:14 PM for the project LevelingBot
 */
@CommandInfo(name = "xp2coins", description = "xp2coins", usage = ">xp2coins")
public class XpToCoinsCommand extends Command {

    private LevelingBot levelingBot;

    public XpToCoinsCommand(LevelingBot levelingBot) {
        this.levelingBot = levelingBot;
        this.name = "xp2coins";
               this.help = "Converts experience to coins";
        this.cooldown = 25;
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        LevelUser user = levelingBot.getUser(event.getAuthor().getIdLong());

        try {
            long amountToGive = (long) (((double) user.getExp() / (double) levelingBot.getConfig().getCoinCost()));
            if (amountToGive < 1) {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("Error")
                        .setColor(Color.RED.darker())
                        .setDescription(event.getAuthor().getAsMention() + " does not have enough xp.");
                event.reply(embedBuilder.build());
                return;
            }

            long xpToTake = amountToGive * levelingBot.getConfig().getCoinCost();
            if (user.getExp() < xpToTake) {
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setTitle("Error")
                        .setColor(Color.RED.darker())
                        .setDescription(event.getAuthor().getAsMention() + " does not have enough xp.");
                event.reply(embedBuilder.build());
                return;
            }
            long coins = amountToGive * levelingBot.getConfig().getCoinCostAmount();
            user.setExp(user.getExp() - xpToTake);
            user.setCoins(user.getCoins() + coins);
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Converted Xp to Coins")
                    .setDescription(event.getAuthor().getAsMention() + " converted **" + new DecimalFormat("#,###").format(xpToTake) + " XP ** to **" +new DecimalFormat("#,###").format(coins)+" coins**.")
                    .setColor(Color.YELLOW.darker());
            event.reply(embedBuilder.build());
        } catch (Exception ignored) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setTitle("Error")
                    .setColor(Color.RED.darker())
                    .setDescription(event.getAuthor().getAsMention() + " does not have enough xp.");
            event.reply(embedBuilder.build());
            return;

        }
        /*

 423         25
  -----   = ----
    xc        5
         */
        //(user.getExp() * levelingBot.getConfig().getCoinGiveAmount())) / levelingBot.getConfig().getCoinGiveAmount()
        long amountToGive = (user.getExp() / levelingBot.getConfig().getCoinCost()) * levelingBot.getConfig().getCoinGiveAmount();
        user.setExp(user.getExp() - amountToGive);
        user.setCoins(user.getCoins() - +amountToGive);

    }
}
