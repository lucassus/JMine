package org.lucassus.jmine.field;

/**
 * Represents a Field coorditane on the Field.
 */
public class Coordinate {

  private int x;
  private int y;

  /**
   * Creates a nes Coordinate instance.
   * @param x
   * @param y
   */
  public Coordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns x position.
   * @return
   */
  public int getX() {
    return x;
  }

  /**
   * Sets the x position.
   * @param x
   */
  public void setX(final int x) {
    this.x = x;
  }

  /**
   * Returns y position.
   * @return
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the y position.
   * @param y
   */
  public void setY(final int y) {
    this.y = y;
  }
}
