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
     * Rozmiary pola minowego definiowane przez uzytkownika
     */
    public static final int GAME_TYPE_USER = 0;
    /**
     * Poczatkujacy gracz
     */
    public static final int GAME_TYPE_NOVICE = 1;
    /**
     * Zaawansowany gracz
     */
    public static final int GAME_TYPE_INTERMEDIATE = 2;
    /**
     * Gracz ekspert
     */
    public static final int GAME_TYPE_EXPERT = 3;
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

    private abstract class GameType {

        /**
         * Szerokosc pola minowego
         */
        protected int mineFieldWidth = 9;
        /**
         * Wysokosc pola minowego
         */
        protected int mineFieldHeight = 9;
        /**
         * Liczba min znajdujacych sie na polu minowym
         */
        protected int numberOfMines = 10;
        protected int gameType;

        public int getMineFieldWidth() {
            return mineFieldWidth;
        }

        public int getMineFiledHeight() {
            return mineFieldHeight;
        }

        public int getNumberOfMines() {
            return numberOfMines;
        }
    }

    /**
     * Klasa reprezentujaca gre uzytkownika poczatkujacego
     */
    public class NoviceGame extends GameType {

        NoviceGame() {
            gameType = GAME_TYPE_NOVICE;
            mineFieldWidth = 9;
            mineFieldHeight = 9;
            numberOfMines = 10;
        }
    }

    /**
     * Klasa reprezentuja gre uzytkownika sredniozaawansowanego
     */
    public class IntermediateGame extends GameType {

        IntermediateGame() {
            gameType = GAME_TYPE_INTERMEDIATE;
            mineFieldWidth = 16;
            mineFieldHeight = 16;
            numberOfMines = 40;
        }
    }

    /**
     * Klasa reprezentujaca gre uzytkownika zaawansowanego
     */
    public class ExpertGame extends GameType {

        ExpertGame() {
            gameType = GAME_TYPE_EXPERT;
            mineFieldWidth = 30;
            mineFieldHeight = 16;
            numberOfMines = 99;
        }
    }

    /**
     * Klasa reprezentujaca gre definiowana przez uzytkownika
     */
    public class UserGame extends GameType {

        UserGame() {
            gameType = GAME_TYPE_USER;
        }

        public void setMineFieldWidth(int mineFieldWidth) {
            this.mineFieldWidth = mineFieldWidth;
        }

        public void setMineFieldHeight(int mineFieldHeight) {
            this.mineFieldHeight = mineFieldHeight;
        }

        public void setNumberOfMines(int numberOfMines) {
            this.numberOfMines = numberOfMines;
        }
    }
    /**
     * Rozmiar przycisku z mina (w pikselach)
     */
    private final int mineSize = 20;
    /**
     * Obiekt gry zdefiniowanej przez uzytkownika
     * @see UserGame
     */
    public UserGame userGame = new UserGame();
    /**
     * Obiekt gry uzytkownika poczatkujacego
     */
    public NoviceGame noviceGame = new NoviceGame();
    /**
     * Obiekt gry uzytkownika sredniozaawansowanego
     */
    public IntermediateGame intermediateGame = new IntermediateGame();
    /**
     * Obiekt gry uzytkownika eksperta
     */
    public ExpertGame expertGame = new ExpertGame();
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
        gameType = noviceGame;

        newGame();
    }

    /**
     * Funkcja zwraca dlugosc pola minowego
     * @return dlugosc pola minowego
     */
    public int getMineFieldWidth() {
        return gameType.mineFieldWidth;
    }

    /**
     * Funkcja zwraca wysokosc pola minowego
     * @return wysokosc pola minowego
     */
    public int getMineFielddHeight() {
        return gameType.mineFieldHeight;
    }

    /**
     * Zwraca liczbe min znajdujacych sie na polu minowym
     * @return liczba min znajdujacych sie na polu minowym
     */
    public int getNumberOfMines() {
        return gameType.numberOfMines;
    }

    /**
     * Rozpoczyna/resetuje gre
     */
    public void newGame() {
        isGameOver = false;
        detonatedFields = 0;
        flagsCount = 0;

        owner.getCounterField().setText("0");
        owner.getFlagsField().setText(Integer.toString(gameType.numberOfMines));

        owner.getNewGameButton().setIcon(GameIcon.FACE.getIcon());
        initializeMineField();
    }

    /**
     * Rozpoczyna nowa gre
     * @param gameType rodzaj gry (poziom trudnosci)
     */
    public void newGame(int gameType) {
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

            if (field.getHasMine()) {
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
    private void gameWin() {
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
        fields = new Field[gameType.mineFieldWidth][gameType.mineFieldHeight];
        for (int i = 0; i < gameType.mineFieldWidth; i++) {
            for (int j = 0; j < gameType.mineFieldHeight; j++) {
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
        for (int i = 0; i < gameType.numberOfMines; i++) {
            setMine();	// wstawiamy mine
        }

        owner.pack();
    }

    /**
     * Wstawia mine w losowo wybranym miejscu pola
     * @return zwraca false, jesli na wylosowanym polu znajduje sie juz mina
     * true jesli udalo sie wstawic mine
     */
    private boolean setMine() {
        // losujemy wspolrzedne
        int x = (int) Math.floor(Math.random() * gameType.mineFieldWidth);
        int y = (int) Math.floor(Math.random() * gameType.mineFieldHeight);
        if (!fields[x][y].getHasMine()) {
            fields[x][y].setHasMine(true);
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if ((x + i >= 0) && (y + j >= 0) && (x + i < gameType.mineFieldWidth) && (y + j < gameType.mineFieldHeight)) {
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
            // nacisnieto lewy przycisk myszy

            // jesli pole ma ustawiona flage
            if (field.getHasFlag()) {
                return;
            }

            if (field.getHasMine()) {
                // wdepnelismy na mine ;)
                field.setIsDetonated(true);
                field.setIcon(GameIcon.MINE_DETONATED.getIcon());
                gameOver();
            } else {
                detonateMine(field);
            }

            if (detonatedFields == (gameType.mineFieldHeight * gameType.mineFieldWidth - gameType.numberOfMines)) {
                // jesli odkryto wszystkie niezaminowane pola
                gameWin();
            }

        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            // nacisnieto prawy przycisk myszy
            // ustawienie/sciagniecie flagi z pola minowego

            // jesli pole zostalo juz zdetonowane
            if (field.isDetonated()) {
                return;
            }

            // jesli postawiono tyle flag ile jest min
            if (flagsCount == gameType.numberOfMines) {
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

        } else if (evt.getButton() == MouseEvent.BUTTON2) {
            // nacisnieto srodkowy przycisk myszy

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
                        if ((x + i >= 0) && (y + j >= 0) && (x + i < gameType.mineFieldWidth) && (y + j < gameType.mineFieldHeight)) {
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

                if (detonatedFields == (gameType.mineFieldHeight * gameType.mineFieldWidth - gameType.numberOfMines)) {
                    // jesli odkryto wszystkie niezaminowane pola
                    gameWin();
                }
            }
        }
    }

    /**
     * Funkcja detonuje sasiednie pola,
     * funkcja wywolywana jestli uzytwkonik kliknie srodkowym
     * przyciskiem myszy
     * @param field pole wokol, ktorego maja zostac wysadzone miny
     */
    private void detonateNeighbourMines(Field field) {
        int x = field.getPositionX();
        int y = field.getPositionY();

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }

                if ((x + i >= 0) && (y + j >= 0) && (x + i < gameType.mineFieldWidth) && (y + j < gameType.mineFieldHeight)) {
                    Field otherField = fields[x + i][y + j];

                    if (otherField.getHasFlag() && otherField.getHasMine()) {
                        // dobrze postawiona flaga
                        continue;
                    }
                    if (otherField.getHasFlag() && !otherField.getHasMine()) {
                        // zle postawiona flaga
                        otherField.setIsDetonated(true);
                        otherField.setIcon(GameIcon.FLAG_WRONG.getIcon());
                        // w nastepnej iteracji petla natrafi na mine :D
                        // i jebudu !!
                    } else if (!otherField.getHasFlag() && otherField.getHasMine()) {
                        // wdepnelismy na mine :/
                        otherField.setIsDetonated(true);
                        otherField.setIcon(GameIcon.MINE_DETONATED.getIcon());
                        gameOver();
                    } else {
                        detonateMine(otherField);
                    }
                }
            }
        }
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

        field.setIsDetonated(true);
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
            } else if (minesCount == 3) {
                color = new Color(255, 0, 0);
            } else if (minesCount == 4) {
                color = new Color(255, 0, 0);
            } else if (minesCount == 5) {
                color = new Color(255, 0, 0);
            } else if (minesCount == 6) {
                color = new Color(255, 0, 0);
            } else if (minesCount == 7) {
                color = new Color(255, 0, 0);
            } else if (minesCount == 8) {
                color = new Color(255, 0, 0);
            }

            field.setForeground(color);
            field.setText(Integer.toString(minesCount));
        } else {
            // brak min w poblizu
            field.setEnabled(false);
            // detonujemy sasiednie pola
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if ((x + i >= 0) && (y + j >= 0) && (x + i < gameType.mineFieldWidth) && (y + j < gameType.mineFieldHeight)) {
                        Field otherField = fields[x + i][y + j];
                        detonateMine(otherField);
                    }
                }
            }
        }

    }

    /**
     * Zwraca rodzaj gry
     * @return rodzaj gry (poziom trudnosci, gra uzytkownika)
     */
    public int getGameType() {
        return gameType.gameType;
    }

    /**
     * Ustawia rodzaj gry
     * @param rodzaj gry
     */
    public void setGameType(int gameType) {
        if (gameType == GAME_TYPE_NOVICE) {
            this.gameType = noviceGame;
        } else if (gameType == GAME_TYPE_INTERMEDIATE) {
            this.gameType = intermediateGame;
        } else if (gameType == GAME_TYPE_EXPERT) {
            this.gameType = expertGame;
        } else {
            this.gameType = userGame;
        }
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
            if (field.isDetonated()) {
                continue;
            }

            // jesli pole ma ustawiona flage
            if (field.getHasFlag()) {
                continue;
            }

            // jesli na polu znajduje sie mina
            if (field.getHasMine()) {
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
