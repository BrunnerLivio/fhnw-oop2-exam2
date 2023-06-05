import exceptions.InvalidAddressException;
import models.Address;
import models.Player;
import org.junit.jupiter.api.*;
import utils.GameUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameTest {
    private static final Address BASEL_ADDRESS = new Address("Hauptstrasse 3", "Basel");
    private static final int HIGHEST_SCORE = 100;
    private static final List<Player> PLAYERS = List.of(
            new Player("Fritz", 2),
            new Player("Müller", 42, new Address("Hauptstrasse 1", "Zürich"), 0),
            new Player("Schmidt", 38, BASEL_ADDRESS, 10),
            new Player("Fritz", 53, BASEL_ADDRESS, 5),
            new Player("Wagner", 73, new Address("Hauptstrasse 3", "Basel"), HIGHEST_SCORE),
            new Player("Bettina", 83, new Address("Hauptstrasse 7", "Lausanne"), 57),
            new Player("Hoffmann", 93, new Address("Hauptstrasse 8", "Bern"), 9),
            new Player("Franz", 1)
    );

    List<Player> PLAYERS_SIMPLE = List.of(new Player("Schmidt", 38, BASEL_ADDRESS, 10));

    private static final List<Player> PLAYERS_NULL = Arrays.asList(null, new Player("Fritz"), null);

    private Game game;
    private Game gameSimple;

    private static boolean hasTestAufgabe1HasPlayersFromCityPassed = false;

    @BeforeEach
    public void setUp() {
        // given
        game = new Game(PLAYERS);
        gameSimple = new Game(PLAYERS_SIMPLE);
    }

    /**
     * Aufgabe 1
     */
    @Nested
    class Aufgabe1 {
        @Test
        public void testAufgabe1PlayersOverAgeReturnAll() {
            // when
            List<Player> allPlayers = game.playersOverAge(-1);

            // then
            assertEquals(PLAYERS.size(), allPlayers.size());
            assertArrayEquals(PLAYERS.toArray(), allPlayers.toArray());

            // when
            List<Player> playersWithAge = game.playersOverAge(0);

            // then
            assertEquals(PLAYERS.size() - 2, playersWithAge.size());
            for (int i = 0; i < playersWithAge.size(); i++) {
                assertSame(PLAYERS.get(i + 1), playersWithAge.get(i));
            }

            // when
            List<Player> playersOver52 = game.playersOverAge(52);

            // then
            assertEquals(4, playersOver52.size());
            for (int i = 0; i < playersOver52.size(); i++) {
                assertSame(PLAYERS.get(i + 3), playersOver52.get(i));
            }
        }

        @Test
        @Order(1)
        void testAufgabe1HasPlayersFromCity() {
            // when
            boolean hasPlayersFromBasel = gameSimple.hasPlayersFromCity("Basel");

            // then
            assertTrue(hasPlayersFromBasel);

            // when
            boolean hasPlayersFromBrugg = gameSimple.hasPlayersFromCity("Brugg");

            // then
            assertFalse(hasPlayersFromBrugg);

            hasTestAufgabe1HasPlayersFromCityPassed = true;
        }

        @Test
        @Order(2)
        void testAufgabe1HasPlayersFromCityWithNullAddress() {
            if (!hasTestAufgabe1HasPlayersFromCityPassed) {
                fail("testAufgabe1HasPlayersFromCity muss zuerst erfolgreich durchlaufen!");
            }

            // given
            List<Player> playersNullAddress = List.of(new Player("Schmidt"));
            Game game = new Game(playersNullAddress);

            // when
            boolean hasPlayersFromEmpty = game.hasPlayersFromCity("Basel");

            // then
            assertFalse(hasPlayersFromEmpty);
        }

        @Test
        @Order(3)
        void testAufgabe1HasPlayersFromCityWithNullParameter() {
            if (!hasTestAufgabe1HasPlayersFromCityPassed) {
                fail("testAufgabe1HasPlayersFromCity muss zuerst erfolgreich durchlaufen!");
            }

            // when
            boolean hasPlayersFromNull = game.hasPlayersFromCity(null);

            // then
            assertFalse(hasPlayersFromNull);
        }

        @Test
        void testAufgabe1PlayerWithAddressOneMatch() {
            // given
            Player mueller = PLAYERS.get(1);
            Address muellerAddress = mueller.getAddress();

            // when
            Optional<Player> playerWithAddressOptional = game.playerWithAddress(muellerAddress);

            // then
            Player playerWithAddress = playerWithAddressOptional.get(); // exists
            assertSame(mueller, playerWithAddress);
        }

        @Test
        void testAufgabe1PlayerWithAddressNoMatches() {
            // when
            Optional<Player> playerWithUnknownAddress = game.playerWithAddress(new Address("N/A", "N/A"));

            // then (Stream enthält keine Elemente, ist leer)
            assertTrue(playerWithUnknownAddress.isEmpty());

            // when
            Optional<Player> playerWithAddressNull = game.playerWithAddress(null);

            // then (Stream enthält keine Elemente, ist leer)
            assertTrue(playerWithAddressNull.isEmpty());
        }

        @Test
        void testAufgabe1PlayerWithAddressMultipleMatchesReturnsFirstPlayer() {
            // given
            Player schmidt = PLAYERS.get(2);

            // when
            Optional<Player> playerWithBaselAddressOptional = game.playerWithAddress(BASEL_ADDRESS);

            // then
            Player playerWithBaselAddress = playerWithBaselAddressOptional.get(); // exists
            assertSame(schmidt, playerWithBaselAddress);
        }

        @Test
        void testAufgabe1PlayerWithDifferentAddressObjectButSameStreetAndCityAndNull() {
            // given
            Player schmidt = PLAYERS.get(2);

            // when
            Optional<Player> playerWithNewBaselAddressOptional = game.playerWithAddress(new Address(BASEL_ADDRESS.getStreet(), BASEL_ADDRESS.getCity()));

            // then
            Player playerWithNewBaselAddress = playerWithNewBaselAddressOptional.get(); // exists
            assertSame(schmidt, playerWithNewBaselAddress);

            // when
            Optional<Player> playerWithNullAddress = game.playerWithAddress(new Address(null, null));

            // then (Stream enthält keine Elemente, ist leer)
            assertTrue(playerWithNullAddress.isEmpty());
        }

        @Test
        void testAufgabe1HighestScore() {
            // when
            int highestScore = game.highestScore();

            // then
            assertSame(HIGHEST_SCORE, highestScore);

            // given
            game = new Game(List.of(new Player("Fritz")));

            // when
            highestScore = game.highestScore();

            // then
            assertSame(0, highestScore);

            // given
            game = new Game(List.of(new Player("Fritz", 42, new Address("Hauptstrasse 1", "Zürich"), 0)));

            // when
            highestScore = game.highestScore();

            // then
            assertSame(0, highestScore);

            // given
            int score = 50;
            game = new Game(List.of(new Player("Fritz", 42, new Address("Hauptstrasse 1", "Zürich"), score)));

            // when
            highestScore = game.highestScore();

            // then
            assertSame(score, highestScore);
        }

        @Test
        void testAufgabe1HighestScoreScore0IfNoPlayers() {
            // given
            game = new Game(new ArrayList<>());

            // when
            int highestScore = game.highestScore();

            // then
            assertSame(0, highestScore);
        }

        @Test
        void testAufgabe1UniquePlayerNamesSortedContainsAllNames() {
            // when
            List<String> uniquePlayerNamesSorted = game.uniquePlayerNamesSorted();

            // then
            assertTrue(uniquePlayerNamesSorted.contains("Fritz"));
            assertTrue(uniquePlayerNamesSorted.contains("Müller"));
            assertTrue(uniquePlayerNamesSorted.contains("Schmidt"));
            assertTrue(uniquePlayerNamesSorted.contains("Wagner"));
            assertTrue(uniquePlayerNamesSorted.contains("Bettina"));
            assertTrue(uniquePlayerNamesSorted.contains("Hoffmann"));
            assertTrue(uniquePlayerNamesSorted.contains("Franz"));
        }

        @Test
        void testAufgabe1UniquePlayerNamesSortedOnlyUniqueNames() {
            // when
            List<String> uniquePlayerNamesSorted = game.uniquePlayerNamesSorted();

            // then
            assertEquals(7, uniquePlayerNamesSorted.size());
            assertTrue(uniquePlayerNamesSorted.contains("Fritz"));
            assertTrue(uniquePlayerNamesSorted.contains("Müller"));
            assertTrue(uniquePlayerNamesSorted.contains("Schmidt"));
            assertTrue(uniquePlayerNamesSorted.contains("Wagner"));
            assertTrue(uniquePlayerNamesSorted.contains("Bettina"));
            assertTrue(uniquePlayerNamesSorted.contains("Hoffmann"));
            assertTrue(uniquePlayerNamesSorted.contains("Franz"));
        }

        @Test
        void testAufgabe1UniquePlayerNamesSortedAreSorted() {
            // when
            List<String> uniquePlayerNamesSorted = game.uniquePlayerNamesSorted();

            // then
            String[] expectedNames = {"Bettina", "Franz", "Fritz", "Hoffmann", "Müller", "Schmidt", "Wagner"};
            assertArrayEquals(expectedNames, uniquePlayerNamesSorted.toArray());
        }

        @RepeatedTest(2) // 2 Punkte (Teilpunkte werden von Hand im Nachhinein vergeben)
        void testAufgabe1PlayersSortedByScoreDescending() {
            // when
            List<Player> playersSortedByScoreDescending = game.playersSortedByScoreDescending();

            // then
            Player[] expectedPlayers = {
                    PLAYERS.get(4), PLAYERS.get(5), PLAYERS.get(2), PLAYERS.get(6),
                    PLAYERS.get(3), PLAYERS.get(0), PLAYERS.get(7), PLAYERS.get(1)
            };
            assertArrayEquals(expectedPlayers, playersSortedByScoreDescending.toArray());
        }

        @RepeatedTest(3) // 3 Punkte (Teilpunkte werden von Hand im Nachhinein vergeben)
        void testAufgabe1PlayerByScore() {
            // when
            Map<Integer, Player> playerByScore = game.playerByScore();

            // then
            assertSame(PLAYERS.get(0), playerByScore.get(2));
            assertSame(PLAYERS.get(1), playerByScore.get(0));
            assertSame(PLAYERS.get(2), playerByScore.get(10));
            assertSame(PLAYERS.get(4), playerByScore.get(HIGHEST_SCORE));
        }
    }

    /**
     * Aufgabe 2
     */
    @Nested
    class Aufgabe2 {

       @Test
       void testAufgabe2ListPlayerNamesNullSafePlayersNoExceptionThrown() {
           // given
           Game game = new Game(PLAYERS);

           // when
           List<String> listPlayerNames = game.listPlayerNamesNullSafe();

           // then `listPlayerNames()` is called internally
           assertEquals(8, listPlayerNames.size());
           String[] expectedNames = {
                   PLAYERS.get(0).getName(), PLAYERS.get(1).getName(), PLAYERS.get(2).getName(), PLAYERS.get(3).getName(),
                   PLAYERS.get(4).getName(), PLAYERS.get(5).getName(), PLAYERS.get(6).getName(), PLAYERS.get(7).getName()
           };
           assertArrayEquals(expectedNames, listPlayerNames.toArray());

           // given
           List<Player> players = PLAYERS_NULL;
           game = new Game(players);
           assertThrows(NullPointerException.class, () -> GameUtils.listPlayerNames(players)); // listPlayerNames() still throws NullPointerException

           // when
           game.listPlayerNamesNullSafe();

           // then
           // no exception is thrown
       }

       @Test
       void testAufgabe2ListPlayerNamesNullSafePlayersIsNull() {
           // given
           List<Player> players = null;
           assertThrows(NullPointerException.class, () -> GameUtils.listPlayerNames(players));  // listPlayerNames() still throws NullPointerException
           Game game = new Game(players);

           // when
           List<String> emptyPlayerNames = game.listPlayerNamesNullSafe();

           // then
           // no exception is thrown
           assertEquals(0, emptyPlayerNames.size());
       }

       @Test
       void testAufgabe2ListPlayerNamesNullSafeEmptyList() {
           // given
           List<Player> players = PLAYERS_NULL;
           game = new Game(players);
           assertThrows(NullPointerException.class, () -> GameUtils.listPlayerNames(players)); // listPlayerNames() still throws NullPointerException

           // when
           List<String> playerNames = game.listPlayerNamesNullSafe();

           // then
           // no exception is thrown
           assertEquals(0, playerNames.size());
       }

       @Test
       void testAufgabe2ValidatePlayersNoExceptionIfValid() throws Exception {
           // given
           Game game = new Game(
                   List.of(
                           new Player(new Address("Hauptstrasse 3", "Brugg")),
                           new Player(new Address("Hauptstrasse 4", "Brugg"))
                   )
           );

           // when, then (no exception is thrown)
           game.validatePlayers();
       }
       @Test
       void testAufgabe2ValidatePlayersNull() {
           // given
           Game game = new Game(null);

           // when, then
           assertThrows(IllegalStateException.class, game::validatePlayers);
       }

       @Test
       void testAufgabe2ValidatePlayersNoAddress() {
           // given
           Game game = new Game(List.of(new Player("Fritz")));

           // when, then
           assertThrows(InvalidAddressException.class, game::validatePlayers);
       }

       @Test
       void testAufgabe2ValidatePlayersStreetOrCityNull() {
           // given
           game = new Game(
                   List.of(
                           new Player(new Address(null, "Brugg")),
                           new Player(new Address("Hauptstrasse 4", "Brugg"))
                   )
           );

           // when, then
           assertThrows(InvalidAddressException.class, game::validatePlayers);

           // given
           game = new Game(
                   List.of(
                           new Player(new Address("Hauptstrasse 3", "Brugg")),
                           new Player(new Address("Hauptstrasse 4", null))
                   )
           );

           // when, then
           assertThrows(InvalidAddressException.class, game::validatePlayers);
       }

       @Test
       void testAufgabe2ValidatePlayersStreetOrCityEmpty() {
           // given
           game = new Game(
                   List.of(
                           new Player(new Address("", "Brugg")),
                           new Player(new Address("Hauptstrasse 4", "Brugg"))
                   )
           );

           // when, then
           assertThrows(InvalidAddressException.class, game::validatePlayers);

           // given
           game = new Game(
                   List.of(
                           new Player(new Address("Hauptstrasse 3", "Brugg")),
                           new Player(new Address("Hauptstrasse 4", ""))
                   )
           );

           // when, then
           assertThrows(InvalidAddressException.class, game::validatePlayers);
       }

    }
}
