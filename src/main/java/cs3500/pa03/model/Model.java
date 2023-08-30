package cs3500.pa03.model;

import cs3500.pa03.model.board.OpponentBoard;
import cs3500.pa03.model.board.UserBoard;
import cs3500.pa03.model.player.FakePlayer;
import cs3500.pa03.model.player.UserPlayer;
import cs3500.pa03.model.player.UserPlayerShots;
import cs3500.pa03.model.ship.Ship;
import cs3500.pa03.model.ship.ShipType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a model for a BattleSalvo game, which stores and handles
 * game functionality.
 */
public class Model {
  private UserPlayer user;
  private FakePlayer computer;
  private UserBoard userBoard;
  private OpponentBoard computerBoard;

  /**
   * Constructor for use in the Driver class solely to make the Controller.
   * Has all null fields until game is set up based on user input from the Controller
   * (no methods are called on this until after that point).
   */
  public Model() {
    user = null;
    computer = null;
    userBoard = null;
    computerBoard = null;
  }

  /**
   * Given board dimensions and fleet specifications, sets up the beginning game state by
   * initializing fields to create players and boards with random ship placements.
   *
   * @param height the height of the board
   * @param width the width of the board
   * @param specifications the specifications of the fleet, where each entry corresponds to
   *                       the amount of each ship type to be placed on the board
   */
  public UserPlayerShots setup(int height, int width, Map<ShipType, Integer> specifications) {
    // creates dummy player to generate random ships for each player
    // (since implementation is the same for all subclasses)
    UserPlayerShots userShots = new UserPlayerShots(new ArrayList<>());
    UserPlayer shipPlacer = new UserPlayer(new ArrayList<>(),
        new ArrayList<>(), userShots);

    // creates the console player and their board
    List<Ship> userShips = shipPlacer.setup(height, width, specifications);
    user = new UserPlayer(new ArrayList<>(), userShips, userShots);
    userBoard = new UserBoard(userShips, height, width);

    // creates the fake ai player and their board
    List<Ship> aiShips = shipPlacer.setup(height, width, specifications);
    computerBoard = new OpponentBoard(aiShips, height, width);
    computer = new FakePlayer(new ArrayList<>(), aiShips, computerBoard);

    return userShots;
  }

  /**
   * Delegates to userBoard to retrieve the string representation of the
   * console player's board.
   *
   * @return a String[][] of the current state of the user's board
   */
  public String[][] convertUserBoard() {
    return userBoard.convert();
  }

  /**
   * Delegates to computer to retrieve the string representation of the
   * ai player's board.
   *
   * @return a String[][] of the current state of the ai's board
   */
  public String[][] convertOpponentBoard() {
    return computerBoard.convert();
  }

  /**
   * Determines how many shots the user has left by counting unsunk ships
   * remaining on the user board.
   *
   * @return how many shots the user has left
   */
  public int availableUserShots() {
    if (userBoard.amtShipsLeft() > userBoard.amtOpenCells()) {
      return userBoard.amtOpenCells();
    } else {
      return userBoard.amtShipsLeft();
    }
  }

  /**
   * Determines the state the game based on whoever sinks all their opponents ships
   * before the other.
   *
   * @return the result of the game for the console player
   */
  public GameResult currentGameState() {
    if (userBoard.amtShipsLeft() == 0 && computerBoard.amtShipsLeft() != 0) {
      return GameResult.LOSE;
    } else if (userBoard.amtShipsLeft() != 0 && computerBoard.amtShipsLeft() == 0) {
      return GameResult.WIN;
    } else {
      return GameResult.DRAW;
    }
  }

  /**
   * Determines if the given shot, taken by the user, is valid.
   *
   * @param shot the shot taken by the user
   * @param shotsAlreadyTaken shots taken by this user earlier in this round
   * @return true if the user can fire at this position
   */
  public boolean validUserShot(Coord shot, List<Coord> shotsAlreadyTaken) {
    boolean isValidShot = false;

    // for every cell in this
    for (int i = 0; i < computerBoard.getBoard().length; i++) {
      for (int j = 0; j < computerBoard.getBoard()[0].length; j++) {
        Coord current = computerBoard.getBoard()[i][j];
        // check if it overlaps the taken shot
        if (shot.overlaps(current) && (current.getStatus() == CellStatus.UNTOUCHED_SHIP
            || current.getStatus() == CellStatus.NOTHING)) {
          boolean overlapsTakenShot = false;

          // check if it was already taken this round
          for (Coord c : shotsAlreadyTaken) {
            if (c.overlaps(shot)) {
              overlapsTakenShot = true;
              break;
            }
          }
          isValidShot = !overlapsTakenShot;
        }
      }
    }
    return isValidShot;
  }

  /**
   * Delegate to both players to take their shots, and update
   * the status of the game for both players.
   */
  public void updateRound(List<Coord> userShots, List<Coord> opponentShots) {
    // update the boards accordingly
    userBoard.updateBoard(opponentShots);
    computerBoard.updateBoard(userShots);
  }

  /**
   * Tells the user player to take their shots.
   *
   * @return shots the shots taken by the player
   */
  public List<Coord> takeUserShots() {
    return user.takeShots();
  }

  /**
   * Tells the ai player to take their shots.
   *
   * @return shots the shots taken by the player
   */
  public List<Coord> takeOpponentShots() {
    return computer.takeShots();
  }
}

