cmake_minimum_required(VERSION 3.22)

project(graphiques)

add_library(graphiques STATIC Fenêtre.cpp Peintre.cpp)

include_directories(${CMAKE_SOURCE_DIR}/lib/glad/include)
target_link_libraries(graphiques glad)