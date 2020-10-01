package models;

public class Player {

  private char type;

  private int id;

  public Player() {
    this.type = 'X';
    this.id = 1;
  }
  
  public Player(Player player) {
    this.type = player.type;
    this.id   = player.id;
  }
  
  public void set(char type1, int id) {
    this.type = type1;
    this.id   = id;
  }
  
  public int getId() {
    return id;
  }
  
  public char getType() {
    return type;
  }
}
