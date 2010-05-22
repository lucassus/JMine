package org.lucassus.jmine.field;

import java.util.Iterator;

/**
 * An iterator over the Mine Field.
 */
class MineFieldIterator implements Iterator<Field> {

    private Field[][] fields;
    private int x;
    private int y;

    /**
     * Creates a new instance of MineFieldIterator.
     * @param fields
     */
    public MineFieldIterator(Field[][] fields) {
        this.fields = fields;
        x = 0;
        y = 0;
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    @Override
    public boolean hasNext() {
        return y < fields.length;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     * @exception NoSuchElementException iteration has no more elements.
     */
    @Override
    public Field next() {
        if (!hasNext()) {
            return null;
        }

        // get the next Field
        Field field = fields[y][x];

        // shift the cursors
        if (++x >= fields[y].length) {
            x = 0;
            y++;
        }

        return field;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
