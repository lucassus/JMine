package org.lucassus.jmine.field.observers;

public interface FieldObserver {

    public void mineDetonated();

    public void fieldDetonated();

    public void flagWasSet();

    public void flagWasRemoved();

}
