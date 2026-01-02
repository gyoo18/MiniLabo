#include <stdio.h>

#include "graphiques/Fenêtre.h"

int main(){
    printf("Hello World!\n");
    Fenêtre* fenêtre = new Fenêtre(800,600,nullptr);

    while(!fenêtre->devraitFermer()){
        fenêtre->miseÀJour();
    }

    delete fenêtre;
}