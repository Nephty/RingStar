package org.example;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Solution {
    private ArrayList<Integer> ring;
    private ArrayList<Integer[]> star;
    public Solution(ArrayList<Integer> ring, ArrayList<Integer[]> star) {
        this.ring = ring;
        this.star = star;
    }

    public int cost() {
        return 0;
    }

    public void switchStarNode() {
        final int starNodeToSwitch = ThreadLocalRandom.current().nextInt(0, star.size() + 1);
        final Integer[] starEdge = star.get(starNodeToSwitch);
        int newRingNode;
        do {
            int randomNum = ThreadLocalRandom.current().nextInt(0, ring.size() + 1);
            newRingNode = ring.get(randomNum);
        }while (newRingNode == starEdge[1]);
        starEdge[1] = newRingNode;
        star.set(starNodeToSwitch, starEdge);
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
