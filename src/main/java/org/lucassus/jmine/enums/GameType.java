package org.lucassus.jmine.enums;

/**
 * Represents Game type configuration.
 */
public enum GameType {

    /**
     * Configuration for novice player.
     */
    NOVICE(9, 9, 10),
    /**
     * Configuration for intermediate player.
     */
    INTERMEDIATE(16, 16, 40),
    /**
     * Configuration for expert player.
     */
    EXPERT(30, 16, 99),
    /**
     * Custom configuration.
     */
    USER(9, 9, 10);
    /**
     * Width of the Mine Field.
     */
    private int mineFieldWidth;
    /**
     * Height of the Mine Field.
     */
    private int mineFieldHeight;
    /**
     * Number of mines on the Mine Field.
     */
    private int numberOfMines;

    /**
     * The Constructor.
     * @param mineFieldWidth width of the Mine Field
     * @param mineFieldHeight height of the Mine Field
     * @param numberOfMines number of mines on the Mine Field
     */
    GameType(final int mineFieldWidth, final int mineFieldHeight, final int numberOfMines) {
        this.mineFieldWidth = mineFieldWidth;
        this.mineFieldHeight = mineFieldHeight;
        this.numberOfMines = numberOfMines;
    }

    /**
     * Returns width of the Mine Field.
     * @return width of the Mine Field
     */
    public int getMineFieldWidth() {
        return mineFieldWidth;
    }

    /**
     * Return height of the Mine Field.
     * @return height of the Mine Field
     */
    public int getMineFieldHeight() {
        return mineFieldHeight;
    }

    /**
     * Returns number of mines on the Mine Field.
     * @return number of mines on the Mine Field
     */
    public int getNumberOfMines() {
        return numberOfMines;
    }

    /**
     * Sets the Mine Field widht.
     * @param mineFieldWidth width of the Mine Field
     * @throws CannotSetCustomGameException if Player can't set custom Game configuration
     */
    public void setMineFieldWidth(final int mineFieldWidth) throws CannotSetCustomGameException {
        canSetCustomGame();
        this.mineFieldWidth = mineFieldWidth;
    }

    /**
     * Sets the Mine Field height.
     * @param mineFieldHeight the Mine Field height.
     * @throws CannotSetCustomGameException if Player can't set custom Game configuration
     */
    public void setMineFieldHeight(final int mineFieldHeight) throws CannotSetCustomGameException {
        canSetCustomGame();
        this.mineFieldHeight = mineFieldHeight;
    }

    /**
     * Sets number of mines on the Mine Field.
     * @param numberOfMines number of mines on the Mine Field.
     * @throws CannotSetCustomGameException if Player can't set custom Game configuration
     */
    public void setNumberOfMines(final int numberOfMines) throws CannotSetCustomGameException {
        canSetCustomGame();
        this.numberOfMines = numberOfMines;
    }

    /**
     * Returns total total fields number on the Mine Field.
     * @return fields number
     */
    public int getFieldsCount() {
        return getMineFieldWidth() * getMineFieldHeight();
    }

    /**
     * Checks if Player can set custom configuration.
     * @throws CannotSetCustomGameException if Player can't set custom Game configuration
     */
    private void canSetCustomGame() throws CannotSetCustomGameException {
        if (this != USER) {
            throw new CannotSetCustomGameException();
        }
    }

}
