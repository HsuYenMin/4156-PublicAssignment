package integration;

//import com.mashape.unirest.http.HttpResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import controllers.PlayGame;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import models.GameBoard;
import models.Message;
import models.Player;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


@TestMethodOrder(OrderAnnotation.class) 
public class GameTest {
  
  Message moveTest(int who, int x, int y) {
    HttpResponse<String> response = Unirest.post("http://localhost:8080/move/" + who)
        .body("x=" + x + "&y=" + y).asString();
    String responseBody = response.getBody();
    JSONObject jsonObject = new JSONObject(responseBody);
    Gson gson = new Gson();
    Message message = new Message(gson.fromJson(jsonObject.toString(), Message.class));
    return message;
  }
  
  GameBoard getGameBoard() {
    HttpResponse<String> response = Unirest.get("http://localhost:8080/getGameBoard").asString();
    String responseBody = response.getBody();
    JSONObject jsonObject = new JSONObject(responseBody);
    Gson gson = new Gson();
    GameBoard gameBoard = new GameBoard(gson.fromJson(jsonObject.toString(), GameBoard.class));
    return gameBoard;
  }
  
  
  /**
  * Runs only once before the testing starts.
  */
  @BeforeAll
  public static void init() {
    // Start Server
    PlayGame.main(null);
    System.out.println("Before All");
  }
  
  /**
  * This method starts a new game before every test run. It will run every time before a test.
  */
  @BeforeEach
  public void startNewGame() {
    // Test if server is running. You need to have an endpoint /
    // If you do not wish to have this end point, it is okay to not have anything in this method.
    // HttpResponse<String> response = Unirest.get("http://localhost:8080/").asString();
    // int restStatus = response.getStatus();

    System.out.println("Before Each");
  }
  
  /**
  * This is a test case to evaluate the newgame endpoint.
  */
  @Test
  @Order(1)
  public void newGameTest() {
    // Create HTTP request and get response
    HttpResponse<String> response = Unirest.get("http://localhost:8080/newgame").asString();
    int restStatus = response.getStatus();
     
    // Check assert statement (New Game has started)
    assertEquals(restStatus, 200);
    System.out.println("Test New Game");
  }
    
  /**
  * This is a test case to evaluate the startgame endpoint.
  */
  @Test
  @Order(2)
  public void startGameTest() {
    // Create a POST request to startgame endpoint and get the body
    // Remember to use asString() only once for an endpoint call. Every
    // time you call asString(), a new request will be sent to the endpoint.
    // Call it once and then use the data in the object.
    HttpResponse<String> response = Unirest.post("http://localhost:8080/startgame")
        .body("type=X").asString();
    String responseBody = response.getBody();
        
    // --------------------------- JSONObject Parsing ----------------------------------
        
    System.out.println("Start Game Response: " + responseBody);
        
    // Parse the response to JSON object
    JSONObject jsonObject = new JSONObject(responseBody);

    // Check if game started after player 1 joins: Game should not start at this point
    assertEquals(false, jsonObject.get("gameStarted"));
        
    // ---------------------------- GSON Parsing -------------------------
    // GSON use to parse data to object
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player1 = gameBoard.getP1();
    
    // Check if player type is correct
    assertEquals('X', player1.getType());
  }
  
  @Test
  @Order(3)
  public void joinGameTest() {
    // A player cannot make a move until both players have joined the game.
    Message message = moveTest(1, 0, 0);
    assertEquals(false, message.getValidity());
    System.out.println("Test Join Game");
    HttpResponse<String> response = Unirest.get("http://localhost:8080/joingame").asString();
    String responseBody = response.getBody();

    System.out.println("Join Game Response: " + responseBody);    
    // Parse the response to JSON object
    response = Unirest.get("http://localhost:8080/getGameBoard").asString();
    responseBody = response.getBody();
    JSONObject jsonObject = new JSONObject(responseBody);
    // Check if game started after player 2 joins: Game should start at this point
    assertEquals(true, jsonObject.get("gameStarted"));
    Gson gson = new Gson();
    GameBoard gameBoard = gson.fromJson(jsonObject.toString(), GameBoard.class);
    Player player2 = gameBoard.getP2();
    assertEquals('O', player2.getType());
  }
  
  @Test
  @Order(4)
  public void winGameTest() {
    // After game has started Player 1 always makes the first move.
    Message message = moveTest(2, 0, 0);
    assertEquals(false, message.getValidity());
    // A player cannot make two moves in their turn.
    message = moveTest(1, 0, 0);
    assertEquals(true, message.getValidity());
    message = moveTest(1, 0, 1);
    assertEquals(false, message.getValidity());
    // A player should be able to win a game.
    message = moveTest(2, 0, 1);
    assertEquals(true, message.getValidity());
    message = moveTest(1, 1, 0);
    assertEquals(true, message.getValidity());
    message = moveTest(2, 1, 1);
    assertEquals(true, message.getValidity());
    message = moveTest(1, 2, 0);
    assertEquals(true, message.getValidity());
    GameBoard gameboard = getGameBoard();
    assertEquals(1, gameboard.getWinner());
  }

  @Test
  @Order(5)
  public void drawGameTest() {
    startGameTest();
    joinGameTest();
    /* 
     * A game should be a draw if all the positions are exhausted and no one has won.
     */
    Message message = moveTest(1, 0, 0);
    assertEquals(true, message.getValidity());
    message = moveTest(2, 1, 1);
    assertEquals(true, message.getValidity());
    message = moveTest(1, 2, 2);
    assertEquals(true, message.getValidity());
    message = moveTest(2, 1, 2);
    assertEquals(true, message.getValidity());
    message = moveTest(1, 1, 0);
    assertEquals(true, message.getValidity());
    message = moveTest(2, 2, 0);
    assertEquals(true, message.getValidity());
    message = moveTest(1, 0, 2);
    assertEquals(true, message.getValidity());
    message = moveTest(2, 0, 1);
    assertEquals(true, message.getValidity());
    message = moveTest(1, 2, 1);
    assertEquals(true, message.getValidity());
    GameBoard gameboard = getGameBoard();
    assertEquals(true, gameboard.getIsDraw());
  }


  /**
  * This will run every time after a test has finished.
  */
  @AfterEach
  public void finishGame() {
    System.out.println("After Each");
  }

  /**
  * This method runs only once after all the test cases have been executed.
  */
  @AfterAll
  public static void close() {
    // Stop Server
    PlayGame.stop();
    System.out.println("After All");
  }
}

