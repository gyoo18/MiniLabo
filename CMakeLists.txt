cmake_minimum_required(VERSION 3.22)
set(CMAKE_CXX_STANDARD 23)

#Initialisation

project(MiniLabo)
add_subdirectory(src)

# Bibliothèques

find_package(glfw3 REQUIRED)
set(LIB "${LIB};glfw")

add_subdirectory(lib/glad)
set(LIB "${LIB};glad")

target_link_libraries(MiniLabo ${LIB})
