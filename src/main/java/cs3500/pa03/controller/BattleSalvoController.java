package cs3500.pa03.controller;

import cs3500.pa03.model.CellStatus;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.GameResult;
import cs3500.pa03.model.Model;
import cs3500.pa03.model.player.UserPlayerShots;
import cs3500.pa03.model.ship.ShipType;
import cs3500.pa03.view.View;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a controller for a game of BattleSalvo.
 */
public class BattleSalvoController {
  private Model model;
  private View view;
  private UserPlayerShots userShots;

  /**
   * Instantiates a controller given the other MVC components to interact with.
   *
   * @param m the model to handle application functionality
   * @param v the view to handle user i/o
   */
  public BattleSalvoController(Model m, View v) {
    model = m;
    view = v;
    // instantiates a null userShots since the game and shots haven't been set up yet
    userShots = null;
  }

  /**
   * Utilies helper methods to run an entire BattleSalvo game,
   * processing user input and delgating to the model for updating the state.
   */
  public void runGame() {
    // get user input for board and fleet dimensions, and set up the game accordingly
    setup();

    // while the game is still playable, run the next round
    while (model.currentGameState() != GameResult.WIN
        && model.currentGameState() != GameResult.LOSE) {
      playRound();
    }

    // once the game is over, return a message reporting the result
    String endingMessage;
    if (model.currentGameState() == GameResult.WIN) {
      endingMessage = "Congrats, you won!!";
    } else if (model.currentGameState() == GameResult.LOSE) {
      endingMessage = "Sorry, you just lost to my ai player!";
    } else {
      endingMessage = "Sorry, you and my ai player tied!";
    }
    view.displayMessage(endingMessage);
  }

  /**
   * Simulates one round of the game, from getting and checking user shots to
   * updating the boards in the model.
   */
  private void playRound() {
    // display the boards
    String[][] opponentBoard = model.convertOpponentBoard();
    String[][] userBoard = model.convertUserBoard();
    int shotsAvailable = model.availableUserShots();
    view.displayBothBoards(opponentBoard, userBoard, shotsAvailable);

    // validate user shot input and then store it in the console container
    List<Coord> shotsInput = validateUserShots();
    userShots.updateShots(shotsInput);

    // delegate to the model to take official shots for both players
    List<Coord> consoleShots = model.takeUserShots();
    List<Coord> aiShots = model.takeOpponentShots();

    // update the game state accordingly
    model.updateRound(consoleShots, aiShots);
  }

  /**
   * Processes user shot input and returns them as a list of coordinates
   *
   * @return the completed list of user shots
   */
  private List<Coord> validateUserShots() {
    String badCoordMessage = "Shot must be valid on the board, not fired yet, "
        + "and in the form (x y),\nwhere the top left cell is (0 0). Try again!\n";

    List<Coord> shotsSoFar = new ArrayList<>();

    // keep adding and checking shots until its the right size
    while (shotsSoFar.size() != model.availableUserShots()) {
      String input = view.getResponse();

      boolean badCoord = true;
      while (badCoord) {
        // if the coord is a valid input, add it to the list then exit the loop
        if (validCoord(input, shotsSoFar)) {
          String[] parts = input.split(" ");
          Integer coordX = Integer.parseInt(parts[0]);
          Integer coordY = Integer.parseInt(parts[1]);
          Coord shot = new Coord(coordX, coordY);
          shot.updateStatus(CellStatus.UNTOUCHED_SHIP);
          shotsSoFar.add(shot);
          badCoord = false;
        } else {
          // if not, give them another try
          view.displayMessage(badCoordMessage);
          input = view.getResponse();
        }
      }
    }
    return shotsSoFar;
  }

  /**
   * Determines if the user's input is a valid coordinate.
   *
   * @param input the user input as a string
   * @param shotsSoFar previous shots successfully taken during this round
   * @return true if the given coordinate is open on the board and not already in
   *         the list of shotsSoFar
   */
  private boolean validCoord(String input, List<Coord> shotsSoFar) {
    try {
      String[] parts = input.split(" ");
      Integer coordX = Integer.parseInt(parts[0]);
      Integer coordY = Integer.parseInt(parts[1]);
      Coord shot = new Coord(coordX, coordY);
      // if the input is in the proper format, check if its valid on the board
      return model.validUserShot(shot, shotsSoFar);
    } catch (Exception e) {
      // if the input isn't in the form "integer integer", return false
      return false;
    }
  }

