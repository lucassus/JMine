package org.lucassus.jmine.field;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lucassus.jmine.enums.CannotSetCustomGameException;
import org.lucassus.jmine.enums.GameType;
import org.lucassus.jmine.enums.GameIcon;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lucassus.jmine.field.observers.IFieldObserver;
import org.lucassus.jmine.field.observers.IMineFieldObserver;

public class MineField implements IFieldObserver, Iterable<Field> {

    /**
     * Liczba postawionych flag
     */
    private int flagsCount;
    /**
     * Rodzaj gry
     */
    // TODO remove this relation
    private GameType gameType;
    /**
     * Tablica przechowujaca komorki pola minowego
     */
    private Field[][] fields;
    private IMineFieldObserver observer;

    /**
     * Creates a new instance of MineField
     */
    public MineField() {
        gameType = GameType.NOVICE;
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
        List<Field> fieldsWithMine = new ArrayList<Field>(gameType.getNumberOfMines());
        Iterator<Field> it = iterator();
        while (it.hasNext()) {
            fieldsWithMine.add(it.next());
        }

        Collections.shuffle(fieldsWithMine);
        fieldsWithMine = fieldsWithMine.subList(0, gameType.getNumberOfMines());
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
        return detonatedFieldsCount == (gameType.getFieldsCount() - gameType.getNumberOfMines());
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

    public void initializeMineField(Field[][] fields, int numberOfMines) {
        flagsCount = 0;

        try {
            gameType = GameType.USER;
            gameType.setMineFieldHeight(fields.length);
            gameType.setMineFieldWidth(fields[0].length);
            gameType.setNumberOfMines(numberOfMines);
        } catch (CannotSetCustomGameException ex) {
            Logger.getLogger(MineField.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fields = fields;
        for (int y = 0; y < gameType.getMineFieldHeight(); y++) {
            for (int x = 0; x < gameType.getMineFieldWidth(); x++) {
                Coordinate coordinate = new Coordinate(x, y);

                Field field = fields[y][x];
                field.setCoordinate(coordinate);
                field.attachObserver(this);
            }
        }

        setupNeighborFields();
        randomizeMines();
    }

    /**
     * Tworzy pole minowe
     * @param panelMineField
     */
    public void initializeMineField() {
        flagsCount = 0;

        // tworzymy przeciski reprezentujace komorki pola
        fields = new Field[gameType.getMineFieldHeight()][gameType.getMineFieldWidth()];
        for (int y = 0; y < gameType.getMineFieldHeight(); y++) {
            for (int x = 0; x < gameType.getMineFieldWidth(); x++) {
                Coordinate coordinate = new Coordinate(x, y);

                Field field = new Field();
                field.setCoordinate(coordinate);
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

        int x = field.getCoordinate().getX();
        int y = field.getCoordinate().getY();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if ((x + i >= 0) && (y + j >= 0)
                        && (x + i < gameType.getMineFieldWidth())
                        && (y + j < gameType.getMineFieldHeight())) {
                    neighbours.add(fields[y + j][x + i]);
                }
            }
        }

        return neighbours;
    }

    /**
     * Zwraca rodzaj gry
     * @return rodzaj gry (poziom trudnosci, gra uzytkownika)
     */
    public GameType getGameType() {
        return gameType;
    }

    /**
     * Ustawia rodzaj gry
     * @param gameType rodzaj gry
     */
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
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
        return gameType.getNumberOfMines() - flagsCount;
    }

    public int getFlagsCount() {
        return flagsCount;
    }

    @Override
    public Iterator<Field> iterator() {
        return new MineFieldIterator(fields);
    }
    
}
