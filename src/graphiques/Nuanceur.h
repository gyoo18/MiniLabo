#pragma once
#include <glad/glad.h>
#include <bits/stdc++.h>
#include <map>
#include <string>

class Nuanceur{
public:
    GLuint m_ID;
    bool m_estConstruit = false;

private:
    std::string m_fragsrc;
    std::string m_somsrc;
    std::map<std::string, GLuint> uniformes;

public:
    Nuanceur(std::string fragment_source, std::string sommet_source);
    void construire();
    void chargerUniforme(std::string nom, bool valeur);
    void chargerUniforme(std::string nom, long valeur);
    void chargerUniforme(std::string nom, double valeur);

private:
    void lierAttributs();
    void lierUniformes();
};