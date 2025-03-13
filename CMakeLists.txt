cmake_minimum_required(VERSION 3.22)

#Initialisation

project(MiniLabo)

add_subdirectory(lib/glad)

add_subdirectory(src)

# Bibliothèques
if(UNIX)
    include(cmake/GLFWConfig.cmake)
    set(LIB "${LIB};glfw")
else()
    find_package(glfw3 REQUIRED)
    set(LIB "${LIB};glfw")
endif()
find_package(OpenGL REQUIRED)
set(LIB "${LIB};OpenGL")
target_include_directories(MiniLabo PUBLIC lib/glad/include)
set(LIB "${LIB};glad")
target_link_libraries(MiniLabo ${LIB})