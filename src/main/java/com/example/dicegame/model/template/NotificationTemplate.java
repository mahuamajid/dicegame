package com.example.dicegame.model.template;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationTemplate {
    private NotificationTemplate() {
    }

    public static String gameStartTemplate(String gameName) {
        return String.format("The game: %s has started! Good luck to all players!", gameName);
    }

    public static String gameEndTemplate(String playerName, String gameName, int score) {
        return String.format("The game: %s has ended! Winner: %s with a total score of %d!", playerName, gameName, score);
    }

    public static String prizeTemplate(String playerName, String gameName, int score) {
        return String.format("Congratulations, %s. You have won the game: %s with a total score of %d and have been awarded the prize.", playerName, gameName, score);
    }
}
