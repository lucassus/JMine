package org.lucassus.jmine.field;

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
