package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        int maxTimeSeconds = 120;
        String dataSet = "data9";
        double alpha = 0.8;
        runGrasp(maxTimeSeconds * 1000, dataSet, alpha);
    }

    /**
     * Run grasp on the given dataset
     * @param maxTime Time to run the algorithm (in ms)
     * @param dataSet The name of the dataSet in the folder src/main/resources/
     * @param alpha The value of alpha
     * @throws FileNotFoundException If the file is not found
     */
    public static void runGrasp(int maxTime, String dataSet, double alpha) throws FileNotFoundException {
        System.out.println("Running GRASP on " + dataSet + " for " + maxTime / 1000 + "s");
        MatrixReader matrixReader = new MatrixReader("grasp/src/main/resources/" + dataSet + ".dat");
        matrixReader.matrixRead();
        Grasp grasp = new Grasp(
                alpha,
                matrixReader.ringCost,
                matrixReader.starCost,
                matrixReader.length_of_matrix
        );
        Solution solution = grasp.findSolution(maxTime);
        System.out.println(solution);

        //Save
        String filename = "grasp/src/main/results/" + dataSet + ".txt";
        saveFile(filename, solution.toString());
    }

    /**
     * Save the given string in the given file
     * @param filename The name of the file (with the path)
     * @param content The content to save.
     */
    public static void saveFile(String filename, String content) {
        try {
            File file = new File(filename);
            FileWriter myWriter = new FileWriter(file);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}