package at.shortydev.bot.commands;

import at.shortydev.bot.commands.impl.HelpCommand;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class Commands {

    private final ArrayList<Command> commands;

    public Commands(boolean registerCommands) {
        commands = new ArrayList<>();
        if (registerCommands)
            registerCommands();
    }

    public void registerCommands() {
        commands.add(new HelpCommand("help", "Displays all commands available", ""));
    }

    public void executeCommand(TextChannel textChannel, Member member, String message) {
        String command = message.split(" ")[0];
        message = message.replaceFirst(command, "");
        for (Command cmd : commands) {
            if (command.equalsIgnoreCase(cmd.getName()) || new ArrayList<>(Arrays.asList(cmd.getAliases())).contains(command)) {
                cmd.onCommand(textChannel, member, command, message.isEmpty() ? new String[]{} : message.substring(1).split(" "));
            }
        }
    }
}
