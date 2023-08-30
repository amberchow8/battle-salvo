package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods in the class Coord.
 */
class CoordTest {
  Coord coordOne;
  Coord coordTwo;

  /**
   * Creates fields for testing.
   */
  @BeforeEach
  public void setup() {
    coordOne = new Coord(1, 1);
    coordTwo = new Coord(2, 2);
    coordTwo.updateStatus(CellStatus.UNTOUCHED_SHIP);
  }

  /**
   * Tests the getter method getX.
   */
  @Test
  public void testGetX() {
    assertEquals(coordOne.getX(), 1);
  }

  /**
   * Tests the getter method getU.
   */
  @Test
  public void testGetY() {
    assertEquals(coordTwo.getY(), 2);
  }

  /**
   * Tests the methods updateStatus and getStatus.
   */
  @Test
  public void testUpdateStatus() {
    assertEquals(coordOne.getStatus(), CellStatus.NOTHING);
    coordOne.updateStatus(CellStatus.MISSED_SHIP);
    assertEquals(coordOne.getStatus(), CellStatus.MISSED_SHIP);
  }

}