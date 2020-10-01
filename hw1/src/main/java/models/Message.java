package models;

public class Message {
  
  private boolean moveValidity;

  private int code;

  private String message;

  /**
   * Copy constructor.
   */
  public Message(Message message) {
    this.moveValidity = message.moveValidity;
    this.code = message.code;
    this.message = message.message;
  }

  /**
   * check validity.
   */
  public Message(Move move, GameBoard gameboard) {
    if (gameboard.getGameStarted()) {
      if (move.getPlayer().getId() == gameboard.getTurn()) {
        if (gameboard.getBoardState()[move.getX()][move.getY()] == 0) {
          this.moveValidity = true;
          this.code = 100;
          this.message = "valid";
        } else {
          this.moveValidity = false;
          this.code = 202;
          this.message = "wrong place";
        }
      } else {
        this.moveValidity = false;
        this.code = 201;
        this.message = "wrong player";
      }
    } else {
      this.moveValidity = false;
      this.code = 200;
      this.message = "not started yet";
    }
  }

  /**
   * Know whether the move is legal.
   */  
  public boolean getValidity() {
    // System.out.println(moveValidity);
    return moveValidity;
  }
  
  public int getCode() {
    return code;
  }
}
