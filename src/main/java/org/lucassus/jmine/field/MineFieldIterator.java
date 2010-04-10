package org.lucassus.jmine.field;

import java.util.Iterator;

class MineFieldIterator implements Iterator<Field> {

    private Field[][] fields;
    private int x;
    private int y;

    public MineFieldIterator(Field[][] fields) {
        this.fields = fields;
        x = 0;
        y = 0;
    }

    @Override
    public boolean hasNext() {
        return y < fields.length;
    }

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
