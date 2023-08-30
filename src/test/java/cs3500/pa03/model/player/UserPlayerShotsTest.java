package cs3500.pa03.model.player;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods within the class UserPlayerShots.
 */
class UserPlayerShotsTest {

  /**
   * Tests the methods getShots and updateShots.
   */
  @Test
  public void testShotMethods() {
    ArrayList<Coord> coords = new ArrayList<Coord>();
    Coord coordOne = new Coord(1, 1);
    Coord coordTwo = new Coord(2, 2);
    coordTwo.updateStatus(CellStatus.UNTOUCHED_SHIP);
    coords.add(coordOne);
    UserPlayerShots shots = new UserPlayerShots(coords);
    assertFalse(shots.getShots().contains(coordTwo));
    coords.add(coordTwo);
    shots.updateShots(coords);
    assertTrue(shots.getShots().contains(coordTwo));
  }
}