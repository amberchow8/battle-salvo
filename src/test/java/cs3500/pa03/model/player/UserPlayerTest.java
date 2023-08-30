package cs3500.pa03.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ship.Ship;
import cs3500.pa03.model.ship.ShipType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods within the class UserPlayer.
 */
class UserPlayerTest {
  UserPlayer player;
  Coord coord;

  /**
   * Sets up fields for testing.
   */
  @BeforeEach
  public void setup() {
    List<Coord> startingCoords = new ArrayList<>();
    List<Ship> startingShips = new ArrayList<>();
    List<Coord> shotCoords = new ArrayList<>();
    coord = new Coord(1, 1);
    shotCoords.add(coord);
    UserPlayerShots shots = new UserPlayerShots(shotCoords);
    player = new UserPlayer(startingCoords, startingShips, shots);
  }

  /**
   * Tests the method name.
   */
  @Test
  public void testName() {
    assertEquals(player.name(), "Console Player");
  }

  /**
   * Tests the method takeShots.
   */
  @Test
  public void testTakeShots() {
    List<Coord> cords = player.takeShots();
    assertTrue(player.takeShots().contains(coord));
  }

  /**
   * Tests the method reportDamage.
   */
  @Test
  public void testReportDamage() {
    List<Coord> startingCoords = new ArrayList<>();
    List<Ship> startingShips = new ArrayList<>();
    Ship ship = new Ship(ShipType.CARRIER);
    startingShips.add(ship);
    List<Coord> shotCoords = new ArrayList<>();
    coord = new Coord(1, 1);
    UserPlayerShots shots = new UserPlayerShots(shotCoords);
    UserPlayer newPlayer = new UserPlayer(startingCoords, startingShips, shots);

    ArrayList<Coord> opponentShots = new ArrayList<>();
    Coord shotOne = new Coord(0, 5);
    Coord shotTwo = new Coord(6, 6);
    opponentShots.add(shotOne);
    opponentShots.add(shotTwo);
    List<Coord> damage = newPlayer.reportDamage(opponentShots);

    assertEquals(1, damage.size());
  }

  /**
   * Tests the method setup.
   */
  @Test
  public void testSetup() {
    Map<ShipType, Integer> map = new HashMap<>();
    map.put(ShipType.CARRIER, 1);
    map.put(ShipType.BATTLESHIP, 1);
    map.put(ShipType.DESTROYER, 1);
    map.put(ShipType.SUBMARINE, 1);
    List<Ship> setup = player.setup(6, 6, map);

    // check that setup creates 4 ships
    assertEquals(4, setup.size());
  }
}