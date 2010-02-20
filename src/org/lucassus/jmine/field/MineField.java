package org.lucassus.jmine.field;

import org.lucassus.jmine.JMineFrame;
import java.awt.Color;
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
     * Liczba zdetonowanych pol pola minowego
     */
    private int detonatedFields;
    /**
     * Liczba postawionych flag
     */
    private int flagsCount;
    /**
     * Rozmiar przycisku z mina (w pikselach)
     */
    private final int mineSize = 20;
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
    private Field fields[][];

    /** Creates a new instance of MineField */
    public MineField(JMineFrame owner) {
        flagsCount = 0;
        detonatedFields = 0;
        this.owner = owner;
        gameType = GameType.NOVICE;

        newGame();
    }

    /**
     * Funkcja zwraca dlugosc pola minowego
     * @return dlugosc pola minowego
     */
    public int getMineFieldWidth() {
        return gameType.getMineFieldWidth();
    }

    /**
     * Funkcja zwraca wysokosc pola minowego
     * @return wysokosc pola minowego
     */
    public int getMineFielddHeight() {
        return gameType.getMineFiledHeight();
    }

    /**
     * Zwraca liczbe min znajdujacych sie na polu minowym
     * @return liczba min znajdujacych sie na polu minowym
     */
    public int getNumberOfMines() {
        return gameType.getNumberOfMines();
    }

    /**
     * Rozpoczyna/resetuje gre
     */
    public void newGame() {
        isGameOver = false;
        detonatedFields = 0;
        flagsCount = 0;

        owner.getCounterField().setText("");
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
        FieldsIterator iterator = new FieldsIterator(fields);
        while (iterator.hasNext()) {
            Field field = iterator.next();

            if (field.isDetonated()) {
                continue;
            }

            if (field.hasMine()) {
                field.setForeground(new java.awt.Color(255, 0, 0));
                if (field.getHasFlag()) {
                    field.setIcon(GameIcon.FLAG.getIcon());
                } else {
                    field.setIcon(GameIcon.MINE.getIcon());
                }
            } else {
                if (field.getHasFlag()) {
                    // nie trafilismy z flaga
                    field.setIcon(GameIcon.FLAG_WRONG.getIcon());
                }
            }
        }
    }

    /**
     * Procedura wywolywana jesli gra zakonczyla sie zwycienstwem
     */
    private void winGame() {
        isGameOver = true;

        // oflagujemy pozostawione nieoflagowane miny
        FieldsIterator iterator = new FieldsIterator(fields);
        while (iterator.hasNext()) {
            Field field = iterator.next();
            if (!field.isDetonated() && !field.getHasFlag()) {
                // stawiamy flage
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
        fields = new Field[getMineFieldWidth()][getMineFielddHeight()];
        for (int i = 0; i < getMineFieldWidth(); i++) {
            for (int j = 0; j < getMineFielddHeight(); j++) {
                Field field = new Field(i, j) {

                    {
                        setText("");
                        setMargin(new java.awt.Insets(0, 0, 0, 0));
                        setPreferredSize(new java.awt.Dimension(mineSize, mineSize));

                        addMouseListener(new MouseAdapter() {

                            @Override
                            public void mouseClicked(MouseEvent evt) {
                                click(evt);
                            }
                        });
                    }
                };
                fields[i][j] = field;

                GridBagConstraints gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = i;
                gridBagConstraints.gridy = j;

                jPanelMineField.add(field, gridBagConstraints);
            }
        }

        // losujemy miny
        for (int i = 0; i < getNumberOfMines(); i++) {
            setMine();	// wstawiamy mine
        }

        owner.pack();
    }

    private void leftMouseButtonClick(Field field) {
        // jesli pole ma ustawiona flage
        if (field.getHasFlag()) {
            return;
        }

        if (field.hasMine()) {
            // wdepnelismy na mine ;)
            field.setDetonated(true);
            field.setIcon(GameIcon.MINE_DETONATED.getIcon());
            gameOver();
        } else {
            detonateMine(field);
        }
        if (detonatedFields == (getMineFieldWidth() * getMineFielddHeight() - getNumberOfMines())) {
            // jesli odkryto wszystkie niezaminowane pola
            winGame();
        }
    }

    private void middleMouseButtonClick(Field field) {
        if (field.isDetonated()) {
            // jesli pole zostalo juz zdetonowane
            int x = field.getPositionX();
            int y = field.getPositionY();
            int mineFlagsCount = 0;
            // liczymy liczbe flag w sasiedztwie
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if ((x + i >= 0) && (y + j >= 0) && (x + i < getMineFieldWidth()) && (y + j < getMineFielddHeight())) {
                        if (fields[x + i][y + j].getHasFlag()) {
                            mineFlagsCount++;
                        }
                    }
                }
            }
            // sprawdzamy czy liczba min w sasiedztwie zgadza sie
            // z liczba postawionych flag
            if (field.getNeightborMinesCount() == mineFlagsCount) {
                // detonujemy sasiednie pola
                detonateNeighbourMines(field);
            }
            if (detonatedFields == (getMineFielddHeight() * getMineFieldWidth() - getNumberOfMines())) {
                // jesli odkryto wszystkie niezaminowane pola
                winGame();
            }
        }
    }

    private void rightMouseButtonClick(Field field) {
        // ustawienie/sciagniecie flagi z pola minowego
        // jesli pole zostalo juz zdetonowane
        if (field.isDetonated() || flagsCount == getNumberOfMines()) {
            return;
        }

        if (!field.getHasFlag()) {
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
        int x = (int) Math.floor(Math.random() * getMineFieldWidth());
        int y = (int) Math.floor(Math.random() * getMineFielddHeight());
        if (!fields[x][y].hasMine()) {
            fields[x][y].setHasMine(true);
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if ((x + i >= 0) && (y + j >= 0) && (x + i < getMineFieldWidth()) && (y + j < getMineFielddHeight())) {
                        fields[x + i][y + j].incrementNeightborMinesCount();
                    }
                }
            }

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
    private void click(MouseEvent evt) {
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
    private void detonateNeighbourMines(Field field) {
        for (Field otherField : getNeighbourFieldsFor(field)) {
            // dobrze postawiona flaga
            if (otherField.getHasFlag() && otherField.hasMine()) {
                continue;
            }

            if (otherField.getHasFlag() && !otherField.hasMine()) {
                // zle postawiona flaga
                otherField.setDetonated(true);
                otherField.setIcon(GameIcon.FLAG_WRONG.getIcon());
                // w nastepnej iteracji petla natrafi na mine :D
                // i jebudu !!
            } else if (!otherField.getHasFlag() && otherField.hasMine()) {
                // wdepnelismy na mine :/
                otherField.setDetonated(true);
                otherField.setIcon(GameIcon.MINE_DETONATED.getIcon());
                gameOver();
            } else {
                detonateMine(otherField);
            }
        }
    }

    private List<Field> getNeighbourFieldsFor(Field field) {
        List<Field> neighbours = new ArrayList<Field>();

        int x = field.getPositionX();
        int y = field.getPositionY();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if ((x + i >= 0) && (y + j >= 0) && (x + i < getMineFieldWidth()) && (y + j < getMineFielddHeight())) {
                    Field otherField = fields[x + i][y + j];
                    neighbours.add(otherField);
                }
            }
        }

        return neighbours;
    }

    /**
     * Rozminowuje komorke pola minowego
     * @param komorka pola minowego do wysadzenia ;)
     */
    private void detonateMine(Field field) {

        // jesli pole zostalo juz zdetonowane
        if (field.isDetonated()) {
            return;
        }

        field.setDetonated(true);
        detonatedFields++;
        int minesCount = field.getNeightborMinesCount();
        int x = field.getPositionX();
        int y = field.getPositionY();

        if (minesCount != 0) {
            // w poblizu znajduja sie miny
            field.setBackground(new java.awt.Color(238, 238, 238));

            Color color = new Color(0, 0, 0);
            // okreslenie koloru cyfry
            if (minesCount == 1) {
                color = new Color(0, 0, 255);
            } else if (minesCount == 2) {
                color = new Color(0, 128, 0);
            } else if (minesCount >= 3) {
                color = new Color(255, 0, 0);
            }

            field.setForeground(color);
            field.setText(Integer.toString(minesCount));
        } else {
            // brak min w poblizu
            field.setEnabled(false);
            // detonujemy sasiednie pola

            for (Field otherField : getNeighbourFieldsFor(field)) {
                detonateMine(otherField);
            }
        }

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
     * @param rodzaj gry
     */
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    /**
     * Podpowiedz - rozminowuje losowo wybrane pole, kara za
     * podpowiedz jest dodanie 10 sekund do stopera
     */
    public void hint() {

        // jesli gra zostala zakonczona
        if (isGameOver) {
            return;
        }

        List<Field> fieldsLeft = new ArrayList<Field>();
        FieldsIterator iterator = new FieldsIterator(fields);
        while (iterator.hasNext()) {
            Field field = iterator.next();

            // jesli pole zostalo juz zdetonowane
            // jesli pole ma ustawiona flage
            // jesli na polu znajduje sie mina
            if (field.isDetonated() || field.getHasFlag() || field.hasMine()) {
                continue;
            }

            fieldsLeft.add(field);
        }

        // losowanie pola do zdetonowania
        int position = (int) Math.ceil(Math.random() * fieldsLeft.size());
        detonateMine(fieldsLeft.get(position));

        // TODO dodanie kary
    }
}
