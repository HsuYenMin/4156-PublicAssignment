package models;

public class GameBoard {

  private Player p1;

  private Player p2;

  private boolean gameStarted;

  private int turn;

  private char[][] boardState;

  private int winner;

  private boolean isDraw;

  /**
   * GameBoard initialization.
   */
  public GameBoard() {
    this.gameStarted = false;
    this.turn = 1;
    this.boardState = new char[3][3];
    this.p1 = new Player();
    this.p2 = new Player();
    this.winner = 0;
    this.isDraw = false;
  }

  /**
   * GameBoard initialization.
   */
  public GameBoard(GameBoard gameBoard) {
    this.gameStarted = gameBoard.gameStarted;
    this.turn = gameBoard.turn;
    this.boardState = new char[3][3];
    int i;
    int j;
    for (i = 0; i < 3; i++) {
      for (j = 0; j < 3; j++) {
        this.boardState[i][j] = gameBoard.boardState[i][j];
      }
    }
    this.p1 = new Player(gameBoard.p1);
    this.p2 = new Player(gameBoard.p2);
    this.winner = gameBoard.winner;
    this.isDraw = gameBoard.isDraw;
  }

  /** 
   * Reset the gameboard.
   */
  public void reset() {
    this.gameStarted = false;
    this.turn = 1;
    this.boardState = new char[3][3];
    this.p1 = new Player();
    this.p2 = new Player();
    this.winner = 0;
    this.isDraw = false;
  }

  /** 
   * Set the boardState.
   */  
  public void setBoardState(char[][] boardState) {
    int i;
    int j;
    for (i = 0; i < 3; i++) {
      for (j = 0; j < 3; j++) {
        this.boardState[i][j] = boardState[i][j];
      }
    }
  }
  
  /**
   * set Players.
   */
  public void setP1(char type1, int id) {
    this.p1.set(type1, id);
    if (type1 == 'X') {
      this.p2.set('O', 2);
    } else if (type1 == 'O') {
      this.p2.set('X', 2);
    }
  }
  
  /** 
   * Turn on the start flag.
   */
  public void start() {
    this.gameStarted = true;
  }
  
  public Player getP1() {
    return p1;
  }

  public Player getP2() {
    return p2;
  }
  
  public boolean getGameStarted() {
    return gameStarted;
  }

  /** 
   * Get the Tic Tac Toe Game.
   */
  public char[][] getBoardState() {
    char[][] boardState = new char[3][3];
    int i;
    int j;
    for (i = 0; i < 3; i++) {
      for (j = 0; j < 3; j++) {
        boardState[i][j] = this.boardState[i][j];
      }
    }
    return boardState;
  }

  
  public int getTurn() {
    return turn;
  }
  
  public int getWinner() {
    return winner;
  }
 
  public boolean getIsDraw() {
    return isDraw;
  }
  
  /**
   * Find out whether there is a winner.
   */
  public void checkWinner(char type) {
    
    int i;
    int j;
    boolean somebodywins;
    somebodywins = false;
    
    for (i = 0; i < 3; i++) {
      if (boardState[i][0] == type && boardState[i][1] == type && boardState[i][2] == type) {
        somebodywins = true;
        break;
      }
    }
    for (i = 0; i < 3; i++) {
      if (boardState[0][i] == type && boardState[1][i] == type && boardState[2][i] == type) {
        somebodywins = true;
        break;
      }
    }
    if (boardState[0][0] == type && boardState[1][1] == type && boardState[2][2] == type) {
      somebodywins = true;
    }
    if (boardState[0][2] == type && boardState[1][1] == type && boardState[2][0] == type) {
      somebodywins = true;
    }
    
    if (somebodywins) {
      if (p1.getType() == type) {
        winner = 1;
      } else {
        winner = 2;
      }
    } else {
      boolean allfull = true;
      for (i = 0; i < 3; i++) {
        for (j = 0; j < 3; j++) {
          if (boardState[i][j] == 0) {
            allfull = false;
          }
        }
      }
      if (allfull) {
        isDraw = true;
      }
    }
  }

  /**
   * Update the boardState and change the turn.
   */
  public void updateBoardState(Move move) {
    boardState[move.getX()][move.getY()] = move.getPlayer().getType();
    checkWinner(move.getPlayer().getType());
    if (winner == 0 && isDraw == false) {
      if (turn == 1) {
        turn = 2;
      } else {
        turn = 1;
      }
    }
  }

}
