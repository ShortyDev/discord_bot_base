package at.shortydev.bot;

import at.shortydev.bot.commands.Commands;
import at.shortydev.bot.database.AsyncMySQL;
import at.shortydev.bot.database.impl.ServerDatabaseController;
import at.shortydev.bot.listeners.GuildJoinEvent;
import at.shortydev.bot.listeners.GuildLeaveEvent;
import at.shortydev.bot.listeners.TextMessageEvent;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import java.sql.SQLException;

@Getter
public class DiscordBot {

    @Getter
    private static DiscordBot discordBot;

    private ShardManager shardManager;
    private AsyncMySQL mysql;

    private ServerDatabaseController serverDatabaseController;
    private Commands commands;

    private final String public_footer = "Example | Test Bot";

    @SneakyThrows
    public void enable(String[] args) {
        discordBot = this;

        if (args.length < 2) {
            System.out.println("################ TOO FEW ARGUMENTS ################");
            System.out.println("########## (bot-token mysql-credentials) ##########");
            System.out.println("################ TOO FEW ARGUMENTS ################");
            System.exit(-1);
            return;
        }

        // host:port:user:pw:database
        String connectionString = args[1];
        String[] connectionArguments = connectionString.split(":");
        mysql = new AsyncMySQL(connectionArguments[0], Integer.parseInt(connectionArguments[1]), connectionArguments[2], connectionArguments[3], connectionArguments[4]);
        createTables();

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(args[0]);
        builder.addEventListeners(new TextMessageEvent());
        builder.addEventListeners(new GuildJoinEvent());
        builder.addEventListeners(new GuildLeaveEvent());
        shardManager = builder.build();
        shardManager.setActivity(Activity.of(Activity.ActivityType.DEFAULT, "Bot Status"));
        shardManager.setStatus(OnlineStatus.DO_NOT_DISTURB);

        this.serverDatabaseController = new ServerDatabaseController();
        this.commands = new Commands(true);
    }

    private void createTables() {
        try {
            if (!mysql.getMySQL().getConnection().isClosed()) {
                mysql.update(mysql.prepare("CREATE TABLE IF NOT EXISTS b_servers(ID TEXT, commandsUsed INT, botJoined LONG, PREFIX TEXT)"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
