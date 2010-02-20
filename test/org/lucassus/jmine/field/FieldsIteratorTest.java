package org.lucassus.jmine.field;

import junit.framework.TestCase;

/**
 *
 * @author lucassus
 */
public class FieldsIteratorTest extends TestCase {

    private int mineFieldWidth;
    private int mineFieldHeight;
    private Field[][] mineFields;
    
    private FieldsIterator iterator;

    @Override
    protected void setUp() throws Exception {
        mineFieldWidth = 10;
        mineFieldHeight = 5;
        mineFields = new Field[mineFieldWidth][mineFieldHeight];

        for (int i = 0; i < mineFieldWidth; i++) {
            for (int j = 0; j < mineFieldHeight; j++) {
                mineFields[i][j] = new Field(i, j);
            }
        }

        iterator = new FieldsIterator(mineFields);
    }

    /**
     * Test of hasNext method, of class FieldsIterator.
     */
    public void testHasNext() {
        assert iterator.hasNext();
    }

    public void testGetHeight() {
        assertEquals(mineFieldHeight, iterator.getHeight());
    }

    public void testGetWidth() {
        assertEquals(mineFieldWidth, iterator.getWidth());
    }

    /**
     * Test of next method, of class FieldsIterator.
     */
    public void testNext() {
        for (int i = 0; i < mineFieldWidth; i++) {
            for (int j = 0; j < mineFieldHeight; j++) {
                Field field = mineFields[i][j];
                assertEquals(field, iterator.next());
            }
        }
    }
}
