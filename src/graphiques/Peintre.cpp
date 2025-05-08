#include "Peintre.h"

#include <iostream>
#include <glad/glad.h>
#include <bits/stdc++.h>

#include "Maillage.h"

float positions[] = {
    -0.5, -0.5, -0.5,
     0.5, -0.5, -0.5,
       0,  0.5, -0.5,
};

std::string somsrc = 
    "#version 460 core\n"
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
    "#version 460 core\n"
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
GLuint programme;

Peintre::Peintre(){
    printf("Création du peintre\n");

    gladLoadGL();

    glClearColor(0.6,0.6,0.6,1.0);

    // glGenVertexArrays(1,&VAO);
    // glBindVertexArray(VAO);

    // GLuint VBO = 0;
    // glGenBuffers(1,&VBO);
    // glBindBuffer(GL_ARRAY_BUFFER, VBO);
    // glBufferData(GL_ARRAY_BUFFER, sizeof(positions), positions, GL_STATIC_DRAW);
    // glVertexAttribPointer(0,3,GL_FLOAT,false,0,nullptr);
    // glEnableVertexAttribArray(0);

    m.ajouterAttribut(positions, 3);
    m.construire();
    m.préparerAuDessin();

    GLuint nuasom = glCreateShader(GL_VERTEX_SHADER);
    const char* src = somsrc.c_str();
    glShaderSource(nuasom, 1, &src, NULL);
    glCompileShader(nuasom);
    GLint rép = 0;
    glGetShaderiv(nuasom, GL_COMPILE_STATUS,&rép);
    if(!rép){
        GLint lMax = 0;
        glGetShaderiv(nuasom, GL_INFO_LOG_LENGTH, &lMax);
        GLchar stacktrace[lMax];
        glGetShaderInfoLog(nuasom,lMax,NULL,&stacktrace[0]);
        throw std::runtime_error( std::string("Le nuanceur de sommets n'a pas pus compiler :\n") + stacktrace );
    }

    GLuint nuafrag = glCreateShader(GL_FRAGMENT_SHADER);
    src = fragsrc.c_str();
    glShaderSource(nuafrag, 1, &src, NULL);
    glCompileShader(nuafrag);
    glGetShaderiv(nuafrag, GL_COMPILE_STATUS,&rép);
    if(!rép){
        GLint lMax = 0;
        glGetShaderiv(nuafrag, GL_INFO_LOG_LENGTH, &lMax);
        GLchar stacktrace[lMax];
        glGetShaderInfoLog(nuafrag,lMax,NULL,&stacktrace[0]);
        throw std::runtime_error( std::string("Le nuanceur de fragments n'a pas pus compiler :\n") + stacktrace );
    }

    programme = glCreateProgram();
    glAttachShader(programme,nuasom);
    glAttachShader(programme,nuafrag);
    glLinkProgram(programme);
    glGetProgramiv(programme,GL_LINK_STATUS,&rép);
    if(!rép){
        GLint lMax = 0;
        glGetProgramiv(programme, GL_INFO_LOG_LENGTH, &lMax);
        GLchar stacktrace[lMax];
        glGetShaderInfoLog(programme, lMax, NULL, &stacktrace[0]);
        throw std::runtime_error( std::string("Le nuanceur n'a pas pus faire la liaison :\n") + stacktrace );
    }

    glUseProgram(programme);
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
