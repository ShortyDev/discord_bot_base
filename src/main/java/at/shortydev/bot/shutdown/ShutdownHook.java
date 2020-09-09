package at.shortydev.bot.shutdown;

import at.shortydev.bot.DiscordBot;

public class ShutdownHook extends Thread {

    @Override
    public void run() {
        System.out.println("Saving server information...");
        DiscordBot.getDiscordBot().getServerDatabaseController().getSettingsCache().forEach((id, serverSettings) -> DiscordBot.getDiscordBot().getServerDatabaseController().updateServerSettings(serverSettings, false));
        System.out.println("Shutting down JDA shard manager...");
        DiscordBot.getDiscordBot().getShardManager().shutdown();
        System.out.println("Waiting 2 seconds...");
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Closing database connection...");
        DiscordBot.getDiscordBot().getMysql().getMySQL().closeConnection();
        System.out.println("Shutting down...");
    }
}
