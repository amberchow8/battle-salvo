package cs3500.pa04;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa03.model.Coord;
import cs3500.pa03.model.board.OpponentBoard;
import cs3500.pa03.model.player.FakePlayer;
import cs3500.pa03.model.player.UserPlayer;
import cs3500.pa03.model.player.UserPlayerShots;
import cs3500.pa03.model.ship.Ship;
import cs3500.pa03.model.ship.ShipType;
import cs3500.pa04.records.CoordJson;
import cs3500.pa04.records.EndGameJson;
import cs3500.pa04.records.FleetJson;
import cs3500.pa04.records.JoinJson;
import cs3500.pa04.records.MessageJson;
import cs3500.pa04.records.SetupRequestJson;
import cs3500.pa04.records.ShipJson;
import cs3500.pa04.records.VolleyJson;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a proxy that will talk to the server and
 * delegate methods to the aiPlayer.
 */
public class ProxyController {

  private final Socket server;
  private final InputStream in;
  private final PrintStream out;
  private FakePlayer player;
  private OpponentBoard board;
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Construct an instance of a ProxyPlayer.
   *
   * @param server the socket connection to the server
   * @param player the instance of the player
   * @throws IOException if
   */
  public ProxyController(Socket server, FakePlayer player) throws IOException {
    this.server = server;
    this.in = server.getInputStream();
    this.out = new PrintStream(server.getOutputStream());
    this.player = player;
  }

  /**
   * Listens for messages from the server as JSON in the format of a MessageJSON. When a complete
   * message is sent by the server, the message is parsed and then delegated to the corresponding
   * helper method for each message. This method stops when the connection to the server is closed
   * or an IOException is thrown from parsing malformed JSON.
   */
  public void run() {
    try {
      JsonParser parser = this.mapper.getFactory().createParser(this.in);

      while (!this.server.isClosed()) {
        MessageJson message = parser.readValueAs(MessageJson.class);
        delegateMessage(message);
      }
    } catch (IOException e) {
      System.out.println("Server is done reading and closed :)");
    }
  }

  /**
   * Determines the type of request the server has sent and delegates to the
   * corresponding helper method with the message arguments.
   *
   * @param message the MessageJSON used to determine what the server has sent
   */
  private void delegateMessage(MessageJson message) {
    String name = message.messageName();
    JsonNode arguments = message.arguments();

    if ("join".equals(name)) {
      handleJoin();
    } else if ("setup".equals(name)) {
      handleSetup(arguments);
    } else if ("take-shots".equals(name)) {
      handleTakeShots();
    } else if ("report-damage".equals(name)) {
      handleReportDamage(arguments);
    } else if ("successful-hits".equals(name)) {
      handleSuccessfulHits(arguments);
    } else if ("end-game".equals(name)) {
      handleEndGame(arguments);
    } else {
      throw new IllegalStateException("Invalid message name");
    }
  }

  /**
   * Creates a MessageJson to send back to server
   *
   * @param name name of message
   * @param args arguments of message
   * @return message to return to server
   */
  private JsonNode createMessageJson(String name, Record args) {
    MessageJson messageJson = new MessageJson(name,
        JsonUtils.serializeRecord(args));
    return JsonUtils.serializeRecord(messageJson);
  }

  /**
   * Handles controller when the server sends the "join" method by responding
   * with a MessageJson containing name and gametype.
   */
  private void handleJoin() {
    String name = "pa04-amber-lauren";
    String gameType = "SINGLE";
    JoinJson args = new JoinJson(name, gameType);
    JsonNode jsonResponse = createMessageJson("join", args);
    this.out.println(jsonResponse);
  }

