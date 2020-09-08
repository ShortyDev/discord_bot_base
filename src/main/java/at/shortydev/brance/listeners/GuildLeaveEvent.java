package at.shortydev.brance.listeners;

import at.shortydev.brance.DiscordBot;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildLeaveEvent extends ListenerAdapter {

    @Override
    public void onGuildLeave(@NotNull net.dv8tion.jda.api.events.guild.GuildLeaveEvent event) {
        DiscordBot.getDiscordBot().getServerDatabaseController().removeServerSettings(DiscordBot.getDiscordBot().getServerDatabaseController().getSettingsCache().getOrDefault(event.getGuild().getIdLong(), null));
    }
}
