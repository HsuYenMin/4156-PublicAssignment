package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import models.GameBoard;
import models.Message;
import models.Move;
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

// @TestMethodOrder(OrderAnnotation.class)
public class UnitTest {
  GameBoard gameBoard = new GameBoard();
  
  @Test
  public void testSetP1() {
    gameBoard.setP1('O', 1);
    Player player = gameBoard.getP1();
    assertEquals('O', player.getType());
  }

  @Test
  public void testCheckWinner() {
    gameBoard.reset();
    gameBoard.setP1('O', 1);
    char[][] boardState = new char[3][3];
    boardState[0][0] = 'O';
    boardState[0][1] = 'O';
    boardState[0][2] = 'O';
    gameBoard.setBoardState(boardState);
    gameBoard.checkWinner('O');
    gameBoard.reset();
    gameBoard.setP1('O', 1);
    boardState = new char[3][3];
    boardState[0][0] = 'X';
    boardState[1][1] = 'X';
    boardState[2][2] = 'X';
    gameBoard.setBoardState(boardState);
    gameBoard.checkWinner('X');
    gameBoard.reset();
    gameBoard.setP1('O', 1);
    boardState = new char[3][3];
    boardState[0][2] = 'O';
    boardState[1][1] = 'O';
    boardState[2][0] = 'O';
    gameBoard.setBoardState(boardState);
    gameBoard.checkWinner('O');
  }
  
  @Test
  public void testMessage() {
    gameBoard.reset();
    gameBoard.setP1('X', 1);
    gameBoard.start();
    char[][] boardState = new char[3][3];
    boardState[0][0] = 'O';
    boardState[0][1] = 'O';
    boardState[0][2] = 'O';
    gameBoard.setBoardState(boardState);
    Move move = new Move(gameBoard.getP1(), "x=0&y=1");
    Message message = new Message(move, gameBoard);
    assertEquals(202, message.getCode());
  }
}