  /**
   * Handles controller when the server sends the "setup" method
   * by responding with a MessageJson containing a fleet of ships
   *
   * @param arguments arguments
   */
  private void handleSetup(JsonNode arguments) {
    // parse arguments as a setupRequest and extract values as local variables
    SetupRequestJson setupRequest = this.mapper.convertValue(arguments,
        SetupRequestJson.class);

    int width = setupRequest.width();
    int height = setupRequest.height();

    Map<ShipType, Integer> shipMap = new LinkedHashMap<>();
    shipMap.put(ShipType.CARRIER, setupRequest.fleetSpecifications().carriers());
    shipMap.put(ShipType.BATTLESHIP, setupRequest.fleetSpecifications().battleships());
    shipMap.put(ShipType.DESTROYER, setupRequest.fleetSpecifications().destroyers());
    shipMap.put(ShipType.SUBMARINE, setupRequest.fleetSpecifications().submarines());

    // uses a dummy player to generate random ships
    UserPlayerShots userShots = new UserPlayerShots(new ArrayList<>());
    UserPlayer shipPlacer = new UserPlayer(new ArrayList<>(),
        new ArrayList<>(), userShots);

    // setup board and ships based on setupRequest
    List<Ship> playerShips = shipPlacer.setup(height, width, shipMap);
    this.board = new OpponentBoard(playerShips, height, width);
    this.player = new FakePlayer(new ArrayList<>(), playerShips, this.board);

    // adapt all ships to ShipJsons using a ShipAdaptor
    List<ShipJson> fleet = new ArrayList<>();
    for (Ship ship : playerShips) {
      ShipAdapter adapter = new ShipAdapter(ship);
      CoordJson c = new CoordJson(adapter.getStartingCoord().getX(),
          adapter.getStartingCoord().getY());
      ShipJson shipJson = new ShipJson(c, adapter.getLength(), adapter.getDirection());
      fleet.add(shipJson);
    }

    // convert to fleet and then back to a JsonNode to be printed to the output source
    FleetJson returnFleet = new FleetJson(fleet);
    JsonNode jsonResponse = createMessageJson("setup", returnFleet);
    this.out.println(jsonResponse);
  }


  /**
   * Asks the player to take shots for the current turn,
   * and then serializes the player's new guess to Json and sends the
   * response to the server.
   */
  private void handleTakeShots() {
    // take the player shots
    List<Coord> shots = player.takeShots();

    // convert each Coord to a CoordJson and append to shotList
    List<CoordJson> shotList = new ArrayList<>();
    for (Coord c : shots) {
      CoordJson coord = new CoordJson(c.getX(), c.getY());
      shotList.add(coord);
    }

    // create a volleyJson and serialize it
    VolleyJson takenShots = new VolleyJson(shotList);
    JsonNode jsonResponse = createMessageJson("take-shots", takenShots);

    // print the json node
    this.out.println(jsonResponse);
  }

  /**
   * Processes the damage returned by the server
   * and returns a MessageJson containing the opponent's
   * successful shots
   *
   * @param arguments arguments
   */
  private void handleReportDamage(JsonNode arguments) {
    // convert JsonNode args to VolleyJson
    VolleyJson shotsToProcess = this.mapper.convertValue(arguments, VolleyJson.class);

    // convert VolleyJson volley to List<Coord>
    List<Coord> convertedShotsToProcess = JsonUtils.coordJsonToCoord(shotsToProcess.volley());

    // get successful hits to this player
    List<Coord> hits = this.player.reportDamage(convertedShotsToProcess);

    // convert Coord hits to CoordJson
    List<CoordJson> hitsJson = new ArrayList<>();
    for (Coord c : hits) {
      CoordJson coord = new CoordJson(c.getX(), c.getY());
      hitsJson.add(coord);
    }

    // create response VolleyJson and response MessageJson
    VolleyJson returnHits = new VolleyJson(hitsJson);
    JsonNode jsonResponse = createMessageJson("report-damage", returnHits);
    this.out.println(jsonResponse);
  }

  /**
   * Handles successful-hits server request
   * and returns a MessageJson with empty args to the server
   *
   * @param arguments arguments
   */
  private void handleSuccessfulHits(JsonNode arguments) {
    VolleyJson successfulHitsJson = this.mapper.convertValue(arguments, VolleyJson.class);

    List<Coord> successfulHits = JsonUtils.coordJsonToCoord(successfulHitsJson.volley());
    this.player.successfulHits(successfulHits);

    MessageJson messageJson = new MessageJson(
        "successful-hits", mapper.createObjectNode());

    JsonNode jsonResponse = JsonUtils.serializeRecord(messageJson);
    this.out.println(jsonResponse);
  }

  /**
   * Handles end-game message from server
   * and returns a MessageJson with empty args to the server
   *
   * @param arguments arguments
   */
  private void handleEndGame(JsonNode arguments) {
    EndGameJson endGameJson = this.mapper.convertValue(arguments, EndGameJson.class);
    this.player.endGame(endGameJson.result(), endGameJson.reason());
    MessageJson messageJson = new MessageJson("end-game", mapper.createObjectNode());
    JsonNode jsonResponse = JsonUtils.serializeRecord(messageJson);
    this.out.println(jsonResponse);
    try {
      server.close();
    } catch (IOException ignored) {
    }
  }
}
