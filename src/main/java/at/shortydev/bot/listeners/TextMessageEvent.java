package at.shortydev.bot.listeners;

import at.shortydev.bot.DiscordBot;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TextMessageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(event.getGuild().getJDA().getSelfUser().getId())) return;
        if (!event.getChannelType().equals(ChannelType.TEXT)) return;
        DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(event.getGuild().getIdLong(), false).thenAccept(serverSettings -> {
            if (serverSettings == null) {
                return;
            }
            if (event.getMessage().getContentRaw().startsWith(serverSettings.getPrefix()))
                DiscordBot.getDiscordBot().getCommands().executeCommand(event.getTextChannel(), event.getMember(), event.getMessage().getContentRaw().substring(serverSettings.getPrefix().length()));
        });
    }
}
