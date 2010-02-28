package org.lucassus.jmine.field;

import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JPanel;

/**
 * Klasa reprezentujaca pole minowe
 * @author lucassus
 */
public class MineField {

    /**
     * Liczba postawionych flag
     */
    private int flagsCount;
    /**
     * Rodzaj gry
     */
    private GameType gameType;
    /**
     * Tablica przechowujaca komorki pola minowego
     */
    private List<Field> fields;
    private MineFieldObserver observer;

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
                field.setForeground(new java.awt.Color(255, 0, 0));
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
    private boolean allDetonated() {
        int detonatedFieldsCount = getDetonatedFieldsCount();
        return detonatedFieldsCount == (gameType.getFieldsCount() - gameType.getNumberOfMines());
    }

    /**
     * Procedura wywolywana jesli gra zakonczyla sie zwycienstwem
     */
    private void winGame() {
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
    public void initializeMineField(JPanel panelMineField) {
        flagsCount = 0;
        panelMineField.removeAll();

        // tworzymy przeciski reprezentujace komorki pola
        fields = new ArrayList<Field>(gameType.getFieldsCount());
        for (int i = 0; i < gameType.getMineFieldHeight(); i++) {
            for (int j = 0; j < gameType.getMineFieldWidth(); j++) {
                Field field = new Field();
                field.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        mouseClick(evt);
                    }
                });

                fields.add(field);

                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = i;
                gridBagConstraints.gridy = j;

                panelMineField.add(field, gridBagConstraints);
            }
        }

        // set neighbour fields for each field
        for (Field field : fields) {
            List<Field> neighbourFields = getNeighbourFieldsFor(field);
            field.setNeightborFields(neighbourFields);
        }

        randomizeMines();
    }

    private void leftMouseButtonClick(Field field) {
        if (field.hasFlag()) {
            return;
        }

        if (field.hasMine()) {
            field.setDetonated(true);
            field.setIcon(GameIcon.MINE_DETONATED.getIcon());
            gameOver();
        } else {
            field.detonate();

            if (allDetonated()) {
                winGame();
            }
        }
    }

    private void middleMouseButtonClick(Field field) {
        if (field.isDetonated() && field.getNeightborMinesCount() > 0) {

            // sprawdzamy czy liczba min w sasiedztwie zgadza sie
            // z liczba postawionych flag
            if (field.getNeightborMinesCount() == field.getNeightborFlagsCount()) {
                // detonujemy sasiednie pola
                detonateNeighbours(field);

                if (allDetonated()) {
                    winGame();
                }
            }
        }
    }

    private void rightMouseButtonClick(Field field) {
        // ustawienie/sciagniecie flagi z pola minowego
        // jesli pole zostalo juz zdetonowane
        if (field.isDetonated() || gameType.getNumberOfMines() == flagsCount) {
            return;
        }

        if (!field.hasFlag()) {
            field.setHasFlag(true);
            flagsCount++;
        } else {
            field.setHasFlag(false);
            flagsCount--;
        }

        int minesLeft = gameType.getNumberOfMines() - flagsCount;
        observer.updateMinesLeftCount(minesLeft);
    }

    /**
     * Obsluguje klikniecie na mine
     * @param evt
     */
    private void mouseClick(MouseEvent evt) {
        // pole na ktore kliknieto
        Field field = (Field) evt.getSource();

        if (evt.getButton() == MouseEvent.BUTTON1) {
            leftMouseButtonClick(field);
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            rightMouseButtonClick(field);
        } else if (evt.getButton() == MouseEvent.BUTTON2) {
            middleMouseButtonClick(field);
        }
    }

    /**
     * Funkcja detonuje sasiednie pola,
     * funkcja wywolywana jestli uzytwkonik kliknie srodkowym
     * przyciskiem myszy
     * @param field pole wokol, ktorego maja zostac wysadzone miny
     */
    private void detonateNeighbours(Field field) {
        for (Field otherField : field.getNeightborFields()) {
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
                gameOver();
            } else {
                otherField.detonate();
            }
        }
    }

    private List<Field> getNeighbourFieldsFor(Field field) {
        List<Field> neighbours = new ArrayList<Field>();

        int position = fields.indexOf(field);
        int x = position % gameType.getMineFieldWidth();
        int y = position / gameType.getMineFieldHeight();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if ((x + i >= 0) && (y + j >= 0) && (x + i < gameType.getMineFieldWidth()) && (y + j < gameType.getMineFieldHeight())) {
                    int pos = (y + j) * gameType.getMineFieldHeight() + (x + i);
                    Field otherField = fields.get(pos);
                    neighbours.add(otherField);
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

    public void attachMineFieldObserver(MineFieldObserver observer) {
        this.observer = observer;
    }
}
