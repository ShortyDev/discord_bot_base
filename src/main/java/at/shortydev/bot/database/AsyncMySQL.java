package at.shortydev.bot.database;

import lombok.Builder;

import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class AsyncMySQL {
    private static final ExecutorService executor = Executors.newFixedThreadPool(2);
    private MySQL sql;

    public AsyncMySQL(String host, int port, String user, String password, String database) {
        try {
            sql = MySQL.builder().host(host).port(port).user(user).password(password).database(database).build();
            sql.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(PreparedStatement statement) {
        executor.execute(() -> {
            sql.queryUpdate(statement);
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void update(String statement) {
        executor.execute(() -> sql.queryUpdate(statement));
    }

    public void query(PreparedStatement statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);

            new Thread(() -> {
                consumer.accept(result);
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void query(String statement, Consumer<ResultSet> consumer) {
        executor.execute(() -> {
            ResultSet result = sql.query(statement);
            new Thread(() -> consumer.accept(result)).start();
        });
    }

    public PreparedStatement prepare(String query) {
        try {
            return sql.getConnection().prepareStatement(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MySQL getMySQL() {
        return sql;
    }

    @Builder
    public static class MySQL {

        private final String host;
        private final String user;
        private final String password;
        private final String database;
        private final int port;

        private Connection conn;

        public void queryUpdate(String query) {
            checkConnection();
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                queryUpdate(statement);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void queryUpdate(PreparedStatement statement) {
            checkConnection();
            try {
                statement.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public ResultSet query(String query) {
            checkConnection();
            try {
                return query(conn.prepareStatement(query));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public ResultSet query(PreparedStatement statement) {
            checkConnection();
            try {
                return statement.executeQuery();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public Connection getConnection() {
            return this.conn;
        }

        private void checkConnection() {
            try {
                if (this.conn == null || !this.conn.isValid(10) || this.conn.isClosed()) openConnection();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void openConnection() throws Exception {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true", this.user, this.password);
        }

        public void closeConnection() {
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.conn = null;
            }
        }
    }
}
