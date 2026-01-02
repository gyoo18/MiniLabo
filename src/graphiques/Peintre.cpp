#include "Peintre.h"

#include <stdio.h>
#include <glad/glad.h>
#include <bits/stdc++.h>

#include "Maillage.h"
#include "Nuanceur.h"

float positions[] = {
    -0.5, -0.5, -0.5,
     0.5, -0.5, -0.5,
       0,  0.5, -0.5,
};

std::string somsrc = 
    "#version 460\n"
    "precision mediump float;\n"
    "\n"
    "layout (location=0) in vec3 pos;\n"
    "\n"
    "out vec3 pos_O;\n"
    "\n"
    "void main(){\n"
    "   pos_O = pos;\n"
    "   gl_Position = vec4(pos,1.0);\n"
    "}\n";

std::string fragsrc = 
    "#version 460\n"
    "precision mediump float;\n"
    "\n"
    "in vec3 pos_O;\n"
    "\n"
    "out vec4 Fragment;\n"
    "\n"
    "void main(){\n"
    "   Fragment = vec4(pos_O,1.0);\n"
    "}";

Maillage m({{FLOAT, 1}}, sizeof(positions)/sizeof(float), false);
Nuanceur n(somsrc, fragsrc);

Peintre::Peintre(){
    printf("Création du peintre\n");

    gladLoadGL();

    glClearColor(0.6,0.6,0.6,1.0);

    m.ajouterAttribut(positions, 3);
    m.construire();
    m.préparerAuDessin();

    n.construire();
    glUseProgram(n.m_ID);
}

void Peintre::miseÀJour(){
    glClear(GL_COLOR_BUFFER_BIT);
    
    glDrawArrays(GL_TRIANGLES, 0, 3);
}

void Peintre::surMiseÀJourFenêtre(int &l, int &h){
    glViewport(0,0,l,h);
}

Peintre::~Peintre(){
    
}
