package AStarPathFinding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

/*
El Nodo será el botón
 */
public class Node extends JButton implements ActionListener {

    Node parent;
    int col, row, gCost, hCost, fCost;

    boolean start, goal, solid, open, checked;

    /***
     * Constructor
     * @param col
     * @param row
     */
    public Node(int col, int row) {
        super();
        this.col = col;
        this.row = row;

        setBackground(Color.white);
        setForeground(Color.black);
        addActionListener(this);

    }
    public void setValue(char value) {
        setText(Character.toString(value));
    }

    /***
     * Nodo de inicio
     */
    public void setAsStart() {
        setBackground(Color.green);
        setForeground(Color.white);
        setText("Start");
        start = true;
    }

    /***
     * Obstaculos
     */
    public void setAsSolid() {
        setBackground(Color.black);
        setForeground(Color.black);
        solid = true;
    }

    /***
     * Asignación nodo meta
     */
    public void setAsGoal() {
        setBackground(Color.red  );
        setForeground(Color.white);
        setText("Goal");
        start = true;
    }

    /***
     * True si se encuentra en la lista abierta
     */
    public void setAsOpen() {
        open = true;
    }

    /***
     * pinta de color azul los nodos ya visitados
     */
    public void setAsChecked() {
        if(!start && !goal){
            setBackground(Color.blue);
            setForeground(Color.black);
        }
        checked = true;
    }

    /***
     * Pinta de color verde el camino
     */
    public void setAsPath(){
        setBackground(Color.green);//green
        setForeground(Color.black);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
