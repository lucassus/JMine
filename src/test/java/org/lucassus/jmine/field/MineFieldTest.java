package org.lucassus.jmine.field;

import java.awt.Color;
import java.util.List;
import org.lucassus.jmine.enums.GameIcon;
import org.lucassus.jmine.enums.GameType;
import org.lucassus.jmine.field.observers.IMineFieldObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@Test
public class MineFieldTest {
    private static final int MINES_COUNT = 1;

    private Field[][] fields;
    @Mock
    private Field field00;
    @Mock
    private Field field01, field02, field10, field11, field12, field20, field21, field22;
    private int minesCount;

    private MineField instance;
    @Mock
    private IMineFieldObserver observerMock;

    @BeforeMethod
    protected void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        minesCount = MINES_COUNT;
        fields = new Field[][] {
            {field00, field01, field02},
            {field10, field11, field12},
            {field20, field21, field22}
        };

        instance = new MineField(fields, minesCount);
        instance.attachMineFieldObserver(observerMock);
    }

    @Test
    public void constructor() {
        GameType expertGame = GameType.EXPERT;
        MineField mineField = new MineField(expertGame);
        
        assertEquals(expertGame.getMineFieldHeight(), mineField.getHeight());
        assertEquals(expertGame.getMineFieldWidth(), mineField.getWidth());
        assertEquals(expertGame.getFieldsCount(), mineField.getFieldsCount());
        assertEquals(expertGame.getNumberOfMines(), mineField.getMinesCount());
        assertEquals(0, mineField.getFlagsCount());
    }

    /**
     * Test of mineWasDetonated method, of class MineField.
     */
    @Test
    public void mineWasDetonated() {
        instance.mineWasDetonated();
        verify(observerMock).gameOver();
    }

    /**
     * Test of fieldWasDetonated method, of class MineField.
     */
    @Test
    public void fieldWasDetonated() {
        MineField instanceUnderSpy = spy(instance);
        doReturn(true).when(instanceUnderSpy).allDetonated();

        instanceUnderSpy.fieldWasDetonated();
        verify(observerMock).gameWin();
    }

    /**
     * Test of flagWasSet method, of class MineField.
     */
    @Test
    public void flagWasSet() {
        assertEquals(0, instance.getFlagsCount());
        instance.flagWasSet();
        assertEquals(1, instance.getFlagsCount());
        verify(observerMock).updateMinesLeftCount(instance.getMinesCount() - 1);
    }

    /**
     * Test of flagWasRemoved method, of class MineField.
     */
    @Test
    public void flagWasRemoved() {
        assertEquals(0, instance.getFlagsCount());
        instance.flagWasRemoved();
        assertEquals(-1, instance.getFlagsCount());
        verify(observerMock).updateMinesLeftCount(instance.getMinesCount() + 1);
    }

    @Test
    public void findCoordinateFor() {
        Coordinate coordinate = null;

        coordinate = instance.findCoordinateFor(field00);
        assertEquals(0, coordinate.getX());
        assertEquals(0, coordinate.getY());

        coordinate = instance.findCoordinateFor(field12);
        assertEquals(2, coordinate.getX());
        assertEquals(1, coordinate.getY());

        coordinate = instance.findCoordinateFor(field21);
        assertEquals(1, coordinate.getX());
        assertEquals(2, coordinate.getY());
    }

    @Test
    public void getNeighbourFieldsFor() {
        List<Field> neighbourFields = null;

        neighbourFields = instance.getNeighbourFieldsFor(field00);
        assertEquals(3, neighbourFields.size());
        assertTrue(neighbourFields.contains(field01));
        assertTrue(neighbourFields.contains(field10));
        assertTrue(neighbourFields.contains(field11));

        neighbourFields = instance.getNeighbourFieldsFor(field10);
        assertEquals(5, neighbourFields.size());
        assertTrue(neighbourFields.contains(field00));
        assertTrue(neighbourFields.contains(field01));
        assertTrue(neighbourFields.contains(field11));
        assertTrue(neighbourFields.contains(field20));
        assertTrue(neighbourFields.contains(field21));

        neighbourFields = instance.getNeighbourFieldsFor(field11);
        assertEquals(8, neighbourFields.size());
        assertTrue(neighbourFields.contains(field00));
        assertTrue(neighbourFields.contains(field01));
        assertTrue(neighbourFields.contains(field02));
        assertTrue(neighbourFields.contains(field10));
        assertTrue(neighbourFields.contains(field12));
        assertTrue(neighbourFields.contains(field20));
        assertTrue(neighbourFields.contains(field21));
        assertTrue(neighbourFields.contains(field22));

        neighbourFields = instance.getNeighbourFieldsFor(field22);
        assertEquals(3, neighbourFields.size());
        assertTrue(neighbourFields.contains(field11));
        assertTrue(neighbourFields.contains(field12));
        assertTrue(neighbourFields.contains(field21));
    }

    @Test
    public void getDetonatedFieldsCount() {
        when(field00.isDetonated()).thenReturn(true);
        when(field01.isDetonated()).thenReturn(true);

        assertEquals(2, instance.getDetonatedFieldsCount());
    }

    @Test
    public void hint() {
        when(field00.hasMine()).thenReturn(true);
        when(field01.isDetonated()).thenReturn(true);
        when(field02.hasFlag()).thenReturn(true);
        when(field10.hasFlag()).thenReturn(true);
        when(field11.hasFlag()).thenReturn(true);
        when(field12.hasFlag()).thenReturn(true);
        when(field20.hasFlag()).thenReturn(true);
        when(field21.hasFlag()).thenReturn(true);
        when(field21.hasFlag()).thenReturn(true);

        instance.hint();
        verify(field22).detonate();
    }

    @Test
    public void showMines() {
        when(field00.hasMineWithoutFlag()).thenReturn(true);
        when(field01.isDetonated()).thenReturn(true);
        when(field02.hasFlagWithoutMine()).thenReturn(true);

        instance.showMines();

        verify(field00).setForeground(Color.red);
        verify(field00).setIcon(GameIcon.MINE.getIcon());
        verify(field00, never()).detonate();
        verify(field02).setIcon(GameIcon.FLAG_WRONG.getIcon());
        verify(field02, never()).detonate();
        verify(field22).detonate();
    }

    @Test
    public void allDetonated() {
        assertFalse(instance.allDetonated());
    }
}
