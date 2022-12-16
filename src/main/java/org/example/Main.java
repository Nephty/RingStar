package org.example;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    // On met certaines variables en static pour pouvoir les utiliser dans les autres classes
    static int[][] starCost; // Matrice des coûts pour aller de i à j dans star
    static int[][] ringCost; // Matrice des côuts pour aller de i à j dans le ring
    static int size; // Taille du problème
    static ArrayList<ArrayList<Tuple<Integer>>> starOrdered; // matrice 2D de tuple (value, j) qui sont les couts des

    public static void main(String[] args) throws FileNotFoundException {
        for (double i = 0; i <= 3 ; i += 0.1) {
            tryAlpha(i/10, 10000);
        }
    }

    public static void runGrasp(int maxTime) throws FileNotFoundException {
        MatrixReader matrixReader = new MatrixReader("src/main/resources/data1.dat");
        matrixReader.matrixRead();
        Grasp grasp = new Grasp(
                3000,
                0.9,
                matrixReader.ringCost,
                matrixReader.starCost,
                matrixReader.length_of_matrix
        );
        Instant instant = Instant.now();
        System.out.println(grasp.findSolution(10000));
        System.out.println("Calculation time:" + (Instant.now().toEpochMilli() - instant.toEpochMilli()) + " ms");
        System.out.println("Movement count: " + grasp.movementCount);
    }

    public static void tryAlpha(double alpha, int msPerGrasp) throws FileNotFoundException {
        StringBuilder output = new StringBuilder();
        output.append("alpha = ").append(alpha).append("\n");
        for(int i = 1; i < 10; i++) {
            MatrixReader matrixReader = new MatrixReader("src/main/resources/data" + i +".dat");
            matrixReader.matrixRead();
            Grasp grasp = new Grasp(
                    3000,
                    alpha,
                    matrixReader.ringCost,
                    matrixReader.starCost,
                    matrixReader.length_of_matrix
            );
            output.append("\ndata").append(i).append(":\n");
            output.append(grasp.findSolution(msPerGrasp).toString()).append("\n");
        }
        final String filename = "src/main/resources/results_alpha_" + alpha + ".dat";
        saveFile(filename, output.toString());
    }

    public static void saveFile(String filename, String content) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}