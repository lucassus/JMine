package org.lucassus.jmine.field;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author lucassus
 */
@Test
public class CoordinateTest {

    private Coordinate instance;

    @BeforeMethod
    public void setUp() {
        instance = new Coordinate(0, 0);
    }

    /**
     * Test of getX method, of class Coordinate.
     */
    @Test
    public void getX() {
        int expResult = 0;
        int result = instance.getX();
        assertEquals(expResult, result);
    }

    /**
     * Test of setX method, of class Coordinate.
     */
    @Test
    public void setX() {
        int x = 1;
        instance.setX(x);
        assertEquals(x, instance.getX());
    }

    /**
     * Test of getY method, of class Coordinate.
     */
    @Test
    public void getY() {
        int expResult = 0;
        int result = instance.getY();
        assertEquals(expResult, result);
    }

    /**
     * Test of setY method, of class Coordinate.
     */
    @Test
    public void setY() {
        int y = 1;
        instance.setY(y);
        assertEquals(y, instance.getY());
    }

}