package org.lucassus.jmine.field;

import junit.framework.TestCase;

public class GameTypeTest extends TestCase {

    /**
     * Test of getMineFieldWidth method, of class GameType.
     */
    public void testGetMineFieldWidth() {
        assertEquals(9, GameType.NOVICE.getMineFieldWidth());
        assertEquals(16, GameType.INTERMEDIATE.getMineFieldWidth());
        assertEquals(30, GameType.EXPERT.getMineFieldWidth());
        assertEquals(9, GameType.USER.getMineFieldWidth());
    }

    /**
     * Test of getMineFieldHeight method, of class GameType.
     */
    public void testGetMineFieldHeight() {
        assertEquals(9, GameType.NOVICE.getMineFieldHeight());
        assertEquals(16, GameType.INTERMEDIATE.getMineFieldHeight());
        assertEquals(16, GameType.EXPERT.getMineFieldHeight());
        assertEquals(9, GameType.USER.getMineFieldHeight());
    }

    /**
     * Test of getFieldsCount method, of class GameType.
     */
    public void testGetFieldsCount() {
        assertEquals(9 * 9, GameType.NOVICE.getFieldsCount());
        assertEquals(16 * 16, GameType.INTERMEDIATE.getFieldsCount());
        assertEquals(30 * 16, GameType.EXPERT.getFieldsCount());
        assertEquals(9 * 9, GameType.USER.getFieldsCount());
    }

    /**
     * Test of getNumberOfMines method, of class GameType.
     */
    public void testGetNumberOfMines() {
        assertEquals(10, GameType.NOVICE.getNumberOfMines());
        assertEquals(40, GameType.INTERMEDIATE.getNumberOfMines());
        assertEquals(99, GameType.EXPERT.getNumberOfMines());
        assertEquals(10, GameType.USER.getNumberOfMines());
    }
}
