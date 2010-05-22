package org.lucassus.jmine.field.observers;

/**
 * Mine Field Observer interface.
 */
public interface IMineFieldObserver {

    /**
     * Updates number of left mines.
     * @param minesLeft
     */
    public void updateMinesLeftCount(int minesLeft);

    /**
     * Indicates game win.
     */
    public void gameWin();

    /**
     * Indicates game over.
     */
    public void gameOver();

}
