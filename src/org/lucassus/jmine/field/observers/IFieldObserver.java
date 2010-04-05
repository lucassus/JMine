package org.lucassus.jmine.field.observers;

public interface IFieldObserver {

    public void mineWasDetonated();

    public void fieldWasDetonated();

    public void flagWasSet();

    public void flagWasRemoved();

}
