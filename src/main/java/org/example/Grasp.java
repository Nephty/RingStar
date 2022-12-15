package org.example;

import org.example.Solution;

import java.util.ArrayList;
import java.util.Arrays;

public class Grasp {
    private double ALPHA;
    private int maxIterations;

    private int[][] starCost;

    private int[][] ringCost;

    private final int SIZE;


    public Grasp(int maxIter, double alpha, int[][] ringCost, int[][] starCost, int size) {
        this.ALPHA = alpha;
        this.maxIterations = maxIter;
        this.starCost = starCost;
        this.ringCost = ringCost;
        this.SIZE = size;
    }

    public Solution findSolution() {
        // Implement the grasp algorithm for the ring star problem
        Solution bestSolution = new Solution();
        int bestCost = Integer.MAX_VALUE;
        for (int i = 0; i < maxIterations; i++) {
            Solution solution = constructSolution(); // Construct a greedy randomised solution
            Solution solution_local = localSearch(solution); // Recherche localement autour de la solution
            int cost = solution_local.getCost();
            if (cost < bestCost) {
                bestCost = cost;
                bestSolution = solution;
            }

            System.out.println("Iteration " + i + " bestCost: " + bestSolution.getCost());
        }
        return bestSolution;

    }


    /**
     * Heuristique permettant d'estimer le coût d'ajout d'un noeud à une solution.
     * -> voir explication de la construction du LCR (Liste des candidats restrainte)
     * @param node Le noeud à estimer
     * @return Le coût estimé
     */
    //public double estimation(int node, Solution solution) {
        // Implement the estimation function
      //  return minIncrement(node,solution) + meanAllStar(node,solution);
    //}

    /**
     * Cherche le plus petit coût pour ajouter le noeud dans le cycle.
     * (on veut trouver l'endroit où (dist_{before} + dist_{after}) est minimum.)
     * @param node Le noeud à ajouter
     * @return La valeur du plus petit coût ajouté
     */
    public int[] minIncrement(int node, Solution solution) {
        // Implement the minIncrement function
        ArrayList<Integer> solutionRing = solution.getRing();
        int ringSize = solutionRing.size();
        // On prend comme valeur initiale le coût de l'ajouter à la fin.
        int rightIndex = 0;
        int leftIndex = ringSize-1;
        int min = Main.ringCost[node][rightIndex]
                + Main.ringCost[leftIndex][node];

        for (int i=1; i<ringSize; i++) {
            int cost = Main.ringCost[solutionRing.get(i)-1][node-1]
                    + Main.ringCost[node-1][solutionRing.get((i+1)%ringSize)-1];
            if (cost < min) {
                min = cost;
                rightIndex = i+1;
                leftIndex = i;

            }
        }

        // Utiliser ça ou alors on ajoute simplement à la fin ?
        return new int[]{min, leftIndex, rightIndex};
    }

    /**
     *
    public double meanAllStar(int node, Solution solution) {
        // Implement the meanAllStar
        double res = 0;
        for(int j=0; j<Main.size; j++){
            if (!solution.getRing_bool()[j] && j != node){
                res += Main.starCost[node][j];
            }
        }
        return  res / Main.size;
    }
    */

    private Solution constructSolution() {
        Solution bestSolution = null;

        return null;
    }

    /**
     * Movement:
     * -add a node to the ring
     * -remove a node from the ring
     * -swap two node in the ring
     * @param solution a solution
     * @return
             */
    private Solution localSearch(Solution solution) {
        Solution bestNeighbour = null;
        for (Solution neighbour: solution.addNodeNeighbourhood()) {
            if(neighbour.getCost() < solution.getCost()) {
                bestNeighbour = neighbour;
            }
        }
        for (Solution neighbour: solution.removeNodeNeighbourhood()) {
            if(neighbour.getCost() < solution.getCost()) {
                bestNeighbour = neighbour;
            }
        }
        for (Solution neighbour: solution.swapNodeNeighbourhood()) {
            if(neighbour.getCost() < solution.getCost()) {
                bestNeighbour = neighbour;
            }
        }
        return bestNeighbour == null ? solution: bestNeighbour;
    }
}
