package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    private ArrayList<Integer> ring;

    private int ringSize;

    private boolean[] ring_bool;
    private ArrayList<Integer[]> star; // Utile ?
    private int cost;
    public Solution(ArrayList<Integer> ring) {
        this.ring = ring;
        // this.star = star;

        this.cost = Main.calculateSolution(ring);
    }

    public Solution() {
        this.ring = new ArrayList<>(List.of(1));
        this.ringSize = ring.size();

        setRing_bool();

        this.cost = Main.calculateSolution(ring);
    }

    public void setRing_bool(){
        ring_bool = new boolean[Main.size];
        for(int i : ring){
            ring_bool[i] = true;
        }
    }

    public int cost() {
        return cost;
    }

    public ArrayList<Integer> getRing() {
        return ring;
    }

    public void setRing(ArrayList<Integer> ring) {
        this.ring = ring;
        this.ringSize = ring.size();
        this.cost = Main.calculateSolution(ring);
    }

    public ArrayList<Integer[]> getStar() {
        return star;
    }

    public void setStar(ArrayList<Integer[]> star) {
        this.star = star;
    }

    public boolean[] getRing_bool() {
        return ring_bool;
    }

    public int getRingSize() {
        return ringSize;
    }
}
