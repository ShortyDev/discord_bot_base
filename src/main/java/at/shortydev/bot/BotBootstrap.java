package at.shortydev.bot;

import at.shortydev.bot.shutdown.ShutdownHook;

public class BotBootstrap {

    public static void main(String[] args) {
        new DiscordBot().enable(args);
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }
}
