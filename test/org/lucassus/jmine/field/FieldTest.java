package org.lucassus.jmine.field;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

public class FieldTest extends TestCase {

    private Field instance;
    private List<Field> neighborFields;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        neighborFields = new ArrayList<Field>();
        neighborFields.add(new Field());
        neighborFields.add(new Field());
        neighborFields.add(new Field());
        neighborFields.add(new Field());

        instance = new Field();
        instance.setNeighborFields(neighborFields);
    }

    /**
     * Test of getNeighborMinesCount method, of class Field.
     */
    public void testGetNeighborMinesCount() {
        int expResult = 2;
        for (int i = 0; i < expResult; i++) {
            neighborFields.get(i).setHasMine(true);
        }

        int result = instance.getNeighborMinesCount();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNeighborFlagsCount method, of class Field.
     */
    public void testGetNeighborFlagsCount() {
        int expResult = 3;
        for (int i = 0; i < expResult; i++) {
            neighborFields.get(i).setHasFlag(true);
        }

        int result = instance.getNeighborFlagsCount();
        assertEquals(expResult, result);
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

    /**
     * Test of setCoordinate method, of class Field.
     */
    public void testSetCoordinate() {
        Coordinate coordinate = new Coordinate(1, 1);
        instance.setCoordinate(coordinate);

        assertEquals(coordinate, instance.getCoordinate());
    }

}
