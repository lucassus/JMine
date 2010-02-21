package org.lucassus.jmine.field;

import java.util.List;
import javax.swing.JButton;

/**
 * Pojedyncza komorka pola minowego
 * @author lucassus
 */
public class Field extends JButton {

    /**
     * Rozmiar przycisku z mina (w pikselach)
     */
    private final int mineSize = 20;
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
    private List<Field> neightborFields;

    /** Creates a new instance of Mine */
    public Field() {
        super();

        setText("");
        setMargin(new java.awt.Insets(0, 0, 0, 0));
        setPreferredSize(new java.awt.Dimension(mineSize, mineSize));

        isDetonated = false;
        hasFlag = false;
    }

    /**
     * Zwraca liczbe min znajdujacych sie w sasiedztwie pola
     * @return liczba min znajdujacych sie w sasiedztwie
     */
    public int getNeightborMinesCount() {
        int count = 0;
        for (Field field : neightborFields) {
            if (field.hasMine) {
                count++;
            }
        }

        return count;
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
     * @param  hasMine true jesli na polu ma znajdowac sie mina.
     * false w przeciwnym przypadku
     */
    public void setHasMine(boolean hasMine) {
        this.hasMine = hasMine;
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
    public boolean hasFlag() {
        return hasFlag;
    }

    /**
     * Oznacza pole
     * @param hasFlag true - jesli pole zostalo oznaczone
     * false - w przeciwnym przypadku
     */
    public void setHasFlag(boolean hasFlag) {
        this.hasFlag = hasFlag;

        if (hasFlag) {
            setIcon(GameIcon.FLAG.getIcon());
        } else {
            setIcon(null);
        }
    }

    /**
     * @return the neightborMines
     */
    public List<Field> getNeightborFields() {
        return neightborFields;
    }

    /**
     * @param neightborFields the neightborMines to set
     */
    public void setNeightborFields(List<Field> neightborFields) {
        this.neightborFields = neightborFields;
    }
}
