package grasp;

import org.example.Solution;

import java.util.ArrayList;

public class Grasp {
    private final float ALPHA;
    private final int [][] ringCostMatrix;

    private final int [][] starCostMatrix;

    public Grasp(float alpha, int[][] ringCostMatrix, int[][] starCostMatrix) {
        this.ALPHA = alpha;
        this.ringCostMatrix = ringCostMatrix;
        this.starCostMatrix = starCostMatrix;
    }

    public Solution solve() {
        return null;
    }

    private Solution constructSolution(){
        // construction in 2 part(ring and star)
        return null;
    }

    private Solution localSearch() {
        return null;
    }
}
