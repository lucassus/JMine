package org.lucassus.jmine.enums;

import java.net.URL;
import javax.swing.ImageIcon;

/**
 * Collection of game icons.
 */
public enum GameIcon {

    /**
     * Flag icon.
     */
    FLAG("flag.gif"),
    /**
     * Flag set on mine after detonation.
     */
    FLAG_WRONG("flag_wrong.gif"),
    /**
     * The Mine icon.
     */
    MINE("mine.gif"),
    /**
     * Detonated mine icon.
     */
    MINE_DETONATED("mine_detonated.gif"),
    /**
     * Face icon.
     */
    FACE("face.gif"),
    /**
     * Face icon after game win.
     */
    FACE_WIN("face_win.gif"),
    /**
     * Face icon after lost game.
     */
    FACE_DEAD("face_dead.gif"),
    /**
     * Questin mark icon.
     */
    MARK("question_mark.png");

    private static final String ICONS_PATH = "/resources/icons/";

    /**
     * The icon.
     */
    private final ImageIcon icon;

    /**
     * Creates a new instance of GameIcon.
     * @param iconFileName a name of file with icon.
     */
    GameIcon(final String iconFileName) {
        URL location = getClass().getResource(ICONS_PATH + iconFileName);
        icon = new ImageIcon(location);
    }

    /**
     * Returns the icon.
     * @return the icon.
     */
    public ImageIcon getIcon() {
        return icon;
    }
}
