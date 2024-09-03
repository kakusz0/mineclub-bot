package pl.mineclub.bot.events;

import lombok.Getter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import pl.mineclub.bot.instance.BotInstance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Getter
public class NumberEvent extends ListenerAdapter {

    private static final long CHANNEL_ID = 1260824650330669078L; // ID kanału
    private final ConcurrentHashMap<Long, Long> numberMap = new ConcurrentHashMap<>();

    public NumberEvent() {
        try {
            loadNumbersFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        BotInstance.getInstance().getExecutorService().scheduleAtFixedRate(() -> {
            try {
                saveNumbersToDatabase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 30, 60, TimeUnit.SECONDS);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getChannel().getIdLong() != CHANNEL_ID) return;

        String message = event.getMessage().getContentRaw().trim();
        long number;

        try {
            number = Long.parseLong(message);
        } catch (NumberFormatException e) {
            // If the message is not a number, ignore it
            return;
        }

        long lastNumber = getLastNumber();

        // Check if the current number is valid (greater than the last number)
        if (number == lastNumber + 1) {
            // Save the new number in the HashMap
            numberMap.put(number, number);
         } else {
            event.getMessage().delete().queue();
        }
    }

    public void loadNumbersFromDatabase() throws SQLException {
        String query = "SELECT number FROM mineclub_bot";
        try (PreparedStatement statement = BotInstance.getInstance().getMysqlManager().getHikariDataSource().getConnection().prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long number = resultSet.getLong("number");
                numberMap.put(number, number);
            }
        }
    }

    public void saveNumbersToDatabase() throws SQLException {
        String query = "INSERT INTO mineclub_bot (number) VALUES (?) ON DUPLICATE KEY UPDATE number = VALUES(number)";
        try (PreparedStatement statement = BotInstance.getInstance().getMysqlManager().getHikariDataSource().getConnection().prepareStatement(query)) {
            for (Long number : numberMap.values()) {
                statement.setLong(1, number);
                statement.executeUpdate();
            }
        }
        System.out.printf("saved numbers %d%n", numberMap.size());
    }

    private long getLastNumber() {
        if (numberMap.isEmpty()) return 0;
        return numberMap.keySet().stream().max(Long::compareTo).orElse(0L);
    }
}