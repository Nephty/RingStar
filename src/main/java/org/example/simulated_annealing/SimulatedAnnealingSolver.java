package org.example.simulated_annealing;

import org.example.Solution;
import org.example.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.Main.getStarSolution;
import static org.example.Main.setupStarOrdered;

public class SimulatedAnnealingSolver {
    public static Solution solve(int nodesCount, ArrayList<ArrayList<Integer>> ringCosts, ArrayList<ArrayList<Integer>> starCosts) {
        ArrayList<Integer> ring = new ArrayList<>(List.of(1));
        ArrayList<Integer[]> star;

        // Randomly put nodes in the ring or the star (50%)
        for (int i = 2; i <= nodesCount; i++) {
            if (Math.random() > 0.5) {
                ring.add(i);
            }
        }

        // Build the star by adding couples (node, closest ring node)
        ArrayList<ArrayList<Tuple>> starOrdered = setupStarOrdered(starCosts, ringCosts.size());
        star = getStarSolution(starOrdered, ring, ringCosts.size());

        return new Solution(ring, star);
    }
}
