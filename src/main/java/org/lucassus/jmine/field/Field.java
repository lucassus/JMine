package org.lucassus.jmine.field;

import org.lucassus.jmine.field.listeners.FieldMouseListener;
import org.lucassus.jmine.enums.GameIcon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import org.lucassus.jmine.field.observers.IFieldObserver;

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
    /**
     * Sąsiednie miny
     */
    private List<Field> neighborFields;
    private IFieldObserver observer;

    /** Creates a new instance of Mine */
    public Field() {
        super();
        setText(null);
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(mineSize, mineSize));
        addMouseListener(new FieldMouseListener(this));

        neighborFields = new ArrayList<Field>();
        isDetonated = false;
        hasFlag = false;
    }

    /**
     * Zwraca liczbe min znajdujacych sie w sasiedztwie pola
     * @return liczba min znajdujacych sie w sasiedztwie
     */
    public int getNeighborMinesCount() {
        int count = 0;
        for (Field field : neighborFields) {
            if (field.hasMine()) {
                count++;
            }
        }

        return count;
    }

    public int getNeighborFlagsCount() {
        int count = 0;
        for (Field field : neighborFields) {
            if (field.hasFlag()) {
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
     * @return the neighborMines
     */
    public List<Field> getNeighborFields() {
        return neighborFields;
    }

    public boolean addNeighborField(Field neighbourField) {
        return neighborFields.add(neighbourField);
    }

    public void setNeighborFields(List<Field> neighborFields) {
        this.neighborFields = neighborFields;
    }

    /**
     * Rozminowuje komorke pola minowego
     */
    public void detonate() {

        // jesli pole zostalo juz zdetonowane
        if (isDetonated()) {
            return;
        }

        setDetonated(true);

        if (getNeighborMinesCount() > 0) {
            showNeighbourMinesCount();
        } else {
            setEnabled(false);

            // detonujemy sasiednie pola
            for (Field otherField : getNeighborFields()) {
                otherField.detonate();
            }
        }
    }

    private void showNeighbourMinesCount() {
        Color gray = new Color(238, 238, 238);
        setBackground(gray);

        // okreslenie koloru cyfry
        int minesCount = getNeighborMinesCount();
        setForeground(Color.blue.darker());
        if (minesCount == 1) {
            setForeground(Color.blue);
        } else if (minesCount == 2) {
            setForeground(Color.green.darker());
        } else if (minesCount == 3) {
            setForeground(Color.red.darker());
        } else if (minesCount == 4) {
            setForeground(Color.blue.darker());
        } else if (minesCount == 5) {
            setForeground(Color.orange.darker());
        } else if (minesCount == 6) {
            setForeground(Color.green.darker().darker());
        } else if (minesCount == 7) {
            setForeground(Color.red.darker().darker());
        } else if (minesCount == 8) {
            setForeground(Color.cyan.darker());
        }
        
        setText(Integer.toString(minesCount));
    }

    /**
     * Funkcja detonuje sasiednie pola,
     * funkcja wywolywana jestli uzytwkonik kliknie srodkowym
     * przyciskiem myszy
     * @param field pole wokol, ktorego maja zostac wysadzone miny
     */
    public void detonateNeighbourFields() {
        for (Field neighborField : neighborFields) {
            // dobrze postawiona flaga
            if (neighborField.hasMineWithFlag()) {
                continue;
            }

            if (neighborField.hasFlagWithoutMine()) {
                // zle postawiona flaga
                neighborField.setDetonated(true);
                neighborField.setIcon(GameIcon.FLAG_WRONG.getIcon());
                // w nastepnej iteracji petla natrafi na mine :D
                // i jebudu !!
            } else if (neighborField.hasMineWithoutFlag()) {
                // wdepnelismy na mine :/
                neighborField.setDetonated(true);
                neighborField.setIcon(GameIcon.MINE_DETONATED.getIcon());
                observer.mineWasDetonated();
            } else {
                neighborField.detonate();
            }
        }
    }

    public boolean hasMineWithFlag() {
        return hasMine() && hasFlag();
    }

    public boolean hasFlagWithoutMine() {
        return !hasMine() && hasFlag();
    }

    public boolean hasMineWithoutFlag() {
        return hasMine() && !hasFlag();
    }

    public void attachObserver(IFieldObserver observer) {
        this.observer = observer;
    }


    public IFieldObserver getObserver() {
        return observer;
    }
}