  /**
   * Sets up the game by handling user input for board and fleet sizes, then
   * delegating to the model to generate random ship placements based on the input.
   */
  private void setup() {
    String wrongBoardMessage = "Dimensions must be in the form 'height width',\n"
        + "where both are between 6 and 15 inclusive. Try again!\n";

    // prompt user to input board dimensions
    int boardHeight = 0;
    int boardWidth = 0;
    view.displayMessage("Welcome to BattleSalvo!\nPlease enter board dimensions in the "
        + "form 'height width',\nwhere both have to be between 6 and 15 inclusive:\n");
    String boardDimensions = view.getResponse();
    boolean badBoard = true;
    // continue to take in user input until it satisfies the board requirements
    while (badBoard) {
      if (validBoard(boardDimensions)) {
        String[] parts = boardDimensions.split(" ");
        boardHeight = Integer.parseInt(parts[0]);
        boardWidth = Integer.parseInt(parts[1]);
        badBoard = false;
      } else {
        view.displayMessage(wrongBoardMessage);
        boardDimensions = view.getResponse();
      }
    }

    // prompt user to input fleet specifications
    int fleetSizeMax = determineFleetMax(boardHeight, boardWidth);
    String wrongFleetMessage = "Fleet size must be in the form 'Carrier Battleship Destroyer "
        + "Submarine' and have at least one of each ship type.\nThe total can't exceed "
        + fleetSizeMax + " ships. Try again!\n";
    view.displayMessage("Perfect! Please enter your fleet size in the form 'Carrier "
        + "Battleship Destroyer Submarine'.\nThe total can't exceed " + fleetSizeMax + " ships:\n");
    String fleetSize = view.getResponse();
    boolean badFleet = true;
    int carriers = 0;
    int battleships = 0;
    int destroyers = 0;
    int submarines = 0;
    // continue to take in user input until it satisfies the fleet requirements
    while (badFleet) {
      if (validFleet(fleetSize, fleetSizeMax)) {
        String[] parts = fleetSize.split(" ");
        carriers = Integer.parseInt(parts[0]);
        battleships = Integer.parseInt(parts[1]);
        destroyers = Integer.parseInt(parts[2]);
        submarines = Integer.parseInt(parts[3]);
        badFleet = false;
      } else {
        view.displayMessage(wrongFleetMessage);
        fleetSize = view.getResponse();
      }
    }
    // initialize the game by delegating to the model to setup players, boards, and ships
    initializeGame(boardHeight, boardWidth, carriers, battleships, destroyers, submarines);
  }

  /**
   * Given user specifications, delegates to the model to generate the starting game
   * state by placing random ships based on fleet and board size.
   *
   * @param height the height of the board
   * @param width the width of the baord
   * @param carriers the amount of carriers
   * @param battleships the amount of battleships
   * @param destroyers the amount of destroyers
   * @param submarines the amount of submarines
   */
  private void initializeGame(int height, int width, int carriers, int battleships,
                              int destroyers, int submarines) {
    // create map of ship type to ship amount
    Map<ShipType, Integer> specifications = new LinkedHashMap<>();
    specifications.put(ShipType.CARRIER, carriers);
    specifications.put(ShipType.BATTLESHIP, battleships);
    specifications.put(ShipType.DESTROYER, destroyers);
    specifications.put(ShipType.SUBMARINE, submarines);

    // pass all these values to the model, to set up random ship placements on the board
    userShots = model.setup(height, width, specifications);
  }

  /**
   * Given board dimensions, determines the maximum fleet size
   * as the smaller of the two.
   *
   * @param height board height
   * @param width board width
   * @return the maximum fleet size
   */
  private int determineFleetMax(int height, int width) {
    if (height < width) {
      return height;
    } else {
      return width;
    }
  }

  /**
   * Given the most recent user input, returns true if it adheres
   * to board size guidelines.
   *
   * @param input the user input as a string
   * @return true if the input is in proper format
   */
  private boolean validBoard(String input) {
    // index the height and width at the locations specified in the directions
    String[] parts = input.split(" ");

    try {
      int h = Integer.parseInt(parts[0]);
      int w = Integer.parseInt(parts[1]);
      if (h < 16 && h > 5 && w < 16 && w > 5) {
        // return true if the input is integers of valid size
        return true;
      } else {
        // return false if the input is integers of wrong size
        return false;
      }
    } catch (Exception e) {
      // return false if the input can't be converted to an integer
      return false;
    }
  }

  /**
   * Determines if the user input for fleet sizes meets the specified criteria.
   *
   * @param input the string typed by the user, to be parsed into fleet sizes
   * @param sizeMax the maximum amount of ships on the board
   * @return true if the given fleet is valid for this game
   */
  private boolean validFleet(String input, int sizeMax) {
    String[] parts = input.split(" ");
    try {
      int carriers = Integer.parseInt(parts[0]);
      int battleships = Integer.parseInt(parts[1]);
      int destroyers = Integer.parseInt(parts[2]);
      int submarines = Integer.parseInt(parts[3]);

      if (carriers == 0 || battleships == 0 || destroyers == 0 || submarines == 0
          || (destroyers + carriers + submarines + battleships > sizeMax)) {
        return false;
      } else {
        return true;
      }
    } catch (Exception e) {
      return false;
    }
  }
}
