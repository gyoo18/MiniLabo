#include <iostream>

#include "graphiques/Fenêtre.h"

int main(){
    printf("Hello World!");
    Fenêtre* fenêtre = new Fenêtre(800,600,nullptr);

    while(!fenêtre->devraitFermer()){
        fenêtre->miseÀJour();
    }

    delete fenêtre;
}