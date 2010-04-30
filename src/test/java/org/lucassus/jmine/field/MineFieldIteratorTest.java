package org.lucassus.jmine.field;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

@Test
public class MineFieldIteratorTest {

    private Field[][] fields;
    private Field field1, field2, field3, field4;
    private MineFieldIterator instance;

    @BeforeMethod
    public void setUp() {
        field1 = new Field();
        field2 = new Field();
        field3 = new Field();
        field4 = new Field();

        fields = new Field[][]{
                    {field1, field2},
                    {field3, field4}
                };
        instance = new MineFieldIterator(fields);
    }

    /**
     * Test of hasNext and next method, of class MineFieldIterator.
     */
    @Test
    public void hasNextAndNext() {
        assertTrue(instance.hasNext());
        assertEquals(field1, instance.next());

        assertTrue(instance.hasNext());
        assertEquals(field2, instance.next());

        assertTrue(instance.hasNext());
        assertEquals(field3, instance.next());

        assertTrue(instance.hasNext());
        assertEquals(field4, instance.next());

        assertFalse(instance.hasNext());
        assertNull(instance.next());
    }
}
