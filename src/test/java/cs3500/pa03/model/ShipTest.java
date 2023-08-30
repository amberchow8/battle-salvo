package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.ship.Ship;
import cs3500.pa03.model.ship.ShipType;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods within the class Ship.
 */
class ShipTest {
  Ship carrier;
  Ship carrierTwo;

  /**
   * Sets up fields for testing.
   */
  @BeforeEach
  public void setup() {
    carrier = new Ship(ShipType.CARRIER);
    carrierTwo = new Ship(ShipType.CARRIER);
  }

  /**
   * Tests the getter method getCells.
   */
  @Test
  public void testGetCells() {

    assertEquals(carrier.getCells().size(), 6);

    for (Coord c : carrier.getCells()) {
      assertEquals(c.getStatus(), CellStatus.UNTOUCHED_SHIP);
    }
  }

  /**
   * Tests the method overlapsCoord.
   */
  @Test
  public void testOverlapsCoord() {
    Coord goodCoord = new Coord(0, 0);
    Coord badCoord = new Coord(1, 0);
    assertTrue(carrier.overlapsCoord(goodCoord));
    assertFalse(carrier.overlapsCoord(badCoord));
  }

  /**
   * Tests the setup method that sets the coordinates of this
   * ship to fit in a given board.
   */
  @Test
  public void testSetupCoords() {
    int nonFittingShips = 0;
    ArrayList<Ship> ships = new ArrayList<>();

    // to test that calling setupCoords() doesn't place the ship out of bounds,
    // call setupCoords 10 times on a 10x10 board
    for (int i = 0; i < 10; i++) {
      carrier.setupCoords(10, 10, ships);
      // if the ship isn't fully on the board, increasing nonFittingShips by 1
      for (Coord c : carrier.getCells()) {
        if (c.getX() < 0 || c.getX() > 9 || c.getY() < 0 || c.getY() > 9) {
          nonFittingShips += 1;
        }
      }
    }

    // check that there are 0 ships that don't fit
    assertEquals(nonFittingShips, 0);

    // randomly set up 2 carrier ships and count their identical coordinates
    carrier.setupCoords(10, 10, ships);
    carrierTwo.setupCoords(10, 10, ships);
    int identicalPositions = 0;
    for (Coord c1 : carrier.getCells()) {
      for (Coord c2 : carrierTwo.getCells()) {
        if (c1.overlaps(c2)) {
          identicalPositions += 1;
        }
      }
    }
  }

  /**
   * Tests the method isSunk.
   */
  @Test
  public void testIsSunk() {
    assertFalse(carrier.isSunk());

    for (int i = 0; i < 6; i++) {
      carrierTwo.getCells().get(i).updateStatus(CellStatus.HIT_SHIP);
    }
    assertTrue(carrierTwo.isSunk());
  }
}
