package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] starCost = {{0, 4, 8, 4, 8, 12, 8, 12, 16},
                {4, 0, 1, 8, 4, 1, 12, 8, 12},
                {8, 1, 0, 12, 8, 4, 16, 12, 8},
                {4, 8, 12, 0, 4, 8, 1, 8, 12},
                {8, 4, 8, 4, 0, 4, 8, 1, 1},
                {12, 1, 4, 8, 4, 0, 12, 8, 4},
                {8, 12, 16, 1, 8, 12, 0, 4, 8},
                {12, 8, 12, 8, 1, 8, 4, 0, 4},
                {16, 12, 8, 12, 1, 4, 8, 4, 0}};

        int[][] ringCost = {{0, 1, 6, 1, 2, 9, 6, 9, 12},
                {1, 0, 3, 2, 1, 3, 9, 6, 9},
                {6, 3, 0, 9, 6, 3, 12, 9, 6},
                {1, 2, 9, 0, 1, 6, 3, 6, 9},
                {2, 1, 6, 1, 0, 3, 6, 3, 6},
                {9, 3, 3, 6, 3, 0, 9, 6, 3},
                {6, 9, 12, 3, 6, 9, 0, 1, 6},
                {9, 6, 9, 6, 3, 6, 1, 0, 3},
                {12, 9, 6, 9, 6, 3, 6, 3, 0}};

        int size = 9;
        int[] ring = {4, 5, 2, 1};

        ArrayList<ArrayList<Tuple>> starOrdered = setupStarOrdered(starCost, size);

        ArrayList<Integer[]> res = getStarSolution(starOrdered, ring, size);
        for (Integer[] i : res) {
            System.out.println(Arrays.toString(i));
        }

        System.out.println("Total cost: " + calculateSolution(ringCost, starCost, ring, starOrdered, size)
                + ". Expected: 9");
    }


    /**
     * Calcule le coût d'une solution en faisant <br>
     * Somme des coûts des chemins entre les noeuds du ring  + somme des chemins entre les noeuds du star.
     *
     * @param ringCost Matrice des côuts pour laler de i à j dans le ring
     * @param starCost Matrice des coûts pour aller de i à j dans star
     * @param ring     Noeuds du ring
     * @param size     Taille du problème
     * @return Le coùt de la solution
     */
    public static int calculateSolution(int[][] ringCost, int[][] starCost, int[] ring,
                                        ArrayList<ArrayList<Tuple>> starOrdered, int size) {
        int cost = 0;


        for (int i = 0; i < ring.length; i++) {
            cost += ringCost[ring[i] - 1][ring[(i + 1) % ring.length] - 1];
            // System.out.println("Ring cost ("+i+","+(i + 1) % ring.length+"): "
            //       + ringCost[ring[i]-1][ring[(i + 1) % ring.length]-1]);
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
     * En gros, il va prendre chaque noeud du star, parcourir sa colonne de la matrice et la ligne où sera le minimum
     * sera le meilleur depot pour lui. <br>
     * <p>
     * Pour le moment c'est fait de façon naïve, il faudra optimiser ça.
     *
     * @param starCost la matrice des couts pour aller de i à j dans le star. avec les lignes triées en fonctions des coùts croissants
     *                 (pour trouver plus rapidement le minimum).
     * @param ring     Les noeuds du ring.
     */
    public static ArrayList<Integer[]> getStarSolution(ArrayList<ArrayList<Tuple>> starCost, int[] ring, int size) {
        boolean[] ringNodes = new boolean[size];
        for (int i = 0; i < ring.length; i++) {
            ringNodes[ring[i] - 1] = true;
        }
        ArrayList<Integer[]> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!ringNodes[i]) {
                boolean found = false;
                int j = 0;
                while (!found) {
                    if (ringNodes[starCost.get(i).get(j).getIndex()]) {
                        found = true;
                    } else {
                        j++;
                    }
                }
                res.add(new Integer[]{i + 1, starCost.get(i).get(j).getIndex() + 1});
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
    public static ArrayList<ArrayList<Tuple>> setupStarOrdered(int[][] starCost, int size) {
        ArrayList<ArrayList<Tuple>> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ArrayList<Tuple> tmp = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                tmp.add(new Tuple(j, starCost[i][j]));
            }
            tmp.sort(Tuple::compareTo);
            res.add(tmp);
        }
        return res;
    }
}