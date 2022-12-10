package org.example;

import java.util.ArrayList;

public class AntSolver {

    private final int[][] ringCosts;
    private final int[][] starCosts;
    private final int nbrOfNodes;


    public AntSolver(int[][] ringCosts, int[][] starCosts, int nbrOfNodes){
        this.ringCosts = ringCosts;
        this.starCosts = starCosts;
        this.nbrOfNodes = nbrOfNodes;
    }

    public Solution initialize(AntSolver problem){
        ArrayList<Integer> ringNodes = new ArrayList<>();
        ArrayList<Integer[]> starNodes = new ArrayList<>();

        //for the initial solution every node must be inside the ring.
        for (int i = 1; i <= nbrOfNodes; i++){
            ringNodes.add(i);
        }
        return new Solution(ringNodes, starNodes);
    }

    public Solution solve(){
        return null;
    }

}
