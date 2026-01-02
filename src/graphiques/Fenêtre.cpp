#include "Fenêtre.h"

#include <stdio.h>
#include <stdexcept>
#include <GLFW/glfw3.h>

Fenêtre::Fenêtre(int l, int h, Peintre* peintre){
    printf("Création de la fenêtre\n");
    m_largeurPx = l;
    m_hauteurPx = h;

    glfwInit();
    glfwDefaultWindowHints();
    m_glfwFenêtre = glfwCreateWindow(l,h,"MiniLabo",NULL,NULL);
    if (m_glfwFenêtre == nullptr){
        throw std::runtime_error("La fenêtre GLFW n'a pas pue être créé.");
    }
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 6);
    glfwMakeContextCurrent(m_glfwFenêtre);
    glfwSetWindowUserPointer(m_glfwFenêtre, this);
    glfwShowWindow(m_glfwFenêtre);

    if (peintre == nullptr){
        m_peintre = new Peintre();
    }else{
        m_peintre = peintre;
    }

    m_peintre->surMiseÀJourFenêtre(l,h);


    glfwSetFramebufferSizeCallback(m_glfwFenêtre,Fenêtre::surMiseÀJourFenêtre);
}

bool Fenêtre::devraitFermer(){
    return glfwWindowShouldClose(m_glfwFenêtre);
}

void Fenêtre::miseÀJour(){
    glfwPollEvents();
    m_peintre->miseÀJour();
    glfwSwapBuffers(m_glfwFenêtre);
}

void Fenêtre::surMiseÀJourFenêtre(GLFWwindow* glfwFenêtre, int l, int h){
    Fenêtre* fenêtre = (Fenêtre*)glfwGetWindowUserPointer(glfwFenêtre);
    if(fenêtre != nullptr){
        fenêtre->surMiseÀJourFenêtre(l,h);
    }
}

void Fenêtre::surMiseÀJourFenêtre(int l, int h){
    m_peintre->surMiseÀJourFenêtre(l,h);
}

Fenêtre::~Fenêtre(){
    delete m_peintre;
    glfwTerminate();
}