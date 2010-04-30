package org.lucassus.jmine.field;

import java.util.ArrayList;
import java.util.List;
import org.lucassus.jmine.enums.GameIcon;
import org.lucassus.jmine.field.observers.IFieldObserver;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@Test
public class FieldTest {

    private Field instance;
    private List<Field> neighborFields;

    @Mock
    private IFieldObserver observerMock;

    @Mock
    private Field neighborField1, neighborField2, neighborField3, neighborField4;

    @BeforeMethod
    public void setUp() throws Exception {
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
    @Test
    public void getNeighborMinesCount() {
        when(neighborField1.hasMine()).thenReturn(true);
        when(neighborField2.hasMine()).thenReturn(true);

        int result = instance.getNeighborMinesCount();
        assertEquals(2, result);
    }

    /**
     * Test of getNeighborFlagsCount method, of class Field.
     */
    @Test
    public void getNeighborFlagsCount() {
        when(neighborField1.hasFlag()).thenReturn(true);
        when(neighborField3.hasFlag()).thenReturn(true);

        int result = instance.getNeighborFlagsCount();
        assertEquals(2, result);
    }

    /**
     * Test of hasMine method, of class Field.
     */
    @Test
    public void hasMine() {
        assertFalse(instance.hasMine());

        instance.setHasMine(true);
        assertTrue(instance.hasMine());
    }

    /**
     * Test of isDetonated method, of class Field.
     */
    @Test
    public void isDetonated() {
        assertFalse(instance.isDetonated());

        instance.setDetonated(true);
        assertTrue(instance.isDetonated());
    }

    /**
     * Test of hasFlag method, of class Field.
     */
    @Test
    public void hasFlag() {
        assertFalse(instance.hasFlag());

        instance.setHasFlag(true);
        assertTrue(instance.hasFlag());
    }

    /**
     * Test of getNeighborFields method, of class Field.
     */
    @Test
    public void getNeighborFields() {
        List result = instance.getNeighborFields();
        assertEquals(neighborFields, result);
    }

    /**
     * Test of detonate method, of class Field.
     */
    @Test
    public void detonate() {
        instance.detonate();
        assertTrue(instance.isDetonated());
    }

    @Test
    public void detonateNeighbourFields() {
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
    @Test
    public void hasMineWithFlag() {
        assertFalse(instance.hasMineWithFlag());

        instance.setHasMine(true);
        instance.setHasFlag(true);
        assertTrue(instance.hasMineWithFlag());
    }

    /**
     * Test of hasFlagWithoutMine method, of class Field.
     */
    @Test
    public void hasFlagWithoutMine() {
        assertFalse(instance.hasFlagWithoutMine());

        instance.setHasFlag(true);
        assertTrue(instance.hasFlagWithoutMine());
    }

    /**
     * Test of hasMineWithoutFlag method, of class Field.
     */
    @Test
    public void hasMineWithoutFlag() {
        assertFalse(instance.hasMineWithoutFlag());

        instance.setHasMine(true);
        assertTrue(instance.hasMineWithoutFlag());
    }

}
