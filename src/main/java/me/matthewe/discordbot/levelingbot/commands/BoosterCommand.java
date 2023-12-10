package me.matthewe.discordbot.levelingbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.booster.Booster;
import me.matthewe.discordbot.levelingbot.booster.BoosterStorage;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:14 PM for the project LevelingBot
 */
@CommandInfo(name = "booster", description = "The booster command", usage = ">booster")
public class BoosterCommand extends Command {

    private LevelingBot levelingBot;

    public BoosterCommand(LevelingBot levelingBot) {
        this.levelingBot = levelingBot;
        this.name = "booster";
        this.help = "View active booster";
        this.cooldown = 5;
        this.guildOnly = true;
    }


    @Override
    protected void execute(CommandEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("Current Active Booster")
                .setColor(Color.YELLOW.darker());

        BoosterStorage boosterStorage = levelingBot.getBoosterStorage();

        if (boosterStorage != null) {
            Booster activeBooster = boosterStorage.getActiveBooster();
            if (activeBooster != null) {
                embedBuilder.setDescription("Current booster active for **" + formatTime(boosterStorage.getCurrentBoosterTime()) + "**");
            } else {
                embedBuilder.setDescription("No active boosters").setColor(Color.RED.darker());

            }
        }
        event.reply(embedBuilder.build());
    }

    private String formatTime(long currentBoosterTime) {
        long ms = currentBoosterTime - System.currentTimeMillis();

        long seconds = TimeUnit.MILLISECONDS.toSeconds(ms);

        long minutes = 0;
        long hours = 0;
        while ((seconds >= 60)) {
            seconds -= 60;
            minutes++;
        }
        while ((minutes >= 60)) {
            minutes -= 60;
            hours++;
        }
        return hours + "h " + minutes + "m " + seconds + "s";
    }
}
