package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[][] starCost = { {0, 4, 8, 4, 8, 12, 8, 12, 16},
                             {4, 0, 1, 8, 4, 1, 12, 8, 12},
                             {8, 1, 0, 12, 8, 4, 16, 12, 8},
                             {4, 8, 12, 0, 4, 8, 1, 8, 12},
                             {8, 4, 8, 4, 0, 4, 8, 1, 1},
                             {12, 1, 4, 8, 4, 0, 12, 8, 4},
                             {8, 12, 16, 1, 8, 12, 0, 4, 8},
                             {12, 8, 12, 8, 1, 8, 4, 0, 4},
                             {16, 12, 8, 12, 1, 4, 8, 4, 0} };
        
        int size = 9;
        int[] ring = {4, 5, 2, 1};

        ArrayList<Integer[]> res = getStarSolution(starCost, ring, size);
        for(Integer[] i : res){
            System.out.println(Arrays.toString(i));
        }
    }


    /**
     * Calcule le coût d'une solution en faisant <br>
     * Somme des coûts des chemins entre les noeuds du ring  + somme des chemins entre les noeuds du star.
     * @param ringCost Matrice des côuts pour laler de i à j dans le ring
     * @param starCost Matrice des coûts pour aller de i à j dans star
     * @param ring Noeuds du ring
     * @param size Taille du problème
     * @return Le coùt de la solution
     */
    public static int calculateSolution(int[][] ringCost, int[][] starCost, int[] ring, int size){
        // TODO : Implémenter la fonction
        return 0;
    }


    /**
     * Calcule le meilleur dépot pour chaque noeud du star. <br>
     * En gros, il va prendre chaque noeud du star, parcourir sa colonne de la matrice et la ligne où sera le minimum
     * sera le meilleur depot pour lui. <br>
     *
     * Pour le moment c'est fait de façon naïve, il faudra optimiser ça.
     *
     * @param starCost la matrice des couts pour aller de i à j dans le star.
     * @param ring Les noeuds du ring.
     */
    public static ArrayList<Integer[]> getStarSolution(int[][] starCost, int[] ring, int size) {
        boolean[] ringNodes = new boolean[size];
        for(int i=0; i<ring.length; i++){
            ringNodes[ring[i]-1] = true;
        }

        ArrayList<Integer[]> res = new ArrayList<>(); // Tableau de tuple (i, j) où i est le noeud du star et j le meilleur depot pour i.
        for(int i=1;i<=size;i++){
            if(!ringNodes[i-1]){ // Si le noeud n'est pas dans le ring
                int min = Integer.MAX_VALUE;
                int min_index = 0;
                for(int j=1;j<=size;j++){ // On va itérer sur la colonne du noeud i
                    if(starCost[i-1][j-1] < min && ringNodes[j-1]){ // Si le cout est plus petit que le minimum et que le noeud j est dans le ring
                        min = starCost[i-1][j-1];
                        min_index = j;
                    }
                }
                res.add(new Integer[]{i, min_index});
            }
        }
        return res;
    }


    /**
     * Méthode qui va créer un nouveau tableau de tuple qui sont les couts des chemins d'un noeud i vers un noeud j dans le star.
     * Les lignes seront ordonnées afin de pouvoir itérer dessus plus facilement.
     * @param starCost La matrice des couts pour aller de i à j dans le star.
     * @param size La taille du star.
     * @return Un tableau de tuple (i, j) où i est le noeud du star, j le noeud du ring
     */
    public static int[][] setupStarOrdered(int[][] starCost,int size) {
        // TODO : A faire
        return null;
    }
}