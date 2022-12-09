package org.example;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class MatrixReader {

    public int length_of_matrix;
    public int[][] ringCost;
    public int[][] starCost;
    public String path;

    public MatrixReader(String path){
        this.path = path;
    }
    public static void main(String[] args) throws FileNotFoundException {
    }

    /**
     * rempli la matrice ringCost, starCost dans l'objet MatrixReader grace au path entré en parametres dans MatrixReader
     * @throws FileNotFoundException
     */
    public void matrixRead() throws FileNotFoundException {
        File file = new File(this.path);
        Scanner scanner = new Scanner(file);
        this.length_of_matrix = scanner.nextInt();                          // le premier entier du fichier en entete nous donne la taille de la matrice
        this.ringCost = new int[length_of_matrix][length_of_matrix];        // Première matrice du fichier
        this.starCost = new int[length_of_matrix][length_of_matrix];        // Deuxième matrice du fichier

        int count = 0;
        int count2 = 0;

        while(count2 != length_of_matrix){                                  //boucle qui transfert les élément de la premiere matrice du fichier .dat dans ringCost

            while(count != length_of_matrix){                               //ajoute l'un à la suite de l'autre les éléments de la 1ere matrice du fichier .dat dans ringCost
                this.ringCost[count2][count] = scanner.nextInt();
                count++;
            }
            count2++;
            count = 0;
        }
        count = 0;
        count2 = 0;

        while(count2 != length_of_matrix){                                  //boucle qui transfert les élément de la deuxieme matrice du fichier .dat dans starCost

            while(count != length_of_matrix){                               //ajoute l'un à la suite de l'autre les éléments de la 2eme matrice du fichier .dat dans starCost
                this.starCost[count2][count] = scanner.nextInt();
                count++;
            }
            count2++;
            count = 0;
        }

        //afficher la matrice 1 et 2 pour debug
        //System.out.println(ringCost[0][0]);
        //System.out.println(ringCost[11][10]);
        //System.out.println(ringCost[50][50]);

        //System.out.println(starCost[0][0]);
        //System.out.println(starCost[11][10]);
        //System.out.println(starCost[50][50]);
        }
    }

