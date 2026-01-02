cmake_minimum_required(VERSION 3.22)

add_library(graphiques STATIC 
    Fenêtre.cpp 
    Peintre.cpp 
    Maillage.cpp
    Nuanceur.cpp
)

target_link_libraries(graphiques glad)