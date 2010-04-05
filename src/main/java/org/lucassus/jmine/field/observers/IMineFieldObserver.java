package org.lucassus.jmine.field.observers;

public interface IMineFieldObserver {

    public void updateMinesLeftCount(int minesLeft);

    public void gameWin();

    public void gameOver();

}
