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

public class MineField implements IFieldObserver, Iterable<Field> {

    private int height;
    private int width;
    private int fieldsCount;
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

    public MineField() {
        flagsCount = 0;
    }

    /**
     * Creates a new instance of MineField
     */
    public MineField(GameType gameType) {
        super();

        height = gameType.getMineFieldHeight();
        width = gameType.getMineFieldWidth();
        fieldsCount = height * width;
        minesCount = gameType.getNumberOfMines();

        initializeMineField();
    }

    MineField(Field[][] fields, int minesCount) {
        super();

        this.fields = fields;
        height = fields.length;
        width = fields[0].length;
        fieldsCount = height * width;
        this.minesCount = minesCount;
    }

    /**
     * Procedura wywolywana jesli gra zakonczyla sie przegrana
     */
    private void gameOver() {
        // pokazujemy wszystkie miny
        showMines();
        observer.gameOver();
    }

    private int getDetonatedFieldsCount() {
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

    private void showMines() {
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            Field field = it.next();

            if (field.isDetonated()) {
                continue;
            }

            if (field.hasMine()) {
                field.setForeground(Color.red);

                if (field.hasFlag()) {
                    field.setIcon(GameIcon.FLAG.getIcon());
                } else {
                    field.setIcon(GameIcon.MINE.getIcon());
                }
            } else {
                if (field.hasFlag()) {
                    // nie trafilismy z flaga
                    field.setIcon(GameIcon.FLAG_WRONG.getIcon());
                }
            }
        }
    }

    /**
     * Sprawdza czy odkryto wszystkie niezaminowane pola
     * @return
     */
    public boolean allDetonated() {
        int detonatedFieldsCount = getDetonatedFieldsCount();
        return detonatedFieldsCount == (fieldsCount - minesCount);
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
     * Tworzy pole minowe
     * @param panelMineField
     */
    private void initializeMineField() {
        // tworzymy przeciski reprezentujace komorki pola
        fields = new Field[height][width];
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
            Field field = iterator().next();

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

}
