package org.example.simulated_annealing;

import org.example.Solution;
import org.example.Tuple;

import java.util.ArrayList;
import java.util.List;

import static org.example.Main.*;

public class SimulatedAnnealingSolver {
    private static double energy = 1;
    private static double initialTemperature = 100;
    private static double finalTemperature = 0.01;
    private static int nIters = 1000;
    private static double deltaEnergy = 1F / nIters;

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

        Solution previousSolution = new Solution(ring, star);
        Solution currentSolution;

        ArrayList<Integer> costHistory = new ArrayList<>();
        System.out.println("Coût de la solution de départ : " + previousSolution.cost());

        for (int i = 0; i < nIters; i++) {
            currentSolution = previousSolution.randomMovement();
            // TODO : OPTIMISATION : change previous.cost to a var since we will call it many times
            float costDifference = currentSolution.cost() - previousSolution.cost();

            if (costDifference < 0) {
                previousSolution = new Solution(currentSolution.ring, currentSolution.star);
            } else {
                if (Math.random() > Math.exp(-costDifference / initialTemperature)) {
                    previousSolution = new Solution(currentSolution.ring, currentSolution.star);
                }
            }
            costHistory.add(previousSolution.cost());
            System.out.println("initialTemperature = " + initialTemperature);
            System.out.println("finalTemperature = " + finalTemperature);
            System.out.println();
            if (initialTemperature > finalTemperature) {
                initialTemperature *= 0.9F;
            }
            energy -= deltaEnergy;
        }
        System.out.println(costHistory);
        System.out.println("Coût de la solution finale : " + previousSolution.cost());

        return new Solution(ring, star);
    }
}
