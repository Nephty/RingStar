package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    private ArrayList<Integer> ring;

    private ArrayList<Integer[]> star;

    private final boolean[] isRing = new boolean[Main.size];
    private int cost = -1;
    public Solution(ArrayList<Integer> ring) {
        this.ring = ring;
        for (Integer node : ring) {
            isRing[node] = true;
        }
    }

    public Solution() {
        this.ring = new ArrayList<>(List.of(1));
        isRing[0] = true;
        this.cost = Main.calculateSolution(ring);
    }

    public Solution(Solution solution) {
        this.ring = new ArrayList<>(solution.getRing());
        for (Integer node : ring) {
            isRing[node] = true;
        }
    }

    private void calculateCost() {
        this.cost = 0;

        for (int i = 0; i < ring.size(); i++) {
            this.cost += Main.ringCost[ring.get(i) - 1][ring.get((i + 1) % ring.size()) - 1];
        }

        ArrayList<Integer[]> starSolution = getStar();
        for (Integer[] i : starSolution) {
            this.cost += Main.starCost[i[0] - 1][i[1] - 1];
            // System.out.println("Star cost ("+i[0]+","+i[1]+"): " + starCost[i[0]-1][i[1]-1]);
        }
    }
    private void calculateStarSolution() {
        boolean[] ringNodes = new boolean[Main.size];
        for (int i = 0; i < ring.size(); i++) {
            ringNodes[ring.get(i) - 1] = true;
        }
        ArrayList<Integer[]> res = new ArrayList<>();
        for (int i = 0; i < Main.size; i++) {
            if (!ringNodes[i]) {
                boolean found = false;
                int j = 0;
                while (!found) {
                    if (ringNodes[Main.starOrdered.get(i).get(j).getIndex()]) {
                        found = true;
                    } else {
                        j++;
                    }
                }
                res.add(new Integer[]{i + 1, Main.starOrdered.get(i).get(j).getIndex() + 1});
            }
        }
        this.star = res;
    }

    public int getCost() {
        if(cost == -1) {
            calculateCost();
        }
        return cost;
    }

    public int getRingEdgeCost(int nodeA, int nodeB) {
        return Main.ringCost[nodeA - 1][nodeB - 1];
    }

    public void addNodeToRing(int node, int index) {
        if(index == 0) {
            this.ring.add(node, 0);
        }
        this.ring.add(node);
        this.isRing[node] = true;
    }

    public void setRing(ArrayList<Integer> ring) {
        this.ring = ring;
        for (Integer node : ring) {
            isRing[node] = true;
        }
        this.cost = Main.calculateSolution(ring);
    }

    public ArrayList<Integer> getRing() {
        return ring;
    }

    public boolean[] getIsRing() {
        return isRing;
    }

    public ArrayList<Integer[]> getStar() {
        if(this.star == null){
            calculateStarSolution();
        }
        return star;
    }
    //TODO Make sure the starList doesn't get outdated
}
