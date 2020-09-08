package at.shortydev.bot.commands.impl;

import at.shortydev.bot.DiscordBot;
import at.shortydev.bot.commands.Command;
import at.shortydev.bot.messages.EasyEmbed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HelpCommand extends Command {

    public HelpCommand(String name, String description, String... aliases) {
        super(name, description, aliases);
    }

    @Override
    public void onCommand(TextChannel textChannel, Member member, String command, String[] args) {
        if (args.length == 0) {
            HashMap<String, String> commands = new HashMap<>();
            DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(textChannel.getGuild().getIdLong(), false).thenAccept(serverSettings -> {

                for (Command cmd : DiscordBot.getDiscordBot().getCommands().getCommands()) {
                    commands.put(serverSettings.getPrefix() + cmd.getName(), cmd.getDescription());
                }
                StringBuilder commandList = new StringBuilder();
                for (Map.Entry<String, String> entry : commands.entrySet()) {
                    commandList.append("\n").append(entry.getKey()).append(" - ").append(entry.getValue());
                }
                EasyEmbed embedBuilder = EasyEmbed.builder().title("Help - Brance Bot").color(Color.GREEN).description(commandList.substring(1)).footer(EasyEmbed.Footer.builder().text(DiscordBot.getDiscordBot().getPublic_footer()).build())
                        .timestamp(new Date().toInstant()).autoDelete(20).build();
                embedBuilder.buildMessageAndSend(textChannel);
            });
        }
    }
}
