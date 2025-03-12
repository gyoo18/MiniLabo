add_subdirectory(graphiques)
add_executable(MiniLabo MiniLabo.cpp)

target_link_libraries(MiniLabo graphiques)
include_directories(graphiques)