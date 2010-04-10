package org.lucassus.jmine.field;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.lucassus.jmine.enums.GameIcon;
import org.lucassus.jmine.field.observers.IFieldObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class FieldTest extends TestCase {

    private Field instance;
    private List<Field> neighborFields;

    @Mock
    private IFieldObserver observerMock;

    @Mock
    private Field neighborField1, neighborField2, neighborField3, neighborField4;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        neighborFields = new ArrayList<Field>();
        neighborFields.add(neighborField1);
        neighborFields.add(neighborField2);
        neighborFields.add(neighborField3);
        neighborFields.add(neighborField4);

        instance = new Field();
        instance.setNeighborFields(neighborFields);
        instance.attachObserver(observerMock);
    }

    /**
     * Test of getNeighborMinesCount method, of class Field.
     */
    public void testGetNeighborMinesCount() {
        when(neighborField1.hasMine()).thenReturn(true);
        when(neighborField2.hasMine()).thenReturn(true);

        int result = instance.getNeighborMinesCount();
        assertEquals(2, result);
    }

    /**
     * Test of getNeighborFlagsCount method, of class Field.
     */
    public void testGetNeighborFlagsCount() {
        when(neighborField1.hasFlag()).thenReturn(true);
        when(neighborField3.hasFlag()).thenReturn(true);

        int result = instance.getNeighborFlagsCount();
        assertEquals(2, result);
    }

    /**
     * Test of hasMine method, of class Field.
     */
    public void testHasMine() {
        assertFalse(instance.hasMine());

        instance.setHasMine(true);
        assertTrue(instance.hasMine());
    }

    /**
     * Test of isDetonated method, of class Field.
     */
    public void testIsDetonated() {
        assertFalse(instance.isDetonated());

        instance.setDetonated(true);
        assertTrue(instance.isDetonated());
    }

    /**
     * Test of hasFlag method, of class Field.
     */
    public void testHasFlag() {
        assertFalse(instance.hasFlag());

        instance.setHasFlag(true);
        assertTrue(instance.hasFlag());
    }

    /**
     * Test of getNeighborFields method, of class Field.
     */
    public void testGetNeighborFields() {
        List result = instance.getNeighborFields();
        assertEquals(neighborFields, result);
    }

    /**
     * Test of detonate method, of class Field.
     */
    public void testDetonate() {
        instance.detonate();
        assertTrue(instance.isDetonated());
    }

    public void testDetonateNeighbourFields() {
        when(neighborField1.hasFlagWithoutMine()).thenReturn(true);
        when(neighborField2.hasMineWithoutFlag()).thenReturn(true);
        when(neighborField4.hasMineWithFlag()).thenReturn(true);

        instance.detonateNeighbourFields();

        verify(observerMock).mineWasDetonated();

        verify(neighborField1).setDetonated(true);
        verify(neighborField1).setIcon(GameIcon.FLAG_WRONG.getIcon());

        verify(neighborField2).setDetonated(true);
        verify(neighborField2).setIcon(GameIcon.MINE_DETONATED.getIcon());

        verify(neighborField3).detonate();
    }

    /**
     * Test of hasMineWithFlag method, of class Field.
     */
    public void testHasMineWithFlag() {
        assertFalse(instance.hasMineWithFlag());

        instance.setHasMine(true);
        instance.setHasFlag(true);
        assertTrue(instance.hasMineWithFlag());
    }

    /**
     * Test of hasFlagWithoutMine method, of class Field.
     */
    public void testHasFlagWithoutMine() {
        assertFalse(instance.hasFlagWithoutMine());

        instance.setHasFlag(true);
        assertTrue(instance.hasFlagWithoutMine());
    }

    /**
     * Test of hasMineWithoutFlag method, of class Field.
     */
    public void testHasMineWithoutFlag() {
        assertFalse(instance.hasMineWithoutFlag());

        instance.setHasMine(true);
        assertTrue(instance.hasMineWithoutFlag());
    }

}
