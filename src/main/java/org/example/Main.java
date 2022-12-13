package org.example;

import org.example.simulated_annealing.SimulatedAnnealingSolver;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] starCostt = {{0, 4, 8, 4, 8, 12, 8, 12, 16},
                {4, 0, 1, 8, 4, 1, 12, 8, 12},
                {8, 1, 0, 12, 8, 4, 16, 12, 8},
                {4, 8, 12, 0, 4, 8, 1, 8, 12},
                {8, 4, 8, 4, 0, 4, 8, 1, 1},
                {12, 1, 4, 8, 4, 0, 12, 8, 4},
                {8, 12, 16, 1, 8, 12, 0, 4, 8},
                {12, 8, 12, 8, 1, 8, 4, 0, 4},
                {16, 12, 8, 12, 1, 4, 8, 4, 0}};

        int[][] ringCostt = {{0, 1, 6, 1, 2, 9, 6, 9, 12},
                {1, 0, 3, 2, 1, 3, 9, 6, 9},
                {6, 3, 0, 9, 6, 3, 12, 9, 6},
                {1, 2, 9, 0, 1, 6, 3, 6, 9},
                {2, 1, 6, 1, 0, 3, 6, 3, 6},
                {9, 3, 3, 6, 3, 0, 9, 6, 3},
                {6, 9, 12, 3, 6, 9, 0, 1, 6},
                {9, 6, 9, 6, 3, 6, 1, 0, 3},
                {12, 9, 6, 9, 6, 3, 6, 3, 0}};

        int size = 9;

        ArrayList<ArrayList<Integer>> starCosts = new ArrayList<>();
        starCosts.add(new ArrayList<>(Arrays.asList(0, 4, 8, 4, 8, 12, 8, 12, 16)));
        starCosts.add(new ArrayList<>(Arrays.asList(4, 0, 1, 8, 4, 1, 12, 8, 12)));
        starCosts.add(new ArrayList<>(Arrays.asList(8, 1, 0, 12, 8, 4, 16, 12, 8)));
        starCosts.add(new ArrayList<>(Arrays.asList(4, 8, 12, 0, 4, 8, 1, 8, 12)));
        starCosts.add(new ArrayList<>(Arrays.asList(8, 4, 8, 4, 0, 4, 8, 1, 1)));
        starCosts.add(new ArrayList<>(Arrays.asList(12, 1, 4, 8, 4, 0, 12, 8, 4)));
        starCosts.add(new ArrayList<>(Arrays.asList(8, 12, 16, 1, 8, 12, 0, 4, 8)));
        starCosts.add(new ArrayList<>(Arrays.asList(12, 8, 12, 8, 1, 8, 4, 0, 4)));
        starCosts.add(new ArrayList<>(Arrays.asList(16, 12, 8, 12, 1, 4, 8, 4, 0)));

        ArrayList<ArrayList<Integer>> ringCosts = new ArrayList<>();
        ringCosts.add(new ArrayList<>(Arrays.asList(0, 1, 6, 1, 2, 9, 6, 9, 12)));
        ringCosts.add(new ArrayList<>(Arrays.asList(1, 0, 3, 2, 1, 3, 9, 6, 9)));
        ringCosts.add(new ArrayList<>(Arrays.asList(6, 3, 0, 9, 6, 3, 12, 9, 6)));
        ringCosts.add(new ArrayList<>(Arrays.asList(1, 2, 9, 0, 1, 6, 3, 6, 9)));
        ringCosts.add(new ArrayList<>(Arrays.asList(2, 1, 6, 1, 0, 3, 6, 3, 6)));
        ringCosts.add(new ArrayList<>(Arrays.asList(9, 3, 3, 6, 3, 0, 9, 6, 3)));
        ringCosts.add(new ArrayList<>(Arrays.asList(6, 9, 12, 3, 6, 9, 0, 1, 6)));
        ringCosts.add(new ArrayList<>(Arrays.asList(9, 6, 9, 6, 3, 6, 1, 0, 3)));
        ringCosts.add(new ArrayList<>(Arrays.asList(12, 9, 6, 9, 6, 3, 6, 3, 0)));

        SimulatedAnnealingSolver.solve(size, ringCosts, starCosts);
    }


    /**
     * Calcule le meilleur dépot pour chaque noeud du star. <br>
     * En gros, il va prendre chaque noeud du star, va chercher sa ligne dans starCostOrdered
     * et va chercher le premier noeud du ring qui est dans cette ligne qui sera le meilleur dépot car la liste est triée.
     *
     * @param starCostOrdered la matrice des couts pour aller de i à j dans le star. avec les lignes triées en fonctions des coùts croissants
     *                 (pour trouver plus rapidement le minimum).
     * @param ring     Les noeuds du ring.
     * @param size     La taille du problème.
     * @return Une liste de tuple (i, j) qui sont les noeuds du star et leur meilleur dépot.
     */
    public static ArrayList<Integer[]> getStarSolution(ArrayList<ArrayList<Tuple>> starCostOrdered, ArrayList<Integer> ring, int size) {
        boolean[] ringNodes = new boolean[size];
        for (int i = 0; i < ring.size(); i++) {
            ringNodes[ring.get(i) - 1] = true;
        }
        ArrayList<Integer[]> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!ringNodes[i]) {
                boolean found = false;
                int j = 0;
                while (!found) {
                    if (ringNodes[starCostOrdered.get(i).get(j).getIndex()]) {
                        found = true;
                    } else {
                        j++;
                    }
                }
                res.add(new Integer[]{i + 1, starCostOrdered.get(i).get(j).getIndex() + 1});
            }
        }
        return res;
    }


    /**
     * Méthode qui va créer un nouveau tableau de tuple qui sont les couts des chemins d'un noeud i vers un noeud j dans le star.
     * Les lignes seront ordonnées afin de pouvoir itérer dessus plus facilement. <br>
     * <p>
     * Les matrices sont symétriques donc on la transposée reste la même (inverse iigne et colonne) on peut donc itérer sur les lignes.
     *
     * @param starCost La matrice des couts pour aller de i à j dans le star.
     * @param size     La taille du star.
     * @return Une matrice 2D de tuple (value, j) qui sont les couts des chemins d'un noeud i vers un noeud j de cout value
     */
    public static ArrayList<ArrayList<Tuple>> setupStarOrdered(ArrayList<ArrayList<Integer>> starCost, int size) {
        ArrayList<ArrayList<Tuple>> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Tuple> tmp = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                tmp.add(new Tuple(j, starCost.get(i).get(j)));
            }
            tmp.sort(Tuple::compareTo);
            res.add(tmp);
        }
        return res;
    }
}