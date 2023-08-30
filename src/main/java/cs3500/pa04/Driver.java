package cs3500.pa04;

import cs3500.pa03.controller.BattleSalvoController;
import cs3500.pa03.model.Model;
import cs3500.pa03.model.board.OpponentBoard;
import cs3500.pa03.model.player.FakePlayer;
import cs3500.pa03.view.View;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * This method connects to the server at the given host and port, builds a proxy referee
   * to handle communication with the server, and sets up a client player.
   *
   * @param host the server host
   * @param port the server port
   * @throws IOException if there is a communication issue with the server
   */
  private static void runClient(String host, int port)
      throws IOException, IllegalStateException {
    Socket server = new Socket(host, port);
    OpponentBoard board = new OpponentBoard(new ArrayList<>(), 6, 6);
    FakePlayer player = new FakePlayer(new ArrayList<>(), new ArrayList<>(), board);
    ProxyController controller = new ProxyController(server, player);
    controller.run();
  }

  /**
   * Project entry point
   *
   * @param args - no command line args required
   */
  public static void main(String[] args) {
    try {
      if (args.length == 2) {
        runClient(args[0], Integer.parseInt(args[1]));
      } else if (args.length == 0) {
        Model model = new Model();
        View view = new View(new InputStreamReader(System.in),
            new PrintStream(System.out));
        BattleSalvoController controller = new BattleSalvoController(model, view);
        controller.runGame();
      }
    } catch (IOException | IllegalStateException e) {
      System.out.println("Unable to connect to the server.");
    } catch (NumberFormatException e) {
      System.out.println("Second argument should be an integer. Format: `[host] [port]`.");
    }
  }
}