package cs3500.pa03.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods in the class View.
 */
class ViewTest {
  private View view;
  private StringBuilder stringBuilder;
  private View badView;

  /**
   * Sets up examples for testing.
   */
  @BeforeEach
  public void setup() {
    stringBuilder = new StringBuilder();
    StringReader stringReader = new StringReader("hi");
    view = new View(stringReader, stringBuilder);
    MockAppendable mock = new MockAppendable();
    badView = new View(stringReader, mock);
  }

  /**
   * Tests that the print method successfully appends to an appendable.
   */
  @Test
  public void testDisplaySuccess() {
    assertEquals(stringBuilder.toString(), "");
    view.displayMessage("hi");
    view.displayMessage(" people");
    assertEquals(stringBuilder.toString(), "hi people");
  }

  /**
   * Tests that the print method throws a successful error.
   */
  @Test
  public void testDisplayFail() {
    Exception exception = assertThrows(RuntimeException.class, () ->
        badView.displayMessage("hi"), "Mock throwing an error");
    assertEquals("Error displaying message, sorry!", exception.getMessage());
  }

  /**
   * Tests the method getReponse that reads the next line.
   */
  @Test
  public void testGetResponse() {
    stringBuilder.append("hi");
    assertEquals(view.getResponse(), "hi");
    Exception exception = assertThrows(RuntimeException.class, () ->
        view.getResponse(), "No input left to read");
    assertEquals("No input left to read", exception.getMessage());
  }

  /**
   * Tests the method displayOpponentBoard.
   */
  @Test
  public void testDisplayBothBoards() {
    String[][] testBoard = {
        {"A", "B", "C"},
        {"D", "E", "F"},
        {"G", "H", "I"}
    };

    assertEquals(stringBuilder.toString(), "");
    view.displayBothBoards(testBoard, testBoard, 8);
    assertEquals(stringBuilder.toString(), "Opponent board: \n"
        + "A B C \nD E F \nG H I \n\nYour board: \nA B C \nD E F \nG H I \n"
        + "\nPlease enter 8 shots in the form (x-coordinate y-coordinate),\n"
        + "with each shot on its own line and the top left cell starting as '0 0':\n");

  }

}