package org.lucassus.jmine.field;

import org.lucassus.jmine.JMineFrame;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
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

  private class Counter extends TimerTask {

    private Timer timer;
    private boolean isWorking;

    public Counter() {
      timer = new Timer();
      timer.schedule(this, 0, 1000);
    }

    public void run() {
      int count = Integer.parseInt(owner.getCounterField().getText());
      count++;
      owner.getCounterField().setText(String.valueOf(count));
    }
  }

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

    owner.getNewGameButton().setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/face.gif")));
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

    owner.getNewGameButton().setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/face_dead.gif")));

    // pokazujemy wszystkie miny
    for (int i = 0; i < gameType.mineFieldWidth; i++) {
      for (int j = 0; j < gameType.mineFieldHeight; j++) {
        if (fields[i][j].getIsDetonated()) {
          continue;
        }
        if (fields[i][j].getHasMine()) {
          fields[i][j].setForeground(new java.awt.Color(255, 0, 0));
          if (fields[i][j].getHasFlag()) {
            fields[i][j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/flag.gif")));
          } else {
            fields[i][j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/mine.gif")));
          }
        } else {
          if (fields[i][j].getHasFlag()) {
            // nie trafilismy z flaga
            fields[i][j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/flag_wrong.gif")));
          }
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
    for (int i = 0; i < gameType.mineFieldWidth; i++) {
      for (int j = 0; j < gameType.mineFieldHeight; j++) {
        if (!fields[i][j].getIsDetonated() && !fields[i][j].getHasFlag()) {
          // stawiamy flage
          fields[i][j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/flag.gif")));
        }
      }
    }

    owner.getFlagsField().setText("0");
    owner.getNewGameButton().setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/face_win.gif")));
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
        fields[i][j] = new Field(i, j);

        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = i;
        gridBagConstraints.gridy = j;
        fields[i][j].setText("");
        fields[i][j].setMargin(new java.awt.Insets(0, 0, 0, 0));
        fields[i][j].setPreferredSize(new java.awt.Dimension(mineSize, mineSize));

        fields[i][j].addMouseListener(new MouseAdapter() {

          @Override
          public void mouseClicked(java.awt.event.MouseEvent evt) {
            click(evt);
          }
        });

        jPanelMineField.add(fields[i][j], gridBagConstraints);
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
    if (isGameOver) {	// gra skonczona
      return;
    } else {

      Field field = (Field) evt.getSource();		// pole na ktore kliknieto
      if (evt.getButton() == evt.BUTTON1) {
        // nacisnieto lewy przycisk myszy

        if (field.getHasFlag()) {	// jesli pole ma ustawiona flage
          return;
        }

        if (field.getHasMine()) {
          // wdepnelismy na mine ;)
          field.setIsDetonated(true);
          field.setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/mine_detonate.gif")));
          gameOver();
        } else {
          detonateMine(field);
        }

        if (detonatedFields == (gameType.mineFieldHeight * gameType.mineFieldWidth - gameType.numberOfMines)) {
          // jesli odkryto wszystkie niezaminowane pola
          gameWin();
        }

      } else if (evt.getButton() == evt.BUTTON3) {
        // nacisnieto prawy przycisk myszy
        // ustawienie/sciagniecie flagi z pola minowego

        if (field.getIsDetonated()) {
          // jesli pole zostalo juz zdetonowane
          return;
        }

        if (flagsCount == gameType.numberOfMines) {
          // jesli postawiono tyle flag ile jest min
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

      } else if (evt.getButton() == evt.BUTTON2) {
        // nacisnieto srodkowy przycisk myszy

        if (field.getIsDetonated()) {
          // jesli pole zostalo juz zdetonowane

          int x = field.getPositionX();
          int y = field.getPositionY();
          int flagsCount = 0;

          // liczymy liczbe flag w sasiedztwie
          for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
              if (i == 0 && j == 0) {
                continue;
              }
              if ((x + i >= 0) && (y + j >= 0) && (x + i < gameType.mineFieldWidth) && (y + j < gameType.mineFieldHeight)) {
                if (fields[x + i][y + j].getHasFlag()) {
                  flagsCount++;
                }
              }
            }
          }

          // sprawdzamy czy liczba min w sasiedztwie zgadza sie
          // z liczba postawionych flag
          if (field.getNeightborMinesCount() == flagsCount) {
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
          if (fields[x + i][y + j].getHasFlag() && fields[x + i][y + j].getHasMine()) {
            // dobrze postawiona flaga
            continue;
          }
          if (fields[x + i][y + j].getHasFlag() && !fields[x + i][y + j].getHasMine()) {
            // zle postawiona flaga
            fields[x + i][y + j].setIsDetonated(true);
            fields[x + i][y + j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/flag_wrong.gif")));
            // w nastepnej iteracji petla natrafi na mine :D
            // i jebudu !!
          } else if (!fields[x + i][y + j].getHasFlag() && fields[x + i][y + j].getHasMine()) {
            // wdepnelismy na mine :/
            fields[x + i][y + j].setIsDetonated(true);
            fields[x + i][y + j].setIcon(new javax.swing.ImageIcon(getClass().getResource("/JMine/resources/mine_detonate.gif")));
            gameOver();
          } else {
            detonateMine(x + i, y + j);
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
    if (field.getIsDetonated()) {
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
            detonateMine(x + i, y + j);
          }
        }
      }
    }

  }

  /**
   * Detonuje pole o podanych wspolrzednych
   * @param posX pozycja X pola
   * @param posY pozycja Y pola
   */
  private void detonateMine(int posX, int posY) {
    detonateMine(fields[posX][posY]);
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

    Vector fieldsLeft = new Vector();
    for (int i = 0; i < gameType.mineFieldWidth; i++) {
      for (int j = 0; j < gameType.mineFieldHeight; j++) {
        if (fields[i][j].getIsDetonated()) {
          continue;	// jesli pole zostalo juz zdetonowane
        }
        if (fields[i][j].getHasFlag()) {
          continue;		// jesli pole ma ustawiona flage
        }
        if (fields[i][j].getHasMine()) {
          continue;		// jesli na polu znajduje sie mina
        }
        fieldsLeft.add(fields[i][j]);
      }
    }
    int n = fieldsLeft.size();
    // losowanie pola do zdetonowania
    int random = (int) Math.ceil(Math.random() * n);
    detonateMine((Field) fieldsLeft.elementAt(random));

    // dodanie kary
    // todo

  }
}
