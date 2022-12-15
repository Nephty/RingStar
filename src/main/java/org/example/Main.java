package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    // On met certaines variables en static pour pouvoir les utiliser dans les autres classes
    static int[][] starCost; // Matrice des coûts pour aller de i à j dans star
    static int[][] ringCost; // Matrice des côuts pour aller de i à j dans le ring
    static int size; // Taille du problème
    static ArrayList<ArrayList<Tuple<Integer>>> starOrdered; // matrice 2D de tuple (value, j) qui sont les couts des

    // chemins d'un noeud i vers un noeud j de cout value
    public static void main(String[] args) {
        starCost = new int[][]{{0, 4, 8, 4, 8, 12, 8, 12, 16},
                {4, 0, 1, 8, 4, 1, 12, 8, 12},
                {8, 1, 0, 12, 8, 4, 16, 12, 8},
                {4, 8, 12, 0, 4, 8, 1, 8, 12},
                {8, 4, 8, 4, 0, 4, 8, 1, 1},
                {12, 1, 4, 8, 4, 0, 12, 8, 4},
                {8, 12, 16, 1, 8, 12, 0, 4, 8},
                {12, 8, 12, 8, 1, 8, 4, 0, 4},
                {16, 12, 8, 12, 1, 4, 8, 4, 0}};

        ringCost = new int[][]{{0, 1, 6, 1, 2, 9, 6, 9, 12},
                {1, 0, 3, 2, 1, 3, 9, 6, 9},
                {6, 3, 0, 9, 6, 3, 12, 9, 6},
                {1, 2, 9, 0, 1, 6, 3, 6, 9},
                {2, 1, 6, 1, 0, 3, 6, 3, 6},
                {9, 3, 3, 6, 3, 0, 9, 6, 3},
                {6, 9, 12, 3, 6, 9, 0, 1, 6},
                {9, 6, 9, 6, 3, 6, 1, 0, 3},
                {12, 9, 6, 9, 6, 3, 6, 3, 0}};

        size = 9;
        starOrdered = setupStarOrdered(starCost, size);

        ArrayList<Integer> ring = new ArrayList<>(Arrays.asList(4, 5, 2, 1));
        ArrayList<Integer[]> res = getStarSolution(starOrdered, ring, size);
        for (Integer[] i : res) {
            System.out.println(Arrays.toString(i));
        }

        System.out.println("Total cost: " + calculateSolution(ring)
                + ". Expected: 9");

        Grasp grasp = new Grasp(50, 0.6, ringCost, starCost, size);
        System.out.println(Arrays.toString(grasp.minIncrement(7, new Solution(ring))));

    }


    /**
     * Calcule le coût d'une solution en faisan la somme des coûts des chemins entre les noeuds du ring
     * + somme des chemins entre les noeuds du star.
     *
     * @param ring Noeuds du ring
     * @return Le coùt de la solution
     */
    public static int calculateSolution(ArrayList<Integer> ring) {
        int cost = 0;

        for (int i = 0; i < ring.size(); i++) {
            cost += ringCost[ring.get(i) - 1][ring.get((i + 1) % ring.size()) - 1];
        }

        ArrayList<Integer[]> starSolution = getStarSolution(starOrdered, ring, size);
        for (Integer[] i : starSolution) {
            cost += starCost[i[0] - 1][i[1] - 1];
            // System.out.println("Star cost ("+i[0]+","+i[1]+"): " + starCost[i[0]-1][i[1]-1]);
        }

        return cost;
    }


    /**
     * Calcule le meilleur dépot pour chaque noeud du star. <br>
     * En gros, il va prendre chaque noeud du star, va chercher sa ligne dans starCostOrdered
     * et va chercher le premier noeud du ring qui est dans cette ligne qui sera le meilleur dépot car la liste est triée.
     *
     * @param starCostOrdered la matrice des couts pour aller de i à j dans le star. avec les lignes triées en fonctions des coùts croissants
     *                        (pour trouver plus rapidement le minimum).
     * @param ring            Les noeuds du ring.
     * @param size            La taille du problème.
     * @return Une liste de tuple (i, j) qui sont les noeuds du star et leur meilleur dépot.
     */
    public static ArrayList<Integer[]> getStarSolution(ArrayList<ArrayList<Tuple<Integer>>> starCostOrdered, ArrayList<Integer> ring, int size) {
        boolean[] ringNodes = new boolean[size];
        for (Integer integer : ring) {
            ringNodes[integer - 1] = true;
        }
        ArrayList<Integer[]> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!ringNodes[i]) {
                boolean found = false;
                int j = 0;
                while (!found) {
                    if (ringNodes[starCostOrdered.get(i).get(j).getA()]) {
                        found = true;
                    } else {
                        j++;
                    }
                }
                res.add(new Integer[]{i + 1, starCostOrdered.get(i).get(j).getA() + 1});
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
     * @return Une matrice 2D de tuple (a, b) qui sont les couts des chemins d'un noeud i vers un noeud j. a est le noeud j et b est le coût.
     */
    public static ArrayList<ArrayList<Tuple<Integer>>> setupStarOrdered(int[][] starCost, int size) {
        ArrayList<ArrayList<Tuple<Integer>>> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Tuple<Integer>> tmp = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                tmp.add(new Tuple<>(j, starCost[i][j]));
            }
            tmp.sort(Tuple::compareTo);
            res.add(tmp);
        }
        return res;
    }
}