package org.lucassus.jmine.field;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FieldMouseListener implements MouseListener {

    private Field field;

    public FieldMouseListener(Field field) {
        this.field = field;
    }

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
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            leftMouseButtonClick();
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            rightMouseButtonClick();
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            middleMouseButtonClick();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
