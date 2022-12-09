package org.example;

public class Solution {
    private int[] ring;
    private int[][] star;
    public Solution(int[] ring, int[][] star) {
        this.ring = ring;
        this.star = star;
    }

    public int cost() {
        return 0;
    }

    public int[] getRing() {
        return ring;
    }

    public void setRing(int[] ring) {
        this.ring = ring;
    }

    public int[][] getStar() {
        return star;
    }

    public void setStar(int[][] star) {
        this.star = star;
    }
}
