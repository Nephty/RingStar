package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class MatrixReader {

    public int length_of_matrix;
    public int[][] ringCost;
    public int[][] starCost;
    public String path;

    /**
     * Constructor of the class MatrixReader. It takes the path of the file to read and instantiate it's attributes.
     * <p>
     * Attributes : - length_of_matrix : the size of the problem (number of nodes)
     * - ringCost : the cost of going from a node to another one in the ring
     * - starCost : the cost of going from a node to another one in the star
     * - path : the path of the file to read
     *
     * @param path the path of the file to read
     */
    public MatrixReader(String path) {
        this.path = path;
    }

    /**
     * Instantiate the ringCost, starCost and length with the data from the path.
     **/
    public void matrixRead() throws FileNotFoundException {
        File file = new File(this.path);
        Scanner scanner = new Scanner(file);
        this.length_of_matrix = scanner.nextInt();                          // First line gives us the size of the matrix
        this.ringCost = new int[length_of_matrix][length_of_matrix];        // First matrix
        this.starCost = new int[length_of_matrix][length_of_matrix];        // Second matrix

        int count = 0;
        int count2 = 0;

        while (count2 != length_of_matrix) {                                  // Create ringCost

            while (count != length_of_matrix) {                               // Add the numbers from the file to the ringCost matrix
                this.ringCost[count2][count] = scanner.nextInt();
                count++;
            }
            count2++;
            count = 0;
        }
        count = 0;
        count2 = 0;

        while (count2 != length_of_matrix) {                                  // Creates starCost

            while (count != length_of_matrix) {                               // Add the numbers from the file to the starCost matrix
                this.starCost[count2][count] = scanner.nextInt();
                count++;
            }
            count2++;
            count = 0;
        }
    }
}