package org.lucassus.jmine.field.listeners;

import java.awt.event.MouseAdapter;
import org.lucassus.jmine.enums.GameIcon;
import java.awt.event.MouseEvent;
import org.lucassus.jmine.field.Field;

/**
 * Handles mouse actions on the Field.
 */
public class FieldMouseListener extends MouseAdapter {

    private Field field;

    /**
     * Creates the FieldMouseListener instance.
     * @param field
     */
    public FieldMouseListener(Field field) {
        this.field = field;
    }

    /**
     * Handles a left mouse button click.
     */
    private void leftMouseButtonClick() {
        if (field.hasFlag()) {
            return;
        }

        if (field.hasMine()) {
            field.setDetonated(true);
            field.setIcon(GameIcon.MINE_DETONATED.getIcon());
            field.getObserver().mineWasDetonated();
        } else {
            field.detonate();
            field.getObserver().fieldWasDetonated();
        }
    }

    /**
     * Handles a right mouse button click.
     */
    private void rightMouseButtonClick() {
        // ustawienie/sciagniecie flagi z pola minowego
        // jesli pole zostalo juz zdetonowane
        if (field.isDetonated()) {
            return;
        }

        if (!field.hasFlag()) {
            field.setHasFlag(true);
            field.getObserver().flagWasSet();
        } else {
            field.setHasFlag(false);
            field.getObserver().flagWasRemoved();
        }
    }

    /**
     * Handles a middle mouse button click.
     */
    private void middleMouseButtonClick() {
        if (field.isDetonated() && field.getNeighborMinesCount() > 0) {

            // sprawdzamy czy liczba min w sasiedztwie zgadza sie
            // z liczba postawionych flag
            if (field.getNeighborMinesCount() == field.getNeighborFlagsCount()) {
                // detonujemy sasiednie pola
                field.detonateNeighbourFields();
                field.getObserver().fieldWasDetonated();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        final int button = event.getButton();

        if (button == MouseEvent.BUTTON1) {
            leftMouseButtonClick();
        } else if (button == MouseEvent.BUTTON3) {
            rightMouseButtonClick();
        } else if (button == MouseEvent.BUTTON2) {
            middleMouseButtonClick();
        }
    }
}
