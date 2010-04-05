package org.lucassus.jmine.field;

import java.net.URL;
import javax.swing.ImageIcon;

public enum GameIcon {

    FLAG("flag.gif"),
    FLAG_WRONG("flag_wrong.gif"),
    MINE("mine.gif"),
    MINE_DETONATED("mine_detonated.gif"),
    FACE("face.gif"),
    FACE_WIN("face_win.gif"),
    FACE_DEAD("face_dead.gif");

    private final ImageIcon icon;
    public static final String PATH_PREFIX = "/org/lucassus/jmine/resources/";

    GameIcon(String iconFileName) {
        URL location = getClass().getResource(PATH_PREFIX + iconFileName);
        icon = new ImageIcon(location);
    }

    public ImageIcon getIcon() {
        return icon;
    }
}
