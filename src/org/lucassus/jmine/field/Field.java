package org.lucassus.jmine.field;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JButton;
import org.lucassus.jmine.field.observers.FieldObserver;

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
    /**
     * Sąsiednie miny
     */
    private List<Field> neightborFields;
    private FieldObserver observer;
    private Coordinate coordinate;

    /** Creates a new instance of Mine */
    public Field() {
        super();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                mouseClick(evt);
            }
        });

        setText(null);
        setMargin(new java.awt.Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(mineSize, mineSize));

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

    public int getNeightborFlagsCount() {
        int count = 0;
        for (Field field : neightborFields) {
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

    /**
     * Rozminowuje komorke pola minowego
     */
    public void detonate() {

        // jesli pole zostalo juz zdetonowane
        if (isDetonated()) {
            return;
        }

        setDetonated(true);
        
        int minesCount = getNeightborMinesCount();
        if (minesCount > 0) {
            // w poblizu znajduja sie miny
            Color gray = new Color(238, 238, 238);
            setBackground(gray);

            // okreslenie koloru cyfry
            if (minesCount == 1) {
                setBlueForeground();
            } else if (minesCount == 2) {
                setGreenForeground();
            } else if (minesCount >= 3) {
                setRedForeground();
            }

            setText(Integer.toString(minesCount));
        } else {
            // brak min w poblizu
            setEnabled(false);

            // detonujemy sasiednie pola
            for (Field otherField : getNeightborFields()) {
                otherField.detonate();
            }
        }
    }

    /**
     * Obsluguje klikniecie na mine
     * @param evt
     */
    private void mouseClick(MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON1) {
            leftMouseButtonClick();
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            rightMouseButtonClick();
        } else if (evt.getButton() == MouseEvent.BUTTON2) {
            middleMouseButtonClick();
        }
    }

    /**
     * Funkcja detonuje sasiednie pola,
     * funkcja wywolywana jestli uzytwkonik kliknie srodkowym
     * przyciskiem myszy
     * @param field pole wokol, ktorego maja zostac wysadzone miny
     */
    private void detonateNeighbours() {
        for (Field otherField : neightborFields) {
            // dobrze postawiona flaga
            if (otherField.hasMineWithFlag()) {
                continue;
            }

            if (otherField.hasFlagWithoutMine()) {
                // zle postawiona flaga
                otherField.setDetonated(true);
                otherField.setIcon(GameIcon.FLAG_WRONG.getIcon());
                // w nastepnej iteracji petla natrafi na mine :D
                // i jebudu !!
            } else if (otherField.hasMineWithoutFlag()) {
                // wdepnelismy na mine :/
                otherField.setDetonated(true);
                otherField.setIcon(GameIcon.MINE_DETONATED.getIcon());
                observer.mineWasDetonated();
            } else {
                otherField.detonate();
            }
        }
    }

    private void leftMouseButtonClick() {
        if (hasFlag()) {
            return;
        }

        if (hasMine()) {
            setDetonated(true);
            setIcon(GameIcon.MINE_DETONATED.getIcon());
            observer.mineWasDetonated();
        } else {
            detonate();
            observer.fieldWasDetonated();
        }
    }

    private void rightMouseButtonClick() {
        // ustawienie/sciagniecie flagi z pola minowego
        // jesli pole zostalo juz zdetonowane
        if (isDetonated()) {
            return;
        }

        if (!hasFlag()) {
            setHasFlag(true);
            observer.flagWasSet();
        } else {
            setHasFlag(false);
            observer.flagWasRemoved();
        }
    }

    private void middleMouseButtonClick() {
        if (isDetonated() && getNeightborMinesCount() > 0) {

            // sprawdzamy czy liczba min w sasiedztwie zgadza sie
            // z liczba postawionych flag
            if (getNeightborMinesCount() == getNeightborFlagsCount()) {
                // detonujemy sasiednie pola
                detonateNeighbours();
                observer.fieldWasDetonated();
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

    public void attachObserver(FieldObserver observer) {
        this.observer = observer;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setRedForeground() {
        setForeground(new Color(255, 0, 0));
    }

    private void setBlueForeground() {
        setForeground(new Color(0, 0, 255));
    }

    private void setGreenForeground() {
        setForeground(new Color(0, 128, 0));
    }
}
