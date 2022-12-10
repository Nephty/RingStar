package org.example;

import java.util.ArrayList;

public class Solution {
    private ArrayList<Integer> ring;
    private ArrayList<Integer[]> star;
    public Solution(ArrayList<Integer> ring, ArrayList<Integer[]> star) {
        this.ring = ring;
        this.star = star;
    }

    public int getCost(Solution solution, int[][] ringCosts, int[][] starCosts) {
        int i = 0;
        int totalCost = 0;
        ArrayList<Integer> ring = solution.getRing();
        ArrayList<Integer[]> star = solution.getStar();

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

    public ArrayList<Integer> getRing() {
        return ring;
    }

    public void setRing(ArrayList<Integer> ring) {
        this.ring = ring;
    }

    public ArrayList<Integer[]> getStar() {
        return star;
    }

    public void setStar(ArrayList<Integer[]> star) {
        this.star = star;
    }
}
