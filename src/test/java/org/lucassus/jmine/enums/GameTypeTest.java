package org.lucassus.jmine.enums;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test
public class GameTypeTest {

    /**
     * Test of getMineFieldWidth method, of class GameType.
     */
    @Test
    public void getMineFieldWidth() {
        assertEquals(9, GameType.NOVICE.getMineFieldWidth());
        assertEquals(16, GameType.INTERMEDIATE.getMineFieldWidth());
        assertEquals(30, GameType.EXPERT.getMineFieldWidth());
        assertEquals(9, GameType.USER.getMineFieldWidth());
    }

    /**
     * Test of getMineFieldHeight method, of class GameType.
     */
    @Test
    public void getMineFieldHeight() {
        assertEquals(9, GameType.NOVICE.getMineFieldHeight());
        assertEquals(16, GameType.INTERMEDIATE.getMineFieldHeight());
        assertEquals(16, GameType.EXPERT.getMineFieldHeight());
        assertEquals(9, GameType.USER.getMineFieldHeight());
    }

    /**
     * Test of getFieldsCount method, of class GameType.
     */
    @Test
    public void getFieldsCount() {
        assertEquals(9 * 9, GameType.NOVICE.getFieldsCount());
        assertEquals(16 * 16, GameType.INTERMEDIATE.getFieldsCount());
        assertEquals(30 * 16, GameType.EXPERT.getFieldsCount());
        assertEquals(9 * 9, GameType.USER.getFieldsCount());
    }

    /**
     * Test of getNumberOfMines method, of class GameType.
     */
    @Test
    public void getNumberOfMines() {
        assertEquals(10, GameType.NOVICE.getNumberOfMines());
        assertEquals(40, GameType.INTERMEDIATE.getNumberOfMines());
        assertEquals(99, GameType.EXPERT.getNumberOfMines());
        assertEquals(10, GameType.USER.getNumberOfMines());
    }
}
