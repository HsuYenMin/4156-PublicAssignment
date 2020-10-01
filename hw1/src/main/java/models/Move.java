package models;

public class Move {
  
  private Player player;

  private int moveX;

  private int moveY;

  /** 
   * Move initialization.
   */
  public Move(Player player, String moveString) {
    this.player = player;
    this.moveX = moveString.charAt(2) - '0';
    this.moveY = moveString.charAt(6) - '0';
  }
  
  public int getX() {
    return moveX;
  }
  
  public int getY() {
    return moveY;
  }
  
  public Player getPlayer() {
    return player;
  }
  
}
