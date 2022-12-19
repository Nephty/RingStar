package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

public class Main {
    // On met certaines variables en static pour pouvoir les utiliser dans les autres classes
    static int[][] starCost; // Matrice des coûts pour aller de i à j dans star
    static int[][] ringCost; // Matrice des côuts pour aller de i à j dans le ring
    static int size; // Taille du problème
    static ArrayList<ArrayList<Tuple<Integer>>> starOrdered; // matrice 2D de tuple (value, j) qui sont les couts des

    public static void main(String[] args) throws FileNotFoundException {

        for (double i = 0.8; i < 1.05 ; i += 0.1) {
            //tryAlpha(i, 5000);
            System.out.println("\n");
        }


        analysePerformance(0, 10);
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
        System.out.println(grasp.findSolution(maxTime));
        System.out.println("Calculation time:" + (Instant.now().toEpochMilli() - instant.toEpochMilli()) + " ms");
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
        final String filename = "src/main/resources/results_alpha_0.0" + (int)(alpha * 10) + ".dat";
        saveFile(filename, output.toString());
        System.out.println("file saved for alpha " + alpha);
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

    private static final int[] bestResults = {1278, 2113, 1244, 1614, 2504, 1710, 63846, 115388, 94265};
    public static void analysePerformance(int alphaStart, int alphaEnd) throws FileNotFoundException {
        int[][] alphaPerformance = new int[alphaEnd - alphaStart + 1][9];
        for (int i = 0; i < alphaPerformance.length; i++) {
            File file = new File("src/main/resources/results_alpha_0.0" + i + ".dat");
            Scanner scanner = new Scanner(file);
            int k = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(line.startsWith("S")) {
                    int costStart = line.lastIndexOf('t') + 4;
                    int costEnd = line.indexOf('r') - 2;
                    int cost = Integer.parseInt(line.substring(costStart, costEnd));
                    alphaPerformance[i][k] = cost;
                    k++;
                }
            }
        }
        double[][] performanceSurplus = new double[alphaPerformance.length][9];
        double[] percentageAverage = new double[alphaPerformance.length];
        for (int i = 0; i < alphaPerformance.length; i++) {
            System.out.println("\nFor an alpha of 0.0" + (alphaStart + i) + ":\n");
            double average = 0;
            // cost = bestCost * surplus
            // surplus - 1 = percentage of surplus
            for (int j = 0; j < 9; j++) {
                double surplus = ((double)alphaPerformance[i][j] / (double)bestResults[j]) - 1;
                performanceSurplus[i][j] = surplus;
                System.out.println("data" + (alphaStart + j) + ": +" + (int)(surplus * 100) + "%");
                average += surplus;
            }
            System.out.println("Average surplus of " + average / 9);
            percentageAverage[i] = average;
        }
        int index = 0;
        double bestAverage = percentageAverage[0];
        for (int i = 1; i < percentageAverage.length; i++) {
            if(percentageAverage[i] < bestAverage) {
                index = i;
                bestAverage = percentageAverage[i];
            }
        }
        System.out.println("The best average is for an alpha of 0.0" + (index + alphaStart) + ": " + bestAverage / 9);

    }
}