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
    std::string const fichier("C:/Users/loren/CLionProjects/RingStar/data/data1.dat");

    std::ifstream flux1 (fichier.c_str());

    if(flux1){

        std::string current_line;
        int length_of_matrix;
        std::cout << std::to_string(length_of_matrix) << std::endl;
        flux1 >> length_of_matrix;

        int matrix1[length_of_matrix][length_of_matrix];  // Première matrice du fichier
        int matrix2[length_of_matrix][length_of_matrix];  // Deuxième matrice du fichier

        int count = 0;
        int count2 = 0;
        int current_int;

        while(count2 != length_of_matrix){

            while(count != length_of_matrix){
                flux1 >> current_int;
                matrix1[count2][count] = current_int;
                count++;
            }
            count2++;
        }


    }

    else{
        std::cout <<"Erreur de la lecture du fichier." << std::endl;
    }
}

int main(){
    read_matrix();
}
