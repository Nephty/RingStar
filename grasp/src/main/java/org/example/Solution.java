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

    public Solution(Solution solution) {
        this.GRASP = solution.GRASP;
        this.ring = new ArrayList<>(solution.getRing());
        this.isRing = new boolean[GRASP.SIZE];
        for (Integer node : ring) {
            isRing[node - 1] = true;
        }
    }

    @Override
    public String toString() {
        StringBuilder BobTheBuilder = new StringBuilder();
        BobTheBuilder.append("Ring: ").append(ringSize()).append("\n");
        for(Integer node : ring) {
            BobTheBuilder.append(node).append(" ");
        }
        BobTheBuilder.append("\nStar: ").append("\n");
        for (Integer[] node : star) {
            BobTheBuilder.append(node[0]).append(" ").append(node[1]).append("\n");
        }
        BobTheBuilder.append("Cost: ").append(getCost());
        return BobTheBuilder.toString();
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

    /**
     * For a node n give the cost in the ring of (n-1 to n) + (n to n + 1)
     * @param node A node nothing to say
     * @return Already said above
     */
    private int getAdjacentEdgesCostSum(int previousNode, int node, int nextNode) {
        // fromCost + toCost
        // toCost : when node - 1 == 0 then preceding node is last node in the list ie ringSize
        // Note: node != index -> node = index + 1
        return getRingEdgeCost(previousNode, node) + getRingEdgeCost(node, nextNode);
    }

    public boolean isStarNode(int node) {
        return !isRing[node - 1];
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
        for (int node = 1; node <= GRASP.SIZE; node++) {
            if (isStarNode(node)) {
                //default cost if inserted at last index
                int bestCost = this.getAdjacentEdgesCostSum(ring.get(ringSize() - 2), node, ring.get(0));
                int bestIndex = ringSize() - 1;
                // check each position in the ring to find the least expensive
                for (int j = 0; j < ring.size() - 1; j++) {
                    final int cost = this.getAdjacentEdgesCostSum(
                            j == 0 ? ring.get(ringSize() - 1) : ring.get(j - 1),
                            node,
                            ring.get(j + 1)
                    );
                    if (cost < bestCost) {
                        bestCost = cost;
                        bestIndex = j;
                    }
                }
                Solution neighbour = new Solution(this);
                neighbour.addNodeToRing(node, bestIndex);
                neighbourhood[k] = neighbour;
                k++;
            }

        }
        return neighbourhood;
    }

    public Solution[] swapStarRingNodeNeighbourhood() {
        Solution[] neighbourhood = new Solution[GRASP.SIZE - ring.size()];
        int k = 0;
        for (int node = 1; node <= GRASP.SIZE; node++) {
            if (isStarNode(node)) {
                //default cost if inserted at last index
                int bestCostGain = this.getAdjacentEdgesCostSum(ring.get(ringSize() - 2), node, ring.get(0)) -
                        this.getAdjacentEdgesCostSum(ring.get(ringSize() - 2), ring.get(ringSize() - 1), ring.get(0));
                int bestIndex = ringSize() - 1;
                // check each position in the ring to find the least expensive

                for (int i = 0; i < ringSize() - 1; i++) {
                    final int costGain = getAdjacentEdgesCostSum(
                            i == 0  ? ring.get(ringSize() - 1) : ring.get(i - 1),
                            node,
                            ring.get(i + 1)
                    ) - getAdjacentEdgesCostSum(
                            i == 0 ? ring.get(ringSize() - 1) : ring.get(i - 1),
                            ring.get(i),
                            ring.get(i + 1)
                    );
                    if(costGain < bestCostGain) {
                        bestCostGain = costGain;
                        bestIndex = i;
                    }
                }
                Solution neighbour = new Solution(this);
                neighbour.swapNodeToRing(node, bestIndex);

                neighbourhood[k] = neighbour;
                k++;
            }
        }

        return neighbourhood;
    }

    public Solution[] removeNodeNeighbourhood() {
        Solution[] neighbourhood = new Solution[ring.size()];
        for (int i = 0; i < ring.size(); i++) {
            Solution neighbour = new Solution(this);
            neighbour.removeRingNode(i);
            neighbourhood[i] = neighbour;
        }
        return neighbourhood;
    }

    public Solution[] swapRingNodeNeighbourhood() {
        Solution[] neighbourhood = new Solution[ring.size()];
        for (int i = 0; i < ring.size(); i++) {
            Solution neighbour = new Solution(this);
            neighbour.swapRingNode(i);
            neighbourhood[i] = neighbour;
        }
        return neighbourhood;
        //TODO fix redundant solution problem
    }

    public void addNodeToRing(int node, int index) {
        this.ring.add(index, node);
        this.isRing[node - 1] = true;
        this.cost = -1;
    }

    private void swapNodeToRing(int node, int index) {
        this.isRing[ring.get(index) - 1] = false;
        this.isRing[node - 1] = true;
        this.ring.set(index, node);
    }

    private void removeRingNode(int index) {
        this.isRing[this.ring.get(index) - 1] = false;
        this.ring.remove(index);
        this.cost = -1;
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
        this.cost = -1;
    }
    //TODO Make sure the starList doesn't get outdated
}
