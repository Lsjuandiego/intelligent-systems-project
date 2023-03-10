package AStarPathFinding;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Panel extends JPanel {
    //los nodos serán los bloques

    //screen settings
    final int maxCol = 15;
    final int maxRow = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;


    //Node
    Node[][] node = new Node[maxCol][maxRow];
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    //others
    boolean goalReached = false;

    public Panel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRow, maxCol));
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);

        //Place nodes
        int col = 0;
        int row = 0;

        while (col <maxCol && row <maxRow){
            node[col][row] = new Node(col,row);
            this.add(node[col][row]);
            col++;

            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
        //set start and goal node
        setStartNode(3,6);
        setGoalNode(11,3);
        //solid nodes
        setSolidNode(10,2);
        setSolidNode(10,3);
        setSolidNode(10,4);
        setSolidNode(10,5);
        setSolidNode(10,6);
        setSolidNode(10,7);
        setSolidNode(6,2);
        setSolidNode(7,2);
        setSolidNode(8,2);
        setSolidNode(9,2);
        setSolidNode(11,7);
        setSolidNode(12,7);
        setSolidNode(6,1);

        //set cost
        setCostOnNodes();
    }

    private void setStartNode(int col, int row){
        node[col][row].setAsStart();
        startNode = node[col][row];
        currentNode = startNode;
    }
    private void setGoalNode(int col, int row){
        node[col][row].setAsGoal();
        goalNode = node[col][row];
    }

    private void setSolidNode(int col, int row){
        node[col][row].setAsSolid();
    }

    private void setCostOnNodes(){
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

    public void autoSearch(){
        while (goalReached == false){
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
    public void search(){
        if (goalReached == false){
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
    private void openNode(Node node){
        if (node.open == false && node.checked == false && node.solid == false){
            //if the node is not open yet, add it to the open list
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackThePath(){
        //backtrack and draw the best path
        Node current = goalNode;
        while (current != startNode){
            current = current.parent;

            if(current != startNode){
                current.setAsPath();
            }
        }
    }
}
