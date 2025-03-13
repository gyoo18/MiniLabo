add_subdirectory(graphiques)

add_executable(MiniLabo MiniLabo.cpp)

target_include_directories(MiniLabo PUBLIC graphiques)
target_link_libraries(MiniLabo graphiques)