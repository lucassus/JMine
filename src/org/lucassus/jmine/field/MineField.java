package org.lucassus.jmine.field;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lucassus.jmine.field.observers.IFieldObserver;
import org.lucassus.jmine.field.observers.IMineFieldObserver;

public class MineField implements IFieldObserver {

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
    private List<Field> fields;
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
        for (Field field : fields) {
            if (field.isDetonated()) {
                detonatedFieldsCount++;
            }
        }

        return detonatedFieldsCount;
    }

    private void randomizeMines() {
        List<Field> fieldsWithMine = new ArrayList<Field>(fields);
        Collections.shuffle(fieldsWithMine);
        fieldsWithMine = fieldsWithMine.subList(0, gameType.getNumberOfMines());
        for (Field field : fieldsWithMine) {
            field.setHasMine(true);
        }
    }

    private void showMines() {
        for (Field field : fields) {
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
        for (Field field : fields) {
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
    public void initializeMineField() {
        flagsCount = 0;

        // tworzymy przeciski reprezentujace komorki pola
        fields = new ArrayList<Field>(gameType.getFieldsCount());
        for (int y = 0; y < gameType.getMineFieldHeight(); y++) {
            for (int x = 0; x < gameType.getMineFieldWidth(); x++) {
                Coordinate coordinate = new Coordinate(x, y);

                Field field = new Field();
                field.setCoordinate(coordinate);
                field.attachObserver(this);
                fields.add(field);
            }
        }

        // set neighbour fields for each field
        for (Field field : fields) {
            for (Field neighbourField : getNeighbourFieldsFor(field)) {
                field.addNeighborField(neighbourField);
            }
        }

        randomizeMines();
    }

    public List<Field> getFields() {
        return fields;
    }

    private List<Field> getNeighbourFieldsFor(Field field) {
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
                    Field otherField = getFieldByCoordinate(new Coordinate(x + i, y + j));
                    neighbours.add(otherField);
                }
            }
        }

        return neighbours;
    }

    public Field getFieldByCoordinate(Coordinate coordinate) {
        int x = coordinate.getX();
        int y = coordinate.getY();

        int position = y * gameType.getMineFieldHeight() + x;
        Field field = fields.get(position);

        return field;
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
        for (Field field : fields) {

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
}
