package cs3500.pa03.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cs3500.pa03.model.ship.ShipType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods within the Model class.
 */
class ModelTest {
  Model model;

  /**
   * Sets up fields for testing.
   */
  @BeforeEach
  public void setup() {
    model = new Model();
    Map<ShipType, Integer> specifications = new HashMap<ShipType, Integer>();
    specifications.put(ShipType.CARRIER, 2);
    specifications.put(ShipType.BATTLESHIP, 2);
    specifications.put(ShipType.DESTROYER, 2);
    specifications.put(ShipType.SUBMARINE, 2);
    model.setup(8, 8, specifications);
  }

  /**
   * Tests the method convertUserBoard.
   */
  @Test
  public void testConvertUserBoard() {
    String[][] userBoard = model.convertUserBoard();
    assertEquals(userBoard.length, 8);
    assertEquals(userBoard[0].length, 8);
  }

  /**
   * Tests the method convertOpponentBoard.
   */
  @Test
  public void testConvertOpponentBoard() {
    String[][] board = model.convertOpponentBoard();
    assertEquals(board.length, 8);
    assertEquals(board[0].length, 8);
  }

  /**
   * Tests the method availableUserShots.
   */
  @Test
  public void testAvailableUserShots() {
    assertEquals(model.availableUserShots(), 8);
  }

  /**
   * Tests the method currentGameState.
   */
  @Test
  public void testCurrentGameState() {
    assertEquals(model.currentGameState(), GameResult.DRAW);
  }

  /**
   * Tests the methods updateRound and validUserShot.
   */
  @Test
  public void testUpdateRound() {
    String[][] board = model.convertOpponentBoard();
    // count that before taking shots, all cells in the opponent
    // board are either untouched or nothing
    int hitByOpponent = 0;
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        String current = board[i][j];
        if (current.equals("*") || current.equals("H")) {
          hitByOpponent += 1;
        }
      }
    }
    assertEquals(hitByOpponent, 0);

    List<Coord> userShots = new ArrayList<>();
    userShots.add(new Coord(0, 0));
    userShots.add(new Coord(1, 1));
    userShots.add(new Coord(2, 2));
    userShots.add(new Coord(3, 3));
    userShots.add(new Coord(4, 4));
    List<Coord> opponentShots = new ArrayList<>();

    // checks that all 5 shots above are valid
    for (Coord c : opponentShots) {
      assertTrue(model.validUserShot(c, new ArrayList<>()));
    }

    // take 5 opponent shots and check that hitsByOpponent
    // increases accordingly
    model.updateRound(userShots, opponentShots);
    String[][] newBoard = model.convertOpponentBoard();
    for (int i = 0; i < newBoard.length; i++) {
      for (int j = 0; j < newBoard[0].length; j++) {
        String current = newBoard[i][j];
        if (current.equals("*") || current.equals("S")) {
          hitByOpponent += 1;
        }
      }
    }
    assertEquals(5, hitByOpponent);
  }
}