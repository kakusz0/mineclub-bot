package pl.mineclub.bot.managers;

import lombok.Getter;
import lombok.Setter;
import pl.mineclub.bot.instance.BotInstance;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CountManager {
    private final List<Integer> numbers;

    public CountManager() {
        this.numbers = new ArrayList<>();

        this.loadUsers();
    }

    private void loadUsers() {
        try {
            ResultSet resultSet = BotInstance.getInstance().getMysqlManager().executeQuery("SELECT * FROM `mineclub_bot`");
            while (resultSet.next()) {

            }
            resultSet.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }



}
