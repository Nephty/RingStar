//
// Created by Nephty on 12/2/22.
//

#include "main.h"
#include <fstream>
#include <string>
#include <iostream>
#include <stdio.h>


// Permet la lecture du fichier dat et transforme les données qui y sont inclues en deux matrices différentes.

void read_matrix(){
    std::string const fichier("C:/Users/loren/CLionProjects/RingStar/data/data1.dat"); // à remplacer avec votre path (comment on fait un path général ??)

    std::ifstream flux1 (fichier.c_str(),std::ios::binary);

    if(flux1){                                    // si le flux existe
        std::string current_line;
        int length_of_matrix;
        flux1 >> length_of_matrix;                // le premier entier du fichier en entete nous donne la taille de la matrice
        std::cout << std::to_string(length_of_matrix) << std::endl; //print juste la taille de la matrice pour vérifier

        int matrix1[length_of_matrix][length_of_matrix];  // Première matrice du fichier
        int matrix2[length_of_matrix][length_of_matrix];  // Deuxième matrice du fichier

        int count = 0;
        int count2 = 0;
        int current_int;

        //boucle qui transfert les élément de la premiere matrice du fichier dat dans la matrice1
        while(count2 != length_of_matrix){

            while(count != length_of_matrix){
                flux1 >> current_int;
                matrix1[count2][count] = current_int;   //ajoute l'un à la suite de l'autre les éléments de la matrice du fichier .dat dans matrix1
                count++;
            }
            count2++;
            count = 0;
        }

        count = 0;
        count2 = 0;
        current_int;

        //boucle qui transfert les élément de la deuxieme matrice du fichier dat dans la matrice2
        while(count2 != length_of_matrix){

            while(count != length_of_matrix){
                flux1 >> current_int;
                matrix2[count2][count] = current_int;   //ajoute l'un à la suite de l'autre les éléments de la matrice du fichier .dat dans matrix1
                count++;
            }
            count2++;
            count = 0;
        }

        //afficher la matrice 1 et 2 pour debug
        for(int i=0; i<length_of_matrix; i++){
            for(int j=0; j<length_of_matrix; j++){
                printf("%d ",matrix1[i][j]);
            }
        }
        printf("\n\n");
        for(int i=0; i<length_of_matrix; i++){
            for(int j=0; j<length_of_matrix; j++){
                printf("%d ",matrix2[i][j]);
            }
        }
            {
            }
    }
    else{
        std::cout <<"Erreur de la lecture du fichier." << std::endl;
    }
}

int main(){
    read_matrix();
}
