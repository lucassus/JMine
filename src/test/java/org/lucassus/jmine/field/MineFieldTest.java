package org.lucassus.jmine.field;

import java.util.List;
import junit.framework.TestCase;
import org.lucassus.jmine.field.observers.IMineFieldObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class MineFieldTest extends TestCase {

    private Field[][] fields;
    @Mock
    private Field fieldWithMine;
    @Mock
    private Field field2, field3, field4, field5, field6, field7, field8, field9;
    private int minesCount;

    private MineField instance;
    @Mock
    private IMineFieldObserver observerMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        minesCount = 1;
        fields = new Field[][] {
            {fieldWithMine, field2, field3},
            {field4, field5, field6},
            {field7, field8, field9}
        };

        instance = new MineField(fields, minesCount);
        instance.attachMineFieldObserver(observerMock);
    }

    /**
     * Test of mineWasDetonated method, of class MineField.
     */
    public void testMineWasDetonated() {
        instance.mineWasDetonated();
        verify(observerMock).gameOver();
    }

    /**
     * Test of fieldWasDetonated method, of class MineField.
     */
    public void testFieldWasDetonated() {
        MineField instanceUnderSpy = spy(instance);
        doReturn(true).when(instanceUnderSpy).allDetonated();

        instanceUnderSpy.fieldWasDetonated();
        verify(observerMock).gameWin();
    }

    /**
     * Test of flagWasSet method, of class MineField.
     */
    public void testFlagWasSet() {
        assertEquals(0, instance.getFlagsCount());
        instance.flagWasSet();
        assertEquals(1, instance.getFlagsCount());
        verify(observerMock).updateMinesLeftCount(instance.getMinesCount() - 1);
    }

    /**
     * Test of flagWasRemoved method, of class MineField.
     */
    public void testFlagWasRemoved() {
        assertEquals(0, instance.getFlagsCount());
        instance.flagWasRemoved();
        assertEquals(-1, instance.getFlagsCount());
        verify(observerMock).updateMinesLeftCount(instance.getMinesCount() + 1);
    }

    public void testFindCoordinateFor() {
        Coordinate coordinate = null;

        coordinate = instance.findCoordinateFor(fieldWithMine);
        assertEquals(0, coordinate.getX());
        assertEquals(0, coordinate.getY());

        coordinate = instance.findCoordinateFor(field6);
        assertEquals(2, coordinate.getX());
        assertEquals(1, coordinate.getY());

        coordinate = instance.findCoordinateFor(field8);
        assertEquals(1, coordinate.getX());
        assertEquals(2, coordinate.getY());
    }

    public void testGetNeighbourFieldsFor() {
        List<Field> neighbourFields = null;

        neighbourFields = instance.getNeighbourFieldsFor(fieldWithMine);
        assertEquals(3, neighbourFields.size());
        assertTrue(neighbourFields.contains(field2));
        assertTrue(neighbourFields.contains(field4));
        assertTrue(neighbourFields.contains(field5));

        neighbourFields = instance.getNeighbourFieldsFor(field4);
        assertEquals(5, neighbourFields.size());
        assertTrue(neighbourFields.contains(fieldWithMine));
        assertTrue(neighbourFields.contains(field2));
        assertTrue(neighbourFields.contains(field5));
        assertTrue(neighbourFields.contains(field7));
        assertTrue(neighbourFields.contains(field8));

        neighbourFields = instance.getNeighbourFieldsFor(field5);
        assertEquals(8, neighbourFields.size());
        assertTrue(neighbourFields.contains(fieldWithMine));
        assertTrue(neighbourFields.contains(field2));
        assertTrue(neighbourFields.contains(field3));
        assertTrue(neighbourFields.contains(field4));
        assertTrue(neighbourFields.contains(field6));
        assertTrue(neighbourFields.contains(field7));
        assertTrue(neighbourFields.contains(field8));
        assertTrue(neighbourFields.contains(field9));

        neighbourFields = instance.getNeighbourFieldsFor(field9);
        assertEquals(3, neighbourFields.size());
        assertTrue(neighbourFields.contains(field5));
        assertTrue(neighbourFields.contains(field6));
        assertTrue(neighbourFields.contains(field8));
    }
}
