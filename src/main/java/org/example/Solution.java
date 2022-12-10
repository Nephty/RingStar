package org.example;

import java.util.ArrayList;

public class Solution {
    private ArrayList<Integer> ring;
    private ArrayList<Integer[]> star;
    private float[][] visibility;
    private float[][] pheromone;
    private int cost;

    public Solution(ArrayList<Integer> ring, ArrayList<Integer[]> star) {
        this.ring = ring;
        this.star = star;
    }

    public Solution(ArrayList<Integer> ring, ArrayList<Integer[]> star, float[][] visibility, float[][] pheromone, int cost) {
        this.ring = ring;
        this.star = star;
        this.visibility = visibility;
        this.pheromone = pheromone;
        this.cost = cost;

    }
    public ArrayList<Integer> getRing(){
        return this.ring;
    }

    public void setRing(ArrayList<Integer> ring) {
        this.ring = ring;
    }

    public ArrayList<Integer[]> getStar() {
        return this.star;
    }

    public void setStar(ArrayList<Integer[]> star) {
        this.star = star;
    }
}
