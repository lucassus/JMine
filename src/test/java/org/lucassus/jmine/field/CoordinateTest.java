package org.lucassus.jmine.field;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lucassus
 */
public class CoordinateTest {

    private Coordinate instance;

    @Before
    public void setUp() {
        instance = new Coordinate(0, 0);
    }

    /**
     * Test of getX method, of class Coordinate.
     */
    @Test
    public void testGetX() {
        int expResult = 0;
        int result = instance.getX();
        assertEquals(expResult, result);
    }

    /**
     * Test of setX method, of class Coordinate.
     */
    @Test
    public void testSetX() {
        int x = 1;
        instance.setX(x);
        assertEquals(x, instance.getX());
    }

    /**
     * Test of getY method, of class Coordinate.
     */
    @Test
    public void testGetY() {
        int expResult = 0;
        int result = instance.getY();
        assertEquals(expResult, result);
    }

    /**
     * Test of setY method, of class Coordinate.
     */
    @Test
    public void testSetY() {
        int y = 1;
        instance.setY(y);
        assertEquals(y, instance.getY());
    }

}