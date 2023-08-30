package cs3500.pa03.view;

import java.io.IOException;
import java.util.Scanner;

/**
 * Represents a BattleSalvo view that handles
 * user input and output.
 */
public class View {
  private Scanner scanner;
  private Appendable appendable;

  /**
   * Constructor for a view.
   *
   * @param readable the source to read from
   * @param appendable where to append output
   */
  public View(Readable readable, Appendable appendable) {
    scanner = new Scanner(readable);
    this.appendable = appendable;
  }


  /**
   * Appends the given string to the given appendable, intended to
   * display text to the user.
   *
   * @param content the string to add to the appendable
   */
  public void displayMessage(String content) {
    try {
      appendable.append(content);
    } catch (IOException e) {
      throw new RuntimeException("Error displaying message, sorry!");
    }
  }

  /**
   * Uses a scanner to return the next line of this readable.
   */
  public String getResponse() {
    try {
      return scanner.nextLine();
    } catch (Exception e) {
      throw new RuntimeException("No input left to read");
    }
  }

  /**
   * Uses displayMessage to show the opponent board.
   *
   * @param opponentBoard the opponent board
   * @param userBoard the user board
   * @param shots the amount of shots the user can take
   */
  public void displayBothBoards(String[][] opponentBoard, String[][] userBoard, int shots) {
    displayMessage("Opponent board: \n"
        + displayBoard(opponentBoard)
        + "\nYour board: \n"
        + displayBoard(userBoard)
        + "\nPlease enter " + shots + " shots in the form (x-coordinate y-coordinate),\n"
        + "with each shot on its own line and the top left cell starting as '0 0':\n");
  }

  /**
   * Prints the 2D array of Strings as a grid.
   *
   * @param board the board to display
   * @return the entire array as a grid
   */
  private String displayBoard(String[][] board) {
    StringBuilder grid = new StringBuilder();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        // display all strings in the given as a grid
        grid.append(board[i][j] + " ");
      }
      grid.append("\n");
    }
    return grid.toString();
  }
}
