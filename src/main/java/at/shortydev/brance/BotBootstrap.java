package at.shortydev.brance;

import at.shortydev.brance.shutdown.ShutdownHook;

public class BotBootstrap {

    public static void main(String[] args) {
        new DiscordBot().enable(args);
        Runtime.getRuntime().addShutdownHook(new ShutdownHook());
    }
}
