package org.lucassus.jmine.enums;

import java.net.URL;
import javax.swing.ImageIcon;

public enum GameIcon {

    FLAG("flag.gif"),
    FLAG_WRONG("flag_wrong.gif"),
    MINE("mine.gif"),
    MINE_DETONATED("mine_detonated.gif"),
    FACE("face.gif"),
    FACE_WIN("face_win.gif"),
    FACE_DEAD("face_dead.gif"),
    MARK("question_mark.png");

    private final ImageIcon icon;

    GameIcon(String iconFileName) {
        URL location = getClass().getResource("/resources/" + iconFileName);
        icon = new ImageIcon(location);
    }

    public ImageIcon getIcon() {
        return icon;
    }
}
