package org.example.simulated_annealing;

import org.example.Solution;
import org.example.Tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.Main.*;

public class SimulatedAnnealingSolver {
    private double energy;
    private double temperature = 0.95;

    public static Solution solve() {
        ArrayList<Integer> ring = new ArrayList<>(List.of(1));
        ArrayList<Integer[]> star;

        // Randomly put nodes in the ring or the star (50%)
        for (int i = 2; i <= size; i++) {
            if (Math.random() > 0.5) {
                ring.add(i);
            }
        }

        // Build the star by adding couples (node, closest ring node)
        ArrayList<ArrayList<Tuple>> starOrdered = setupStarOrdered(starCost, ringCost.length);
        star = getStarSolution(starOrdered, ring, ringCost.length);

        return new Solution(ring, star);
    }
}
