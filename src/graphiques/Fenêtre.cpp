#include "Fenêtre.h"

#include <iostream>
#include <GLFW/glfw3.h>

Fenêtre::Fenêtre(int l, int h, Peintre* peintre){
    m_largeurPx = l;
    m_hauteurPx = h;

    glfwInit();
    glfwDefaultWindowHints();
    m_glfwFenêtre = glfwCreateWindow(l,h,"MiniLabo",NULL,NULL);
    if (m_glfwFenêtre == nullptr){
        throw std::runtime_error("La fenêtre GLFW n'a pas pue être créé.");
    }
    glfwMakeContextCurrent(m_glfwFenêtre);
    glfwShowWindow(m_glfwFenêtre);

    if (peintre == nullptr){
        m_peintre = new Peintre();
    }
}

bool Fenêtre::devraitFermer(){
    return glfwWindowShouldClose(m_glfwFenêtre);
}

void Fenêtre::miseÀJour(){
    glfwPollEvents();
    glfwSwapBuffers(m_glfwFenêtre);
}

Fenêtre::~Fenêtre(){
    delete m_peintre;
    glfwTerminate();
}