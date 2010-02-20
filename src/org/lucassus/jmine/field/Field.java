package org.lucassus.jmine.field;

import javax.swing.JButton;

/**
 * Pojedyncza komorka pola minowego
 * @author lucassus
 */
public class Field extends JButton {

    /**
     * Okresla czy na danym polu znajduje sie mina
     */
    private boolean hasMine;
    /**
     * Okresla czy pole zostalo oznaczone
     */
    private boolean hasFlag;
    /**
     * Okresla czy pole zostalo zdetonowane
     */
    private boolean isDetonated;
    /**
     * Pozycja X miny na polu minowym
     */
    private int positionX;
    /**
     * Pozycja Y miny na polu minowym
     */
    private int positionY;
    /**
     * Ilosc min w sasiedztwie pola
     */
    private int neightborMinesCount;

    /** Creates a new instance of Mine */
    public Field(int positionX, int positionY) {
        super();

        this.positionX = positionX;
        this.positionY = positionY;

        isDetonated = false;
        hasFlag = false;
        neightborMinesCount = 0;
    }

    /**
     * Inkrementuja liczbe min znajdujacych sie w sasiedztwie pola,
     * procedura wywolywana podczas tworzenia nowego pola minowego
     */
    public void incrementNeightborMinesCount() {
        neightborMinesCount++;
    }

    /**
     * Zwraca liczbe min znajdujacych sie w sasiedztwie pola
     * @return liczba min znajdujacych sie w sasiedztwie
     */
    public int getNeightborMinesCount() {
        return neightborMinesCount;
    }

    /**
     * Funkcja zwraca true jesli na danym polu znajduje sie mina
     * @return boolean
     */
    public boolean hasMine() {
        return hasMine;
    }

    /**
     * Uzbraja/rozbraja komorke pola minowego
     * @param true jesli na polu ma znajdowac sie mina.
     * false w przeciwnym przypadku
     */
    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
    }

    /**
     * Zwraca pozycje X miny na polu minowym
     * @return int
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Zwraca pozycje Y minu na polu minowym
     * @return int
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Zwraca true jesli pole jest zdetonowane,
     * false w przeciwnym przypadku
     * @return boolean
     */
    public boolean isDetonated() {
        return isDetonated;
    }

    /**
     * Okresla czy pole zostalo zdetonowane
     * @param isDetonated true - jesli pole zostalo zdetonowane
     * false - w przeciwnym przypadku
     */
    public void setDetonated(boolean isDetonated) {
        this.isDetonated = isDetonated;
    }

    /**
     * Zwraca true, jesli pole zostalo oznaczone
     * @return boolean
     */
    public boolean getHasFlag() {
        return hasFlag;
    }

    /**
     * Oznacza pole
     * @param hasFlag true - jesli pole zostalo oznaczone
     * false - w przeciwnym przypadku
     */
    public void setHasFlag(boolean hasFlag) {

        // jesli pole zostalo zdetonowane
        if (isDetonated) {
            return;
        }

        this.hasFlag = hasFlag;
        if (hasFlag) {
            setIcon(GameIcon.FLAG.getIcon());
        } else {
            setIcon(null);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Mine position x: ").append(positionX).append(", position y: ").append(positionY);
        return sb.toString();
    }
}
