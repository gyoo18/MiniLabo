#include "Nuanceur.h"
#include "glad/glad.h"
#include <csignal>
#include <cstddef>
#include <stdexcept>
#include <string>

Nuanceur::Nuanceur(std::string sommet_source, std::string fragment_source){
    m_fragsrc = fragment_source;
    m_somsrc = sommet_source;
}

void Nuanceur::construire(){

    GLuint nuasom = glCreateShader(GL_VERTEX_SHADER);
    const char* src = m_somsrc.c_str();
    glShaderSource(nuasom, 1, &src, NULL);
    glCompileShader(nuasom);
    GLint réussite = 0;
    glGetShaderiv(nuasom, GL_COMPILE_STATUS, &réussite);
    if(!réussite){
        GLint max_carac = 0;
        glGetShaderiv(nuasom, GL_INFO_LOG_LENGTH, &max_carac);
        GLchar stacktrace[max_carac];
        glGetShaderInfoLog(nuasom, max_carac, NULL, &stacktrace[0]);
        throw std::runtime_error( std::string("Le nuanceur de sommets n'a pas pus compiler :\n") + stacktrace );
    }

    GLuint nuafrag = glCreateShader(GL_FRAGMENT_SHADER);
    src = m_fragsrc.c_str();
    glShaderSource(nuafrag, 1, &src, NULL);
    glCompileShader(nuafrag);
    réussite = 0;
    glGetShaderiv(nuafrag, GL_COMPILE_STATUS, &réussite);
    if(!réussite){
        GLint max_carac = 0;
        glGetShaderiv(nuafrag, GL_INFO_LOG_LENGTH, &max_carac);
        GLchar stacktrace[max_carac];
        glGetShaderInfoLog(nuafrag, max_carac, NULL, &stacktrace[0]);
        throw std::runtime_error(std::string("Le nuanceur de fragment n'a pas pus compiler :\n") + stacktrace);
    }

    m_ID = glCreateProgram();
    glAttachShader(m_ID, nuasom);
    glAttachShader(m_ID, nuafrag);
    glLinkProgram(m_ID);
    réussite = 0;
    glGetProgramiv(m_ID, GL_LINK_STATUS, &réussite);
    if(!réussite){
        GLint max_carac = 0;
        glGetProgramiv(m_ID, GL_INFO_LOG_LENGTH, &max_carac);
        GLchar stacktrace[max_carac];
        glGetProgramInfoLog(m_ID, max_carac, NULL, &stacktrace[0]);
        throw std::runtime_error(std::string("La liaison des nuanceurs a échouée :\n") + stacktrace);
    }

    glUseProgram(m_ID);

    lierAttributs();
    lierUniformes();

    m_estConstruit = true;
}

void Nuanceur::chargerUniforme(std::string nom, bool valeur){   glUniform1ui(uniformes.find(nom)->second, valeur);  }
void Nuanceur::chargerUniforme(std::string nom, long valeur){   glUniform1i(uniformes.find(nom)->second, valeur);   }
void Nuanceur::chargerUniforme(std::string nom, double valeur){ glUniform1f(uniformes.find(nom)->second, valeur);   }

void Nuanceur::lierAttributs(){
    GLint compte = 0;
    glGetProgramiv(m_ID,GL_ACTIVE_ATTRIBUTES,&compte);
    for (int i = 0; i < compte; i++){
        GLchar* nom;
        glGetActiveAttrib(m_ID, i, 255, NULL, NULL, NULL, nom);
        glBindAttribLocation(m_ID,i,nom);
    }
}

void Nuanceur::lierUniformes(){
    GLint compte = 0;
    glGetProgramiv(m_ID,GL_ACTIVE_UNIFORMS, &compte);
    for(int i = 0; i < compte; i++){
        GLchar* nom;
        glGetActiveUniform(m_ID, i, 255, NULL, NULL, NULL, nom);
        uniformes.insert(std::pair<std::string,GLuint>(nom,i));
    }
}
