package org.example.thomas;

import org.example.MatrixReader;
import org.example.Solution;
import org.example.MatrixReader.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomShit {

    private MatrixReader matrixReader;

    public RandomShit(MatrixReader matrixReader){
        this.matrixReader = matrixReader;
    }
    /*
    public Solution solve(int[][] ringCosts, int[][] starCosts, int nodeNbr){
        ArrayList<Integer> ring = new ArrayList<>();
        ArrayList<Integer[]> star = new ArrayList<>();

        int i = 0;

        ring.add(1);

        while(i < nodeNbr){
            int bestMatch = 0;
            int bestNode = 0;
            for (int j = 1 ; j < ringCosts[i].length && j < starCosts[i].length; j ++){
               if (bestMatch == 0){
                   bestMatch  = ringCosts[i][j];
                   bestNode = j;
               }
               else{
                   if (ringCosts[i][j] < bestMatch){
                       bestMatch = ringCosts[i][j];
                       bestNode = j;
                   }
               }
            }
            ring.add(bestNode);

        }
        return;
    }

     */

}
