#include <bits/stdc++.h>
#include <glad/glad.h>

enum TYPE_DONNÉE{
    BOOL,
    CHAR,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE
};

class Maillage{
public:
private:
    // Chaque liste d'attribut est stocké dans une liste, selon le type.
    bool**   m_boolListe;
    char**   m_charListe;
    short**  m_shortListe;
    int**    m_intListe;
    long**   m_longListe;
    float**  m_floatListe;
    double** m_doubleListe;

    int m_boolListeTaille;
    int m_charListeTaille;
    int m_shortListeTaille;
    int m_intListeTaille;
    int m_longListeTaille;
    int m_floatListeTaille;
    int m_doubleListeTaille;

    int* m_indexes;

    // Les listes suivantes décrivent chaque liste d'attribut, en ordre.
    int* m_attributsDimensions;     // Dimensions des attributs
    int* m_attributsIndexe;         // Indexe de la liste d'attributs dans la liste de listes.
    TYPE_DONNÉE* m_attributsTypes;  // Type de l'attribut

    // Si estIndexé = false, alors m_nbSommets = m_nbDonnées
    bool m_estIndexé;
    int m_nbSommets; // Nombre de triangles x3 (un point peut être partagé par plusieurs triangles, mais chaque référence compte pour un sommet)
    int m_nbAttributs;
    int m_nbDonnées; // Nombre de points (un point peut être partagé par plusieurs triangle, mais il ne compte que pour une donnée)
    bool m_estConstruit = false;

    GLuint m_VAO;
    GLuint* m_VBOs; // VBO des objets
    GLuint m_IVBO;  // VBO des indexes
public:
    Maillage(std::map<TYPE_DONNÉE,int> attributsTypes, int nbPoints, bool estIndexé);

    // ATTENTION: Les méthodes suivantes NE VÉRIFIENT PAS que la liste passée est de la bonne taille.
    void ajouterAttribut(bool attributs[], int dimensions);
    void ajouterAttribut(char attributs[], int dimensions);
    void ajouterAttribut(short attributs[], int dimensions);
    void ajouterAttribut(int attributs[], int dimensions);
    void ajouterAttribut(long attributs[], int dimensions);
    void ajouterAttribut(float attributs[], int dimensions);
    void ajouterAttribut(double attributs[], int dimensions);
    void ajouterIndexes(int indexes[], int indexesLongueur);

    void construire();
    void préparerAuDessin();

    Maillage copier();

    ~Maillage();

private:

    template <typename T>
    void copierAttributs(T** &listeA, int &tailleListeA, TYPE_DONNÉE attributType, T** &listeB);
};