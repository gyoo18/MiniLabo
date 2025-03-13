#pragma once

#include <GLFW/glfw3.h>
#include "Peintre.h"

class Fenêtre{
public:
    int m_largeurPx;
    int m_hauteurPx;
    GLFWwindow* m_glfwFenêtre;
    Peintre* m_peintre;
private:

public:
    static void surMiseÀJourFenêtre(GLFWwindow* fenêtre, int l, int h);

    Fenêtre(int l, int h, Peintre* peintre);
    bool devraitFermer();
    void miseÀJour();
    void surMiseÀJourFenêtre(int l, int h);
    ~Fenêtre();
private:

};