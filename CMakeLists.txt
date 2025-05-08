cmake_minimum_required(VERSION 3.22)
set(CMAKE_CXX_STANDARD 23)

#Initialisation

project(MiniLabo)
add_subdirectory(src)

# Bibliothèques
add_subdirectory(lib/glad)

find_package(glfw3 REQUIRED)
set(LIB "${LIB};glfw")

find_package(OpenGL REQUIRED)
set(LIB "${LIB};OpenGL")

target_include_directories(MiniLabo PUBLIC lib/glad/include)
set(LIB "${LIB};glad")

target_link_libraries(MiniLabo ${LIB})
