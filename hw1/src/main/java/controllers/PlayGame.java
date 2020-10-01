package controllers;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;  
import io.javalin.Javalin;
import java.io.IOException;
import java.util.Queue;
import models.GameBoard;
import models.Message;
import models.Move;
import models.Player;
import org.eclipse.jetty.websocket.api.Session;

public class PlayGame {

  private static final int PORT_NUMBER = 8080;

  private static Javalin app; 
  
  /** Main method of the application.
   * @param args Command line arguments
   */
  public static void main(final String[] args) {    
    app = Javalin.create(config -> {
      config.addStaticFiles("/public");
    }).start(PORT_NUMBER);

    // Test Echo Server
    // app.post("/echo", ctx -> {
    //   ctx.result(ctx.body());
    // });
    // New Game
    app.get("/newgame", ctx -> {
      ctx.redirect("/tictactoe.html");
    });


    GameBoard gameboard = new GameBoard();
    // Start Game
    app.post("/startgame", ctx -> {
      gameboard.reset();
      String body = ctx.body();
      char type1 = body.charAt(5);
      gameboard.setP1(type1, 1);
      Gson gson = new Gson();
      String jsonString = gson.toJson(gameboard);   
      ctx.result(jsonString);
    });
    // Player 2 joins
    app.get("/joingame", ctx -> {
      ctx.redirect("/tictactoe.html?p=2");
      gameboard.start();
      Gson gson = new Gson();
      String jsonString = gson.toJson(gameboard);
      sendGameBoardToAllPlayers(jsonString);
    });
    //  Player's movement 
    app.post("/move/:playerid", ctx -> {
      Move move;
      if (ctx.pathParam("playerid").equals("1")) {
        move = new Move(gameboard.getP1(), ctx.body());
      } else {
        move = new Move(gameboard.getP2(), ctx.body());
      }
      Message message = new Message(move, gameboard);
      if (message.getValidity()) {
        gameboard.updateBoardState(move);
      }
      Gson gson = new Gson();
      // send message to where the request come from
      ctx.result(gson.toJson(message));
      String jsonString = gson.toJson(gameboard);
      sendGameBoardToAllPlayers(jsonString);
    });
    // Get gameBoard
    app.get("/getGameBoard", ctx -> {
      Gson gson = new Gson();
      String jsonString = gson.toJson(gameboard);
      ctx.result(jsonString);
    });
    
    // Web sockets - DO NOT DELETE or CHANGE
    app.ws("/gameboard", new UiWebSocket());
  }

  /** Send message to all players.
   * @param gameBoardJson Gameboard JSON
   * @throws IOException Websocket message send IO Exception
   */
  private static void sendGameBoardToAllPlayers(final String gameBoardJson) {
    Queue<Session> sessions = UiWebSocket.getSessions();
    for (Session sessionPlayer : sessions) {
      try {
        sessionPlayer.getRemote().sendString(gameBoardJson);
      } catch (IOException e) {
        // Add logger here
      }
    }
  }

  public static void stop() {
    app.stop();
  }
}
