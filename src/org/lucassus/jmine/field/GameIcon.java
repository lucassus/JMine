package org.lucassus.jmine.field;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author lucassus
 */
public enum GameIcon {

    FLAG("flag.gif"),
    FLAG_WRONG("flag_wrong"),
    MINE("mine.gif"),
    MINE_DETONATED("mine_detonate.gif"),
    FACE("face.gif"),
    FACE_WIN("face_win.gif"),
    FACE_DEAD("face_dead.gif");

    private final String PATH_PREFIX = "org/lucassus/jmine/resources/";
    private final Icon icon;

    GameIcon(String iconPath) {
        this.icon = new ImageIcon(getClass().getResource(PATH_PREFIX + iconPath));
    }

    public Icon getIcon() {
        return this.icon;
    }
}
