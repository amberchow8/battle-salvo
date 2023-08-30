package cs3500.pa03.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.board.OpponentBoard;
import cs3500.pa03.model.ship.Ship;
import cs3500.pa03.model.ship.ShipType;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods within the class FakePlayer.
 */
class FakePlayerTest {
  FakePlayer player;
  Ship ship;
  OpponentBoard board;

  /**
   * Sets up fields for testing.
   */
  @BeforeEach
  public void setup() {
    ArrayList<Ship> startingShips = new ArrayList<>();
    ship = new Ship(ShipType.CARRIER);
    startingShips.add(ship);
    startingShips.add(new Ship(ShipType.CARRIER));
    board = new OpponentBoard(startingShips, 6, 6);
    List<Coord> startingCoords = new ArrayList<>();
    player = new FakePlayer(startingCoords, startingShips, board);
  }

  /**
   * Tests the method name.
   */
  @Test
  public void testName() {
    assertEquals(player.name(), "amber-lauren");
  }

  /**
   * Tests the method endGame
   */
  @Test
  public void testEndGame() {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    player.endGame(GameResult.WIN, "You win");
    assertTrue(output.toString().contains("WIN: You win"));
    output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    player.endGame(GameResult.LOSE, "You lose");
    assertTrue(output.toString().contains("LOSE: You lose"));
    assertFalse(output.toString().contains("WIN: You win"));
    output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));
    player.endGame(GameResult.DRAW, "It's a draw");
    assertTrue(output.toString().contains("DRAW: It's a draw"));
    System.setOut(System.out);
  }

  /**
   * Tests the method takeShots
   */
  @Test
  public void testTakeShots() {
    List<Coord> shotsOne = player.takeShots();
    // check that the player took two shots, for the two ships on their board
    assertEquals(shotsOne.size(), 2);

    assertEquals(2, player.shipsLeft);
    List<Coord> test = player.takeShots();
    assertEquals(2, test.size());
    test = player.takeShots();
    assertEquals(2, test.size());

    player.successfulShots.addAll(new ArrayList<>(
        Arrays.asList(
            new Coord(0, 1),
            new Coord(0, 2))));
    test = player.takeShots();
    assertEquals(2, test.size());
    player.takeShots();
    player.takeShots();
    player.takeShots();
    player.takeShots();
    player.takeShots();
    player.takeShots();
    assertEquals(2, test.size());
  }

  /**
   * Tests the method setup.
   */
  @Test
  public void testSetup() {
    Map<ShipType, Integer> map = new LinkedHashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);
    List<Ship> setup = player.setup(6, 6, map);

    // check that setup creates 6 ships
    assertEquals(4, setup.size());
  }

  /**
   * Tests the method reportDamage.
   */
  @Test
  public void testReportDamage() {
    ArrayList<Coord> opponentShots = new ArrayList<>();
    Coord shotOne = new Coord(0, 5);
    Coord shotTwo = new Coord(6, 6);
    opponentShots.add(shotOne);
    opponentShots.add(shotTwo);
    List<Coord> damage = player.reportDamage(opponentShots);

    assertTrue(damage.contains(shotOne));
    assertFalse(damage.contains(shotTwo));
  }
}