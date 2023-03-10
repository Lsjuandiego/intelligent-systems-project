package AStarPathFinding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
El Nodo será el botón
 */
public class Node extends JButton implements ActionListener {

    Node parent;
    int col, row, gCost, hCost, fCost;

    boolean start, goal, solid, open, checked;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;

        setBackground(Color.white);
        setForeground(Color.black);
        addActionListener(this);

    }

    public void setAsStart() {
        setBackground(Color.green);
        setForeground(Color.white);
        setText("Start");
        start = true;
    }

    /*
    obstacles
     */
    public void setAsSolid() {
        setBackground(Color.black);
        setForeground(Color.black);
        solid = true;
    }

    public void setAsGoal() {
        setBackground(Color.red  );
        setForeground(Color.white);
        setText("Goal");
        start = true;
    }


    public void setAsOpen() {
        open = true;
    }

    public void setAsChecked() {
        if(start == false && goal == false){
            setBackground(Color.blue);
            setForeground(Color.black);
        }
        checked = true;
    }

    public void setAsPath(){
        setBackground(Color.green);//green
        setForeground(Color.black);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
