package org.lucassus.jmine.field;

import java.util.Iterator;
import org.lucassus.jmine.enums.GameType;
import org.lucassus.jmine.enums.GameIcon;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lucassus.jmine.field.observers.IFieldObserver;
import org.lucassus.jmine.field.observers.IMineFieldObserver;

/**
 * Represents the Mine Field.
 */
public class MineField implements IFieldObserver, Iterable<Field> {

    private int height;
    private int width;
    /**
     * Number of flags on the Mine Field
     */
    private int flagsCount;
    /**
     * Number of Mines
     */
    private int minesCount;
    /**
     * Tablica przechowujaca komorki pola minowego
     */
    private Field[][] fields;
    private IMineFieldObserver observer;

    /**
     * Creates a new instance of MineField.
     */
    public MineField() {
        flagsCount = 0;
    }

    /**
     * Creates a new instance of MineField.
     * @param gameType
     */
    public MineField(GameType gameType) {
        super();

        this.height = gameType.getMineFieldHeight();
        this.width = gameType.getMineFieldWidth();
        this.minesCount = gameType.getNumberOfMines();

        this.fields = new Field[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Field field = new Field();
                field.attachObserver(this);
                fields[y][x] = field;
            }
        }

        setupNeighborFields();
        randomizeMines();
    }

    /**
     * Creates a new instance of MineField.
     * @param fields
     * @param minesCount
     */
    MineField(Field[][] fields, int minesCount) {
        super();

        this.height = fields.length;
        this.width = fields[0].length;
        this.minesCount = minesCount;

        this.fields = fields;
        
        setupNeighborFields();
    }

    /**
     * Procedura wywolywana jesli gra zakonczyla sie przegrana
     */
    private void gameOver() {
        // pokazujemy wszystkie miny
        showMines();
        observer.gameOver();
    }

    public int getDetonatedFieldsCount() {
        int detonatedFieldsCount = 0;

        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();

            if (field.isDetonated()) {
                detonatedFieldsCount++;
            }
        }

        return detonatedFieldsCount;
    }

    private void randomizeMines() {
        List<Field> fieldsWithMine = new ArrayList<Field>(minesCount);
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            fieldsWithMine.add(it.next());
        }

        Collections.shuffle(fieldsWithMine);
        fieldsWithMine = fieldsWithMine.subList(0, minesCount);
        for (Field field : fieldsWithMine) {
            field.setHasMine(true);
        }
    }

    /**
     * Set neighbour fields for each field.
     */
    private void setupNeighborFields() {
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();
            field.setNeighborFields(new ArrayList<Field>());
            for (Field neighbourField : getNeighbourFieldsFor(field)) {
                field.addNeighborField(neighbourField);
            }
        }
    }

    public void showMines() {
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();

            if (field.isDetonated()) {
                continue;
            }

            if (field.hasMineWithoutFlag()) {
                field.setForeground(Color.red);
                field.setIcon(GameIcon.MINE.getIcon());
                continue;
            }

            if (field.hasFlagWithoutMine()) {
                field.setIcon(GameIcon.FLAG_WRONG.getIcon());
                continue;
            }

            field.detonate();
        }
    }

    /**
     * Sprawdza czy odkryto wszystkie niezaminowane pola
     * @return
     */
    public boolean allDetonated() {
        int detonatedFieldsCount = getDetonatedFieldsCount();
        return detonatedFieldsCount == (getFieldsCount() - minesCount);
    }

    /**
     * Procedura wywolywana jesli gra zakonczyla sie zwycienstwem
     */
    private void gameWin() {
        // oflagujemy pozostawione nieoflagowane miny
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();

            // stawiamy flage
            if (!field.isDetonated() && !field.hasFlag()) {
                field.setIcon(GameIcon.FLAG.getIcon());
            }
        }

        observer.gameWin();
    }

    /**
     * Get collection of neighbour fields for given field.
     * @param field the field
     * @return collection of neighbour fields
     */
    public List<Field> getNeighbourFieldsFor(Field field) {
        List<Field> neighbours = new ArrayList<Field>();

        Coordinate coordinate = findCoordinateFor(field);
        int x = coordinate.getX();
        int y = coordinate.getY();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if ((x + i >= 0) && (y + j >= 0)
                        && (x + i < width)
                        && (y + j < height)) {
                    neighbours.add(fields[y + j][x + i]);
                }
            }
        }

        return neighbours;
    }

    public Coordinate findCoordinateFor(Field field) {
        Coordinate coordinate = null;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (fields[i][j] == field) {
                    coordinate = new Coordinate(j, i);
                    break;
                }
            }
        }

        return coordinate;
    }

    /**
     * Podpowiedz - rozminowuje losowo wybrane pole
     */
    public void hint() {
        List<Field> fieldsLeft = new ArrayList<Field>();
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();

            if (field.isDetonated() || field.hasFlag() || field.hasMine()) {
                continue;
            }

            fieldsLeft.add(field);
        }

        // losowanie pola do zdetonowania
        Collections.shuffle(fieldsLeft);
        Field field = fieldsLeft.get(0);
        field.detonate();
    }

    public void attachMineFieldObserver(IMineFieldObserver observer) {
        this.observer = observer;
    }

    @Override
    public void mineWasDetonated() {
        gameOver();
    }

    @Override
    public void fieldWasDetonated() {
        if (allDetonated()) {
            gameWin();
        }
    }

    @Override
    public void flagWasSet() {
        flagsCount++;
        observer.updateMinesLeftCount(getMinesLeftCount());
    }

    @Override
    public void flagWasRemoved() {
        flagsCount--;
        observer.updateMinesLeftCount(getMinesLeftCount());
    }

    private int getMinesLeftCount() {
        return minesCount - flagsCount;
    }

    public int getFlagsCount() {
        return flagsCount;
    }

    public int getMinesCount() {
        return minesCount;
    }

    @Override
    public Iterator<Field> iterator() {
        return new MineFieldIterator(fields);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getFieldsCount() {
        return width * height;
    }

}
