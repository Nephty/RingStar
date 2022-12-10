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
        float[][] visibility = new float[problem.nbrOfNodes][problem.nbrOfNodes];
        float[][] pheromone = new float[problem.nbrOfNodes][problem.nbrOfNodes];

        //for the initial solution every node must be inside the ring.
        for (int i = 1; i <= nbrOfNodes; i++){
            ringNodes.add(i);
        }

        //Compute the cost of the initial solution to compute the base pheromone
        int cost = getCost(ringNodes, starNodes, problem.ringCosts, problem.starCosts);

        //Update the visibility and pheromone matrix.
        int j = 0;
        while(j < ringNodes.size()){
            visibility[ringNodes.get(j) - 1][ringNodes.get(j + 1) - 1] = 1/ringCosts[ringNodes.get(j) - 1][ringNodes.get(j + 1) - 1];
            pheromone[ringNodes.get(j) - 1][ringNodes.get(j + 1) - 1] = 1/(cost * ringNodes.size());
        }

        return new Solution(ringNodes, starNodes, visibility, pheromone, cost);
    }

    public int getCost(ArrayList<Integer> ring, ArrayList<Integer[]> star, int[][] ringCosts, int[][] starCosts) {
        int i = 0;
        int totalCost = 0;

        while (i < ring.size()){
            totalCost += ringCosts[ring.get(i) - 1][ring.get(i+1) - 1];
            i ++;
        }
        for (Integer[] integers : star) {
            int starNode = integers[0];
            int destNode = integers[1];
            totalCost += starCosts[starNode - 1][destNode - 1];
        }

        return totalCost;
    }

    public Solution solve(){
        return null;
    }

}
