package org.lucassus.jmine.field;

import org.lucassus.jmine.JMineFrame;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 * Klasa reprezentujaca pole minowe
 * @author lucassus
 */
public class MineField {

    /**
     * true jesli gra sie zakonczyla
     */
    private boolean isGameOver;
    /**
     * Liczba postawionych flag
     */
    private int flagsCount;
    /**
     * Rodzaj gry
     * @see UserGame
     * @see NoviceGame
     * @see IntermediateGame
     * @see ExpertGame
     */
    private GameType gameType;
    private JMineFrame owner;
    /**
     * Tablica przechowujaca komorki pola minowego
     */
    private List<Field> fields;

    /** Creates a new instance of MineField
     * @param owner
     */
    public MineField(JMineFrame owner) {
        flagsCount = 0;
        this.owner = owner;
        gameType = GameType.NOVICE;

        newGame();
    }

    /**
     * Rozpoczyna/resetuje gre
     */
    public void newGame() {
        isGameOver = false;
        flagsCount = 0;

        owner.getCounterField().setText(null);
        owner.getFlagsField().setText(Integer.toString(gameType.getNumberOfMines()));
        owner.getNewGameButton().setIcon(GameIcon.FACE.getIcon());

        initializeMineField();
    }

    /**
     * Rozpoczyna nowa gre
     * @param gameType rodzaj gry (poziom trudnosci)
     */
    public void newGame(GameType gameType) {
        setGameType(gameType);
        newGame();
    }

    /**
     * Procedura wywolywana jesli gra zakonczyla sie przegrana
     */
    private void gameOver() {
        if (isGameOver) {
            return;
        }
        isGameOver = true;

        owner.getNewGameButton().setIcon(GameIcon.FACE_DEAD.getIcon());

        // pokazujemy wszystkie miny
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
    private boolean isGameWin() {
        int detonatedFields = 0;

        for (Field field : fields) {
            if (field.isDetonated()) {
                detonatedFields++;
            }
        }

        return detonatedFields == (gameType.getFieldsCount() - gameType.getNumberOfMines());
    }

    /**
     * Procedura wywolywana jesli gra zakonczyla sie zwycienstwem
     */
    private void winGame() {
        isGameOver = true;

        // oflagujemy pozostawione nieoflagowane miny
        for (Field field : fields) {
            // stawiamy flage
            if (!field.isDetonated() && !field.hasFlag()) {
                field.setIcon(GameIcon.FLAG.getIcon());
            }
        }

        owner.getFlagsField().setText("0");
        owner.getNewGameButton().setIcon(GameIcon.FACE_WIN.getIcon());
    }

    /**
     * Tworzy pole minowe
     */
    private void initializeMineField() {
        JPanel jPanelMineField = owner.getMineFieldPanel();
        jPanelMineField.removeAll();

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

                jPanelMineField.add(field, gridBagConstraints);
            }
        }

        for (Field field : fields) {
            field.setNeightborFields(getNeighbourFieldsFor(field));
        }

        // losujemy miny
        for (int i = 0; i < gameType.getNumberOfMines(); i++) {
            setMine();	// wstawiamy mine
        }

        owner.pack();
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

            if (isGameWin()) {
                winGame();
            }
        }
    }

    private void middleMouseButtonClick(Field field) {
        if (field.isDetonated() && field.getNeightborMinesCount() > 0) {

            // liczymy liczbe flag w sasiedztwie
            int neighbourFlagsCount = 0;
            for (Field otherField : field.getNeightborFields()) {
                if (otherField.hasFlag()) {
                    neighbourFlagsCount++;
                }
            }

            // sprawdzamy czy liczba min w sasiedztwie zgadza sie
            // z liczba postawionych flag
            if (field.getNeightborMinesCount() == neighbourFlagsCount) {
                // detonujemy sasiednie pola
                detonateNeighbours(field);

                if (isGameWin()) {
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
        owner.getFlagsField().setText(Integer.toString(minesLeft));
    }

    /**
     * Wstawia mine w losowo wybranym miejscu pola
     * @return zwraca false, jesli na wylosowanym polu znajduje sie juz mina
     * true jesli udalo sie wstawic mine
     */
    private boolean setMine() {
        // losujemy wspolrzedne
        int n = (int) Math.ceil(Math.random() * gameType.getFieldsCount() - 1);
        Field field = fields.get(n);

        if (!field.hasMine()) {
            field.setHasMine(true);
            return true;
        } else {
            // jesli na polu znajduje sie juz mina
            // rekurencyjnie wywolujemy funkcje
            setMine();
        }

        return false;
    }

    /**
     * Obsluguje klikniecie na mine
     * @param evt
     */
    private void mouseClick(MouseEvent evt) {
        if (isGameOver) {
            return;
        }

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
            if (otherField.hasFlag() && otherField.hasMine()) {
                continue;
            }

            if (otherField.hasFlag() && !otherField.hasMine()) {
                // zle postawiona flaga
                otherField.setDetonated(true);
                otherField.setIcon(GameIcon.FLAG_WRONG.getIcon());
                // w nastepnej iteracji petla natrafi na mine :D
                // i jebudu !!
            } else if (!otherField.hasFlag() && otherField.hasMine()) {
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
        if (isGameOver) {
            return;
        }

        List<Field> fieldsLeft = new ArrayList<Field>();
        for (Field field : fields) {

            // jesli pole zostalo juz zdetonowane
            // jesli pole ma ustawiona flage
            // jesli na polu znajduje sie mina
            if (field.isDetonated() || field.hasFlag() || field.hasMine()) {
                continue;
            }

            fieldsLeft.add(field);
        }

        // losowanie pola do zdetonowania
        int position = (int) Math.ceil(Math.random() * fieldsLeft.size());
        Field field = fieldsLeft.get(position);
        field.detonate();
    }
}
