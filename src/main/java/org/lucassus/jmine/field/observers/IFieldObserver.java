package org.lucassus.jmine.field.observers;

/**
 * Mine Field observer.
 */
public interface IFieldObserver {

    /**
     * Indicates that a Mine was detonated.
     */
    public void mineWasDetonated();

    /**
     * Indicates that a Field was detonated.
     */
    public void fieldWasDetonated();

    /**
     * Indicates that a flag was set on the field.
     */
    public void flagWasSet();

    /**
     * Indicates that a flag was removed from the field.
     */
    public void flagWasRemoved();

}
