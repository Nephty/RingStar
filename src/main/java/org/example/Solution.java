package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    private ArrayList<Integer> ring;
    private ArrayList<Integer[]> star; // Utile ?

    private int cost;
    public Solution(ArrayList<Integer> ring) {
        this.ring = ring;
        // this.star = star;

        this.cost = Main.calculateSolution(ring);
    }

    public Solution() {
        this.ring = new ArrayList<>(List.of(1));

        this.cost = Main.calculateSolution(ring);
    }

    public int cost() {
        return cost;
    }

    public ArrayList<Integer> getRing() {
        return ring;
    }

    public void setRing(ArrayList<Integer> ring) {
        this.ring = ring;
        this.cost = Main.calculateSolution(ring);
    }

    public ArrayList<Integer[]> getStar() {
        return star;
    }

    public void setStar(ArrayList<Integer[]> star) {
        this.star = star;
    }

}
