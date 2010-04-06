package org.lucassus.jmine.field;

import org.lucassus.jmine.enums.GameType;
import java.util.List;
import junit.framework.TestCase;
import org.lucassus.jmine.field.observers.IMineFieldObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class MineFieldTest extends TestCase {

    private MineField instance;
    @Mock
    private IMineFieldObserver observerMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        instance = new MineField();
        instance.setGameType(GameType.NOVICE);
        instance.initializeMineField();
    }

    /**
     * Test of getGameType method, of class MineField.
     */
    public void testGetGameType() {
        GameType result = instance.getGameType();
        assertEquals(GameType.NOVICE, result);
    }

    /**
     * Test of mineWasDetonated method, of class MineField.
     */
    public void testMineWasDetonated() {
        instance.attachMineFieldObserver(observerMock);

        instance.mineWasDetonated();
        verify(observerMock).gameOver();
    }

    /**
     * Test of fieldWasDetonated method, of class MineField.
     */
    public void testFieldWasDetonated() {
        instance.attachMineFieldObserver(observerMock);

        MineField instance = spy(this.instance);
        when(instance.allDetonated()).thenReturn(true);

        instance.fieldWasDetonated();
        verify(observerMock).gameWin();
    }

    /**
     * Test of flagWasSet method, of class MineField.
     */
    public void testFlagWasSet() {
        instance.attachMineFieldObserver(observerMock);

        assertEquals(0, instance.getFlagsCount());
        instance.flagWasSet();
        assertEquals(1, instance.getFlagsCount());
        verify(observerMock).updateMinesLeftCount(instance.getGameType().getNumberOfMines() - 1);
    }

    /**
     * Test of flagWasRemoved method, of class MineField.
     */
    public void testFlagWasRemoved() {
        instance.attachMineFieldObserver(observerMock);

        assertEquals(0, instance.getFlagsCount());
        instance.flagWasRemoved();
        assertEquals(-1, instance.getFlagsCount());
        verify(observerMock).updateMinesLeftCount(instance.getGameType().getNumberOfMines() + 1);
    }
}
