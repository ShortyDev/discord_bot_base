package at.shortydev.bot.commands;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

@Getter
public abstract class Command {

    String name;
    String[] aliases;
    String description;

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
    }

    /**
     * Execute this command with the specified sender, command and arguments.
     *
     * @param channel channel the command was executed in
     * @param member  command executor
     * @param command the executed command
     * @param args    arguments used to invoke this command
     */
    public abstract void onCommand(TextChannel channel, Member member, String command, String[] args);
}
