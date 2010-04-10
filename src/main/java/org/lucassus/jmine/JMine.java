package org.lucassus.jmine;

import org.apache.log4j.BasicConfigurator;

/**
 * Entry point for the Application.
 * @author lucassus
 */
public class JMine {

    /**
     * Entry point.
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        BasicConfigurator.configure();
        new JMineFrame().setVisible(true);
    }
}
