package org.lucassus.jmine.field.observers;

public interface MineFieldObserver {

    public void updateMinesLeftCount(int minesLeft);

    public void gameWin();

    public void gameOver();

}
