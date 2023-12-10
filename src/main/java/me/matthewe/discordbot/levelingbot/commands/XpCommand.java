package me.matthewe.discordbot.levelingbot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;
import me.matthewe.discordbot.levelingbot.LevelingBot;
import me.matthewe.discordbot.levelingbot.user.LevelUser;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by Matthew Eisenberg on 7/29/2018 at 2:14 PM for the project LevelingBot
 */
@CommandInfo(name = "xp", description = "View balance", usage = ">xp (user)")
public class XpCommand extends Command {

    private LevelingBot levelingBot;

    public XpCommand(LevelingBot levelingBot) {
        this.levelingBot = levelingBot;
        this.name = "xp";
        this.cooldown = 5;
        this.help = "View XP";
        this.arguments = "(optional (user))";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event) {
        LevelUser user = levelingBot.getUser(event.getAuthor().getIdLong());
        if (event.getArgs().isEmpty() || (event.getArgs().split(" ").length == 0)) {
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setAuthor(event.getAuthor().getName(), event.getAuthor().getAvatarUrl(), event.getAuthor().getAvatarUrl())
                    .setColor(Color.YELLOW.darker())
                    .setDescription("You have **"+new DecimalFormat("#,###").format(user.getExp())+"** xp.");
            event.reply(embedBuilder.build());
        } else if (event.getArgs().split(" ").length>=1){
            for (Member mentionedMember : event.getMessage().getMentionedMembers()) {
                if (mentionedMember.getUser().isBot()){
                    continue;
                }
                LevelUser levelUser = levelingBot.getUser(mentionedMember.getUser().getIdLong());
                EmbedBuilder embedBuilder = new EmbedBuilder()
                        .setAuthor(mentionedMember.getUser().getName(), mentionedMember.getUser().getAvatarUrl(), mentionedMember.getUser().getAvatarUrl())
                        .setColor(Color.YELLOW.darker())
                        .setDescription(mentionedMember.getAsMention()+" has **"+new DecimalFormat("#,###").format(levelUser.getExp())+"** xp.");
                event.reply(embedBuilder.build());
            }
        }
    }
}
