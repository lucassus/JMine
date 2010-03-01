package org.lucassus.jmine.field.observers;

public interface FieldObserver {

    public void mineWasDetonated();

    public void fieldWasDetonated();

    public void flagWasSet();

    public void flagWasRemoved();

}
