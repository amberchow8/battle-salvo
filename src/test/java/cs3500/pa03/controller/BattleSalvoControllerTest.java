package cs3500.pa03.controller;

import cs3500.pa03.model.Model;
import cs3500.pa03.view.View;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods in the class BattleSalvoController.
 */
class BattleSalvoControllerTest {

  /**
   * Tests a game where the console player wins.
   */
  @Test
  public void testWinningGame() {
    // sets up a controller with user input meant to win
    Model model = new Model();
    String input = "6 6\n1 1 1 1\n0 0\n0 1\n0 2\n0 3\n0 4\n0 5\n1 1\n1 2\n1 3\n1 4\n1 5\n1 0\n"
        + "2 1\n2 2\n2 3\n2 4\n2 5\n2 0\n3 1\n3 2\n3 3\n3 4\n3 5\n3 0\n4 1\n4 2\n4 3\n4 5\n4 0\n"
        + "4 4\n5 1\n5 2\n5 3\n5 4\n5 5\n5 0\n";
    StringReader stringReader = new StringReader(input);
    StringBuilder stringBuilder = new StringBuilder();
    View view = new View(stringReader, stringBuilder);
    BattleSalvoController controller = new BattleSalvoController(model, view);

    controller.runGame();
  }

  /**
   * Tests a game where the console player enters invalid setup dimensions.
   */
  @Test
  public void testBadGame() {
    // sets up a controller with user input meant to win
    Model model = new Model();
    String input = "n p\n 15 2\n6 6\n5 0 5 5\n0 1 1 1\n6 6 6 6\n1 1 1 0\n1 1 0 1\n1 1 1 1\n0 "
        + "0\n0 1\n0 1\n6 6\n0 2\n0 3\n0 4\n0 5\n1 1\n1 2\n1 3\n1 4\n1 5\n1 0\n2 1\n2 2\n2 "
        + "3\n2 4\n2 "
        + "5\n2 0\n3 1\n3 2\n3 3\n3 4\n3 "
        + "5\n3 0\n4 1\n4 2\n4 3\n4 5\n4 0\n4 4\n5 1\n5 2\n5 3\n5 4\n5 5\n5 0\n";
    StringReader stringReader = new StringReader(input);
    StringBuilder stringBuilder = new StringBuilder();
    View view = new View(stringReader, stringBuilder);
    BattleSalvoController controller = new BattleSalvoController(model, view);

    controller.runGame();
  }
}