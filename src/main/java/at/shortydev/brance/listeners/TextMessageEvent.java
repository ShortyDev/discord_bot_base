package at.shortydev.brance.listeners;

import at.shortydev.brance.DiscordBot;
import at.shortydev.brance.settings.ServerSettings;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TextMessageEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(event.getGuild().getJDA().getSelfUser().getId())) return;
        if (!event.getChannelType().equals(ChannelType.TEXT)) return;
        ServerSettings serverSettings = DiscordBot.getDiscordBot().getServerDatabaseController().getSettingsCache().get(event.getGuild().getIdLong());
        if (serverSettings == null) {
            try {
                serverSettings = DiscordBot.getDiscordBot().getServerDatabaseController().getServerSettings(event.getGuild().getIdLong(), true).get(3, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        } 
        if (serverSettings == null) {
            return;
        }
        if (event.getMessage().getContentRaw().startsWith(serverSettings.getPrefix()))
            DiscordBot.getDiscordBot().getCommands().executeCommand(event.getTextChannel(), event.getMember(), event.getMessage().getContentRaw().substring(serverSettings.getPrefix().length()));
    }
}
