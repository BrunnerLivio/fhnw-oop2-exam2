import exceptions.InvalidAddressException;
import models.Address;
import models.Player;

import java.util.*;
import java.util.stream.Collectors;

import static utils.GameUtils.listPlayerNames;

/**
 * Lösungen mit Compile-Fehlern führen zu einem deutlichen Punkteabzug.
 *
 * In beiden Aufgaben darf jeweils nur in dieser Klasse der Code geändert
 * werden.
 * In jeder anderen Klasse dürfen **keine** Änderungern gemacht werden.
 *
 * In Test-Klassen dürfen ausschliesslich Tests auskommentiert werden, der
 * Inhalt von diesen darf nicht geändert werden.
 */
public class Game {
  private final List<Player> players;

  public Game(List<Player> players) {
    this.players = players;
  }

  /**
   * Aufgabe 1 - Lambdas & Streams (18 Punkte)
   *
   * Für **jede** Teilaufgabe muss jeweils **nur** der **Inhalt der Methode** mit
   * dem **angegebenen Namen** in der Klasse `Game` implementiert werden.
   *
   * Jede Methode darf **ausschliesslich** aus **genau einem** zusammenhängenden
   * Stream ausgehend vom Array `players`
   * (mit **beliebig vielen** Intermediate-Operationen und **genau einer**
   * Terminal-Operation)
   * bestehen, von dem das Resultat zurückgegeben wird und **sonst nichts
   * anderes**.
   *
   * Teilaufgaben, welche in den Lösungen **nicht** das Stream-API verwenden
   * werden nicht bewertet, führen also zu 0 Punkten.
   *
   * Die Verwendung der `forEach`-Methode ist ebenfalls **nicht** erlaubt.
   */

  /**
   * Gibt eine Liste aller Spieler (`Player`) zurück, die **älter** sind als das
   * Alter welches als Parameter mitgegeben wird.
   */
  public List<Player> playersOverAge(int age) {
    return players.stream()
        .filter(p -> p.getAge() > age)
        .collect(Collectors.toList());
  }

  /**
   * Gibt zurück, ob **mindestens einer** der Spieler in der Stadt wohnen, die als
   * Parameter mitgegeben wird.
   */
  public boolean hasPlayersFromCity(String city) {
    return players.stream()
        .filter(p -> p.getAddress() != null && p.getAddress().getCity() != null)
        .anyMatch(p -> p.getAddress().getCity().equals(city));
  }

  /**
   * Gibt **den Spieler** zurück, welcher an der Adresse wohnt, die als Parameter
   * mitgegeben wird.
   * Sie müssen als Rückgabetyp `Optional<Player>` verwenden, da es sein könnte,
   * dass es keinen Spieler an dieser Adresse gibt.
   * Tipp: Schauen Sie welche Terminal-Operationen im Stream `Optional<Player>`
   * zurückgeben.
   */
  public Optional<Player> playerWithAddress(Address address) {
    return players.stream()
        .filter(p -> {
          if (address == null || p.getAddress() == null) {
            return false;
          }

          return p.getAddress().getCity().equals(address.getCity())
              && p.getAddress().getStreet().equals(address.getStreet());
        })
        .findFirst();
  }

  /**
   * Gibt die höchste erzielte Punktzahl (`score`) unter allen Spielern in diesem
   * Spiel zurück.
   */
  public int highestScore() {
    return players.stream()
        .mapToInt(Player::getScore)
        .max()
        .orElse(0);
  }

  /**
   * Gibt eine Liste der **Namen** aller Spieler zurück, welche **keine Namen
   * doppelt** enthält und nach Namen **alphabetisch aufsteigend** sortiert ist.
   */
  public List<String> uniquePlayerNamesSorted() {
    return players.stream()
        .map(p -> p.getName())
        .distinct()
        .sorted()
        .toList();
  }

  /**
   * Gibt eine Liste aller Spieler zurück, die nach der Punktzahl (`score`)
   * **absteigend** sortiert ist.
   */
  public List<Player> playersSortedByScoreDescending() {
    return players.stream()
        .sorted(Comparator.comparing(Player::getScore, Comparator.reverseOrder()))
        .collect(Collectors.toList());

  }

  /**
   * Gibt eine Map zurück, welche basierend auf dem Array `players` als **Key**
   * die erreichte Punktzahl (`score`) und
   * als **Value** das Spieler-Objekt enthält, welche die Punktzahl im Key
   * erreicht hat.
   * Sie können hier davon ausgehen, dass im `players`-Array **jeder** Spieler
   * eine **unterschiedliche** Punktzahl hat,
   * es gibt also **nicht** mehrere Spieler mit der gleichen Punktzahl.
   */
  public Map<Integer, Player> playerByScore() {
    return players.stream()
        .collect(Collectors.toMap(t -> t.getScore(), t -> t));
  }

  /**
   * Aufgabe 2 - Exceptions (8 Punkte)
   *
   * Für **jede** Teilaufgabe muss jeweils **genau eine** Methode mit dem
   * **angegebenen Namen** erstellt werden.
   *
   * Es ist Ihnen überlassen, ob Sie in dieser Aufgabe (falls nötig) Lambdas und
   * oder Streams verwenden oder nicht.
   */

  /**
   * Teilaufgabe a)
   *
   * Die vorgegebene statische Methode `listPlayerNames` in der Klasse `GameUtils`
   * gibt eine Liste der Namen aller Spieler zurück,
   * hat aber den Nachteil, dass in gewissen Fällen eine `NullPointerException`
   * geworfen werden könnte.
   *
   * Schreiben Sie die neue Methode **`listPlayerNamesNullSafe`** in der Klasse
   * `Game`, welche die Methode `listPlayerNames` von `GameUtils` aufruft,
   * aber für den Fall dass diese eine `NullPointerException` wirft, stattdessen
   * eine **leere Liste** zurückgibt.
   *
   * Lösungen, welche die Klasse `GameUtils` **verändern** oder die Methode
   * `listPlayerNames` **nicht** aufrufen werden **nicht bewertet** und führen zu
   * 0 Punkten.
   */
  // TODO add method `listPlayerNamesNullSafe`

  /**
   * Teilaufgabe b)
   *
   * Schreiben Sie die Methode **`validatePlayers`** mit folgender Funktionalität:
   * - falls die Referenz `players` auf `null` zeigt, wird eine
   * **`IllegalStateException`** geworfen.
   * - falls **mindestens einer** der Spieler in der Liste `players` eine
   * **ungültige** Adresse hat, wird eine **`InvalidAddressException`** geworfen.
   *
   * Die `InvalidAddressException` finden Sie im Package `exceptions` und sollen
   * diese verwenden.
   *
   * Eine Adresse gilt in folgenden Fällen als ungültig:
   * - Der Spieler hat keine Adresse
   * - Der Spieler hat eine Adresse, aber die Strasse oder die Stadt (oder beide)
   * sind leer
   */
  // TODO add method `validatePlayers`

}
