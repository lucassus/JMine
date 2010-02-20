package org.lucassus.jmine.field;

import java.util.Iterator;

/**
 *
 * @author lucassus
 */
public class FieldsIterator implements Iterator<Field> {

    private Field[][] fields;
    private int i;
    private int j;

    public FieldsIterator(Field[][] fields) {
        this.fields = fields;
        i = 0;
        j = 0;
    }

    public int getWidth() {
        return fields.length;
    }

    public int getHeight() {
        return fields[0].length;
    }

    @Override
    public boolean hasNext() {
        return i < getWidth() && j < getHeight();
    }

    @Override
    public Field next() {
        if (!hasNext()) {
            return null;
        }

        Field field = fields[i][j];

        j++;
        if (j >= getHeight()) {
            j = 0;
            i++;
        }

        return field;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
