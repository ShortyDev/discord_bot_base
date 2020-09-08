package at.shortydev.brance.database.impl;

import at.shortydev.brance.DiscordBot;
import at.shortydev.brance.settings.ServerSettings;
import lombok.Getter;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Getter
public class ServerDatabaseController {

    private final HashMap<Long, ServerSettings> settingsCache = new HashMap<>();

    public Future<ServerSettings> getServerSettings(long serverId, boolean reload) {
        CompletableFuture<ServerSettings> completableFuture = new CompletableFuture<>();

        if (!settingsCache.containsKey(serverId) || reload) {
            try {
                PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("SELECT * FROM b_servers WHERE ID=?");
                preparedStatement.setString(1, String.valueOf(serverId));
                DiscordBot.getDiscordBot().getMysql().query(preparedStatement, resultSet -> {
                    try {
                        if (resultSet.next()) {
                            ServerSettings serverSettings = ServerSettings.builder().id(serverId).commandsUsed(resultSet.getInt("commandsUsed"))
                                    .botJoined(resultSet.getLong("botJoined")).prefix(resultSet.getString("prefix")).build();
                            completableFuture.complete(serverSettings);
                            settingsCache.put(serverId, serverSettings);
                        } else {
                            completableFuture.complete(null);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            completableFuture.complete(settingsCache.get(serverId));
        }
        return completableFuture;
    }

    public void createServerSettings(ServerSettings serverSettings) {
        if (!settingsCache.containsKey(serverSettings.getId())) {
            try {
                PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("INSERT INTO b_servers(ID, commandsUsed, botJoined, prefix) VALUES (?, ?, ?, ?)");
                preparedStatement.setLong(1, serverSettings.getId());
                preparedStatement.setInt(2, serverSettings.getCommandsUsed());
                preparedStatement.setLong(3, serverSettings.getBotJoined());
                preparedStatement.setString(4, serverSettings.getPrefix());
                DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
                settingsCache.put(serverSettings.getId(), serverSettings);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            updateServerSettings(serverSettings, false);
        }
    }

    public void updateServerSettings(ServerSettings serverSettings, boolean onlyCache) {
        if (onlyCache) {
            settingsCache.put(serverSettings.getId(), serverSettings);
        } else {
            try {
                PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("UPDATE b_servers SET commandsUsed=?, prefix=? WHERE ID=?");
                preparedStatement.setInt(1, serverSettings.getCommandsUsed());
                preparedStatement.setString(2, serverSettings.getPrefix());
                preparedStatement.setLong(3, serverSettings.getId());
                DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
                settingsCache.put(serverSettings.getId(), serverSettings);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void removeServerSettings(@Nullable ServerSettings serverSettings) {
        if (serverSettings == null)
            return;
        settingsCache.remove(serverSettings.getId());
        try {
            PreparedStatement preparedStatement = DiscordBot.getDiscordBot().getMysql().prepare("DELETE FROM b_servers WHERE ID=?");
            preparedStatement.setString(1, String.valueOf(serverSettings.getId()));
            DiscordBot.getDiscordBot().getMysql().update(preparedStatement);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
