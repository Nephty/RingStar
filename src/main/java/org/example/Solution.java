package org.example;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    private ArrayList<Integer> ring;

    private ArrayList<Integer[]> star;

    private final boolean[] isRing;
    private int cost = -1;

    private final Grasp GRASP;

    public Solution(ArrayList<Integer> ring, Grasp grasp) {
        this.GRASP = grasp;
        this.ring = ring;
        this.isRing = new boolean[GRASP.SIZE];
        for (Integer node : ring) {
            isRing[node - 1] = true;
        }
    }

    public Solution(Grasp grasp) {
        this.GRASP = grasp;
        this.ring = new ArrayList<>(List.of(1));
        this.isRing = new boolean[GRASP.SIZE];
        isRing[0] = true;
    }

    public Solution(Solution solution, Grasp grasp) {
        this.GRASP = grasp;
        this.ring = new ArrayList<>(solution.getRing());
        this.isRing = new boolean[GRASP.SIZE];
        for (Integer node : ring) {
            isRing[node - 1] = true;
        }
    }

    @Override
    public String toString() {
        return "Solution {cost = " + this.getCost() +
                ", ringSize = " + this.ringSize() +
                ", size = " + GRASP.SIZE + "}" ;
    }

    public int compareTo(Solution solution) {
        return Integer.compare(this.getCost(), solution.getCost());
    }

    private void calculateCost() {
        this.cost = 0;

        for (int i = 0; i < ring.size(); i++) {
            this.cost += GRASP.getRingCost()[ring.get(i) - 1][ring.get((i + 1) % ring.size()) - 1];
        }

        ArrayList<Integer[]> starSolution = getStar();
        for (Integer[] i : starSolution) {
            this.cost += GRASP.getStarCost()[i[0] - 1][i[1] - 1];
            // System.out.println("Star cost ("+i[0]+","+i[1]+"): " + starCost[i[0]-1][i[1]-1]);
        }
    }

    private void calculateStarSolution() {
        boolean[] ringNodes = new boolean[GRASP.SIZE];
        for (Integer integer : ring) {
            ringNodes[integer - 1] = true;
        }
        ArrayList<Integer[]> res = new ArrayList<>();
        for (int i = 0; i < GRASP.SIZE; i++) {
            if (!ringNodes[i]) {
                boolean found = false;
                int j = 0;
                while (!found) {
                    if (ringNodes[GRASP.getStarOrdered().get(i).get(j).getA()]) {
                        found = true;
                    } else {
                        j++;
                    }
                }
                res.add(new Integer[]{i + 1, GRASP.getStarOrdered().get(i).get(j).getA() + 1});
            }
        }
        this.star = res;
    }

    public int getCost() {
        if (cost == -1) {
            calculateCost();
        }
        return cost;
    }

    public int getRingEdgeCost(int nodeA, int nodeB) {
        return GRASP.getRingCost()[nodeA - 1][nodeB - 1];
    }

    public boolean isNodeRing(int node) {
        return isRing[node - 1];
    }

    public void setRing(ArrayList<Integer> ring) {
        this.ring = ring;
        for (Integer node : ring) {
            isRing[node - 1] = true;
        }
        this.cost = -1;
    }

    public ArrayList<Integer> getRing() {
        return ring;
    }

    public int ringSize() {
        return ring.size();
    }

    public boolean[] getIsRing() {
        return isRing;
    }

    public ArrayList<Integer[]> getStar() {
        if (this.star == null) {
            calculateStarSolution();
        }
        return star;
    }

    //-----------------------------------------------------------------------------------------//
    //NeighbourHood methods
    public Solution[] addNodeNeighbourhood() {
        Solution[] neighbourhood = new Solution[GRASP.SIZE - ring.size()];
        int k = 0;
        for (int i = 1; i <= GRASP.SIZE; i++) {

            if (!this.isNodeRing(i)) {
                // default best is the position before the first node
                int bestCost = this.getRingEdgeCost(i, ring.get(0));
                int bestIndex = 0;
                // check each position in the ring to find the least expensive
                for (int j = 0; j < ring.size(); j++) {
                    final int cost = this.getRingEdgeCost(ring.get(j), i);
                    if (cost < bestCost) {
                        bestCost = cost;
                        bestIndex = j + 1;
                    }
                }
                Solution neighbour = new Solution(this, GRASP);
                neighbour.addNodeToRing(i, bestIndex);
                neighbourhood[k] = neighbour;
                k++;
            }

        }
        return neighbourhood;
    }

    public Solution[] removeNodeNeighbourhood() {
        Solution[] neighbourhood = new Solution[ring.size()];
        for (int i = 0; i < ring.size(); i++) {
            Solution neighbour = new Solution(this, GRASP);
            neighbour.removeRingNode(i);
            neighbourhood[i] = neighbour;
        }
        return neighbourhood;
    }

    public Solution[] swapNodeNeighbourhood() {
        Solution[] neighbourhood = new Solution[ring.size()];
        for (int i = 0; i < ring.size(); i++) {
            Solution neighbour = new Solution(this, GRASP);
            neighbour.swapRingNode(i);
            neighbourhood[i] = neighbour;
        }
        return neighbourhood;
        //TODO fix redundant solution problem
    }

    private void addNodeToRing(int node, int index) {
        if (index == 0) {
            this.ring.add(0, node);
        }
        this.ring.add(node);
        this.isRing[node - 1] = true;
    }

    private void removeRingNode(int index) {
        this.isRing[this.ring.get(index) - 1] = false;
        this.ring.remove(index);
    }

    private void swapRingNode(int index) {
        if (index == 0) {
            int temp = this.ring.get(this.ring.size() - 1);
            this.ring.set(this.ring.size() - 1, this.ring.get(0));
            this.ring.set(0, temp);
        } else {
            int temp = this.ring.get(index - 1);
            this.ring.set(index - 1, this.ring.get(index));
            this.ring.set(index, temp);
        }
    }
    //TODO Make sure the starList doesn't get outdated
}
