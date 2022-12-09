package org.example;

import org.example.thomas.RandomShit;

import java.io.FileNotFoundException;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        MatrixReader mr = new MatrixReader("src/main/resources/data1.dat" /*+ args[0]*/);
        try{
            mr.matrixRead();
        }catch (FileNotFoundException e){
            e.getMessage();
        }

        // Choisir le solver à appeler
        //RandomShit solver = new RandomShit(mr);

        //Solution solution = solver.solve(mr.ringCost, mr.starCost, mr.length_of_matrix);

    }




}