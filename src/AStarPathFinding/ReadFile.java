package AStarPathFinding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ReadFile extends JFrame implements ActionListener {

    private int maxCol;
    private int maxRow;


    //Node
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    //Objetivo alcanzado?
    boolean goalReached = false;
    private final JButton selectFileButton;
    private final JPanel matrixPanel;


    public ReadFile() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        selectFileButton = new JButton("Select File");
        selectFileButton.addActionListener(this);
        add(selectFileButton, BorderLayout.NORTH);

        matrixPanel = new JPanel();
        matrixPanel.setLayout(new GridLayout(0, 7));
        add(new JScrollPane(matrixPanel), BorderLayout.CENTER);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectFileButton) {
            JFileChooser fileChooser = new JFileChooser(".");
            int selected = fileChooser.showOpenDialog(this);
            if (selected == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                System.out.println("File path: " + file.getAbsolutePath());
                Node[][] matrix = readFromFile(file);
                displayMatrix(matrix);
            }
        }
    }

    private Node[][] readFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            List<List<Character>> matrix = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] cells = line.split("[^a-zA-Z]");
                List<Character> row = new ArrayList<>();
                for (String cell : cells) {
                    if (!cell.isEmpty()) {
                        row.add(cell.charAt(0));
                    }
                }
                matrix.add(row);
            }

            Node[][] nodes = new Node[matrix.size()][];
            for (int i = 0; i < matrix.size(); i++) {
                List<Character> row = matrix.get(i);
                nodes[i] = new Node[row.size()];
                for (int j = 0; j < row.size(); j++) {
                    Node node = new Node(i, j);
                    node.setValue(row.get(j));
                    nodes[i][j] = node;

                    switch (row.get(j)) {
                        case 'I' -> setStartNode(nodes, i, j);
                        case 'F' -> setGoalNode(nodes, i, j);
                        case 'R', 'M' -> setSolidNode(nodes, i, j);
                    }
                }
            }

            return nodes;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void displayMatrix(Node[][] matrix) {
        matrixPanel.removeAll();
        matrixPanel.setLayout(new GridLayout(matrix.length, matrix[0].length));
        maxCol = matrix.length;
        maxRow = matrix[0].length;

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                Node node = matrix[row][col];

                matrixPanel.add(node);
            }
        }
        setCostOnNodes(matrix);
        autoSearch(matrix);
        pack();
    }

    /***
     * posición del comienzo
     * @param col
     * @param row
     */
    private void setStartNode(Node[][] node,int col, int row){
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }
    /***
     * Asignación nodo final
     * @param col
     * @param row
     */
    private void setGoalNode(Node[][] node, int col, int row){
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    /***
     * Asignar nodos "pared"
     * @param col
     * @param row
     */
    private void setSolidNode(Node[][] node, int col, int row){
        node[col][row].setAsSolid();
    }

    /***
     * Da costo de los nodos
     */
    private void setCostOnNodes(Node[][] node){
        int col = 0 , row = 0;
        while (col <maxCol && row <maxRow){
            getCost(node[col][row]);
            col++;

            if (col == maxCol) {
                col = 0;
                row++;
            }
        }

    }
    /***
     * Metodo para calcular los costos de los nodos
     * @param node
     */
    private void getCost(Node node){
        //get the G cost (distance from the start node to the current node)
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);

        node.gCost = xDistance + yDistance;

        //get the H cost (distance from the start node to the current node)
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);

        node.hCost = xDistance + yDistance;

        //get F cost (total cost)
        node.fCost = node.gCost + node.hCost;

        //display the cost on node
        if(node != startNode && node != goalNode){
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
        }
    }
    /***
     * Verificar si el nodo no está abierto aun para agregarlo
     * a la lista
     * @param node
     */
    private void openNode(Node node){
        if (!node.open && !node.checked && !node.solid){
            //if the node is not open yet, add it to the open list
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }
    public void autoSearch(Node[][] node){
        while (!goalReached){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            //open the up node
            if(row-1 >=0){
                openNode(node[col][row-1]);
            }
            //open the left node
            if(col-1 >= 0){
                openNode(node[col-1][row]);
            }
            //open the down node
            if (row+1 < maxRow){
                openNode(node[col][row+1]);
            }

            //open the right node
            if(col+1 < maxCol){
                openNode(node[col+1][row]);
            }
            // find the best node
            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                //check if this nodes F cost is better
                if(openList.get(i).fCost < bestNodeFCost){
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                }
                //if F cost is equal, check the G cost
                else if (openList.get(i).fCost == bestNodeFCost) {
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
            //after the loop, we get the best node wich is our next step
            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode){
                goalReached = true;
                trackThePath();
            }
        }
    }

    /***
     * Marca el mejor camino
     */
    private void trackThePath(){
        //backtrack and draw the best path

        Node current = goalNode;
        while (current != startNode){
            current = current.parent;
            System.out.println("Current: "+current.row+" , "+current.col);
            if(current != startNode){
                current.setAsPath();
            }
        }
    }
}