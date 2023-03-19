package AStarPathFinding;

import javax.swing.*;

/**
 *
 */
public class Main {
    /***
     *
     * @param args
     */
    public static void main(String[] args) {

        //new ReadFile();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);
        frame.add(new Panel());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}