package utils;

import models.Player;

import java.util.ArrayList;
import java.util.List;

public class GameUtils {

    /**
     * Nimmt eine Liste von Spielern und gibt eine Liste mit den Namen dieser Spieler zurück.
     * Kann NullPointerExceptions werfen!
     */
    public static List<String> listPlayerNames(List<Player> players) {
        List<String> playerNames = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            playerNames.add(players.get(i).getName());
        }
        return playerNames;
    }

}
