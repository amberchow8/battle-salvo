package cs3500.pa03.model.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.ship.Ship;
import cs3500.pa03.model.ship.ShipType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods in the class OpponentBoard.
 */
class UserBoardTest {
  Board board;
  Ship carrier;

  /**
   * Sets up fields for testing.
   */
  @BeforeEach
  public void setup() {
    List<Ship> ships = new ArrayList<>();
    carrier = new Ship(ShipType.CARRIER);
    ships.add(carrier);
    board = new UserBoard(ships, 6, 6);
  }

  /**
   * Tests the method getBoard.
   */
  @Test
  public void testGetBoard() {
    assertEquals(board.getBoard().length, 6);
    assertEquals(board.getBoard()[0].length, 6);
  }

  /**
   * Tests that the constructor set up its cells correctly.
   */
  @Test
  public void testConstructor() {
    int shipCells = 0;

    for (int i = 0; i < 6; i++) {
      for (int j = 0; j < 6; j++) {
        Coord current = board.getBoard()[i][j];
        // check that its x and y values are its location on the board
        assertEquals(current.getX(), j);
        assertEquals(current.getY(), i);
        // increase shipCells by one if its status is UNTOUCHED_SHIP
        if (current.getStatus() == CellStatus.UNTOUCHED_SHIP) {
          shipCells += 1;
        }
      }
    }

    assertEquals(shipCells, 6);
  }

  /**
   * Tests the method convert.
   */
  @Test
  public void testConvert() {
    board.getBoard()[5][5].updateStatus(CellStatus.HIT_SHIP);
    board.getBoard()[5][4].updateStatus(CellStatus.MISSED_SHIP);

    String[][] expected = board.convert();
    assertEquals(expected[0][0], "S");
    assertEquals(expected[3][3], "-");
    assertEquals(expected[5][5], "H");
    assertEquals(expected[5][4], "*");
  }

  /**
   * Tests the method amtShipsLeft.
   */
  @Test
  public void testAmtShipsLeft() {
    assertEquals(board.amtShipsLeft(), 1);

    // sink a ship and check that the amount left decreases
    for (int i = 0; i < 6; i++) {
      carrier.getCells().get(i).updateStatus(CellStatus.HIT_SHIP);
    }
    board.updateBoard(carrier.getCells());
    assertEquals(board.amtShipsLeft(), 0);
  }

  /**
   * Tests the method isCoordOpen.
   */
  @Test
  public void testIsCoordOpen() {
    board.getBoard()[5][5].updateStatus(CellStatus.HIT_SHIP);
    board.getBoard()[5][4].updateStatus(CellStatus.MISSED_SHIP);

    assertTrue(board.isCoordOpen(new Coord(0, 0)));
    assertTrue(board.isCoordOpen(new Coord(0, 2)));
    assertFalse(board.isCoordOpen(new Coord(5, 5)));
    assertFalse(board.isCoordOpen(new Coord(4, 5)));
  }

  /**
   * Tests the method amtOpenCells.
   */
  @Test
  public void testAmtOpenCells() {
    assertEquals(board.amtOpenCells(), 36);
    board.getBoard()[0][0].updateStatus(CellStatus.MISSED_SHIP);
    assertEquals(board.amtOpenCells(), 35);
  }

  /**
   * Tests the method updateBoard.
   */
  @Test
  public void testUpdateBoard() {
    assertEquals(board.amtOpenCells(), 36);
    List<Coord> opponentShots = new ArrayList<>();
    opponentShots.add(new Coord(0, 0));
    opponentShots.add(new Coord(1, 1));
    opponentShots.add(new Coord(2, 2));
    board.updateBoard(opponentShots);
    assertEquals(33, board.amtOpenCells());
  }

}