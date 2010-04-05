package org.lucassus.jmine.enums;

import org.lucassus.jmine.enums.GameIcon;
import java.net.URL;
import javax.swing.ImageIcon;
import junit.framework.TestCase;

public class GameIconTest extends TestCase {

    /**
     * Test of getIcon method, of class GameIcon.
     */
    public void testGetIcon() {
        URL iconLocation = getClass().getResource(GameIcon.PATH_PREFIX + "flag.gif");
        ImageIcon expResult = new ImageIcon(iconLocation);
        ImageIcon result = GameIcon.FLAG.getIcon();

        assertTrue(result.toString().equals(expResult.toString()));
    }

}
