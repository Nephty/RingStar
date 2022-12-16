package org.example;

import org.example.Solution;
import org.example.Tuple;
import java.util.Date;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.example.Maint_methode.*;

public class TabuSolver {

    private static int max_nomove_iter = 50; //Nombre d'iteration a faire pour arreter la boucle (si on a k nombre de fois le meme résultat a la suite)
    private static int time_limit = 500000; // 500000ms
    private static int tabu_list_size = 10; // taille de la liste tabou (+ grand -> prend plus de temps mais peu etre plus ^précis, il faut adapter)
    private static int iter_number = 1000; // nombre d'iteration totale avant d'arreter l'algo

    //regarde si la solution se trouve dans la liste tabou
    public static boolean check_in_tabu(LinkedList<Solution> tab_list, Solution sol){

        for(int i=0;i<tab_list.size();i++){
            if(tab_list.get(i) == sol){
                return true;
            }
        }
        return false;
    }

    // solveur de solution va retourner le meilleur résultat
    public static Solution solve(int[][] ringCost, int[][] starCost) {
        LinkedList<Solution> tabuList=  new LinkedList<Solution>();
        ArrayList<Integer> ring = new ArrayList<>(List.of(1));
        ArrayList<Integer[]> star;

        // Randomly put nodes in the ring or the star (50%)
        for (int i = 2; i <= ring.size(); i++) {
            if (Math.random() > 0.5) {
                ring.add(i);
            }
        }
        // Build the star by adding couples (node, closest ring node)
        ArrayList<ArrayList<Tuple>> starOrdered = setupStarOrdered(starCost, ringCost.length);
        star = getStarSolution(starOrdered, ring, ringCost.length);

        Solution previousSolution = new Solution(ring, star);
        Solution currentSolution;
        Solution bestSolution = previousSolution;



        ArrayList<Integer> costHistory = new ArrayList<>();
        System.out.println("Coût de la solution de départ : " + previousSolution.cost());

        tabuList.add(previousSolution);

        Date date = new Date();
        int time_check = (int) date.getTime();
        int time_check2 = (int) date.getTime();
        int nomove_iter = 0;
        int iter_check = 0;

        if (tabuList.size() != tabu_list_size){

            while(time_check!= time_check2 + time_limit && iter_check!= iter_number && nomove_iter != max_nomove_iter){
                ArrayList<Solution> previousNeighboors = previousSolution.getNeighboors();
                for(int i =0; i < previousNeighboors.size();i++){
                    if(check_in_tabu(tabuList,previousNeighboors.get(i))){
                        previousNeighboors.remove(i);
                    }
                }

                currentSolution = bestSolutionFinder(previousNeighboors);

                if(currentSolution.cost() < bestSolution.cost()){
                    bestSolution = currentSolution;
                }
                tabuList.add(currentSolution);
            }
        }
        else {
            while(time_check!= time_check2 + time_limit && iter_check!= iter_number && nomove_iter != max_nomove_iter){
                ArrayList<Solution> previousNeighboors = previousSolution.getNeighboors();
                for(int i =0; i < previousNeighboors.size();i++){
                    if(check_in_tabu(tabuList,previousNeighboors.get(i))){
                        previousNeighboors.remove(i);
                    }
                }

                currentSolution = bestSolutionFinder(previousNeighboors);

                if(currentSolution.cost() < bestSolution.cost()){
                    bestSolution = currentSolution;
                }
                tabuList.remove();
                tabuList.add(currentSolution);
            }

        }
        return bestSolution;

    }
    //prend en entrée la liste des anciens voisins et analyse lequel a le meilleur cout et le retourne.
    private static Solution bestSolutionFinder(ArrayList<Solution> previousNeighboors) {
        Solution best = previousNeighboors.get(0);
        for(int i =1; i< previousNeighboors.size(); i++){
            if(previousNeighboors.get(i).cost() < best.cost()){
                best = previousNeighboors.get(i);
            }
        }
        return best;
    }


}
