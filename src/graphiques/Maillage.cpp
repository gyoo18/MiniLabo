#include "Maillage.h"

#include <algorithm>
#include <glad/glad.h>

Maillage::Maillage(std::map<TYPE_DONNÉE,int> attributsTypes, int nbPoints, bool estIndexé){
    int nbAttributs = 0;
    for (std::map<TYPE_DONNÉE,int>::iterator iter = attributsTypes.begin(); iter != attributsTypes.end(); iter++){
        nbAttributs++;
        switch (iter->first) {
            case BOOL:
                m_boolListe = new bool*[iter->second];
                m_boolListeTaille = iter->second;
                break;
            case CHAR:
                m_charListe = new char*[iter->second];
                m_charListeTaille = iter->second;
                break;
            case SHORT:
                m_shortListe = new short*[iter->second];
                m_shortListeTaille = iter->second;
                break;
            case INT:
                m_intListe = new int*[iter->second];
                m_intListeTaille = iter->second;
                break;
            case LONG:
                m_longListe = new long*[iter->second];
                m_longListeTaille = iter->second;
                break;
            case FLOAT:
                m_floatListe = new float*[iter->second];
                m_floatListeTaille = iter->second;
                break;
            case DOUBLE:
                m_doubleListe = new double*[iter->second];
                m_doubleListeTaille = iter->second;
                break;

        }
    }
    m_attributsDimensions = new int[nbAttributs];
    m_attributsIndexe = new int[nbAttributs];
    std::fill_n(m_attributsIndexe, nbAttributs, -1);
    m_attributsTypes = new TYPE_DONNÉE[nbAttributs];
    m_nbAttributs = nbAttributs;
    m_estIndexé = estIndexé;
    m_nbDonnées = nbPoints;
    if(!estIndexé){
        m_nbSommets = nbPoints;
    }
}

void Maillage::ajouterAttribut(bool attributs[], int dimensions){
    bool àAjouter = false;
    int indexe = -1;
    for (int i = 0; i < m_boolListeTaille; i++){
        if (m_boolListe[i] == nullptr){
            m_boolListe[i] = attributs;
            àAjouter = true;
            indexe = i;
            break;
        }
    }
    if(!àAjouter){
        std::printf("[ERREUR] Maillage.ajouterAttribut() : impossible de rajouter un autre attribut booléen. Veuillez augmenter en augmenter le nombre.\n");
        return;
    }


    for (int i = 0; i < m_nbAttributs; i++){
        if(m_attributsIndexe[i] == -1){
            m_attributsIndexe[i] = indexe;
            m_attributsDimensions[i] = dimensions;
            m_attributsTypes[i] = BOOL;
        }
    }
}

void Maillage::ajouterAttribut(char attributs[], int dimensions){
    bool àAjouter = false;
    int indexe = -1;
    for (int i = 0; i < m_charListeTaille; i++){
        if (m_charListe[i] == nullptr){
            m_charListe[i] = attributs;
            àAjouter = true;
            indexe = i;
            break;
        }
    }
    if(!àAjouter){
        std::printf("[ERREUR] Maillage.ajouterAttribut() : impossible de rajouter un autre attribut booléen. Veuillez augmenter en augmenter le nombre.\n");
        return;
    }
    for (int i = 0; i < m_nbAttributs; i++){
        if(m_attributsIndexe[i] == -1){
            m_attributsIndexe[i] = indexe;
            m_attributsDimensions[i] = dimensions;
            m_attributsTypes[i] = CHAR;
        }
    }
}

void Maillage::ajouterAttribut(short attributs[], int dimensions){
    bool àAjouter = false;
    int indexe = -1;
    for (int i = 0; i < m_shortListeTaille; i++){
        if (m_shortListe[i] == nullptr){
            m_shortListe[i] = attributs;
            àAjouter = true;
            indexe = i;
            break;
        }
    }
    if(!àAjouter){
        std::printf("[ERREUR] Maillage.ajouterAttribut() : impossible de rajouter un autre attribut booléen. Veuillez augmenter en augmenter le nombre.\n");
        return;
    }
    for (int i = 0; i < m_nbAttributs; i++){
        if(m_attributsIndexe[i] == -1){
            m_attributsIndexe[i] = indexe;
            m_attributsDimensions[i] = dimensions;
            m_attributsTypes[i] = SHORT;
        }
    }
}

void Maillage::ajouterAttribut(int attributs[], int dimensions){
    bool àAjouter = false;
    int indexe = -1;
    for (int i = 0; i < m_intListeTaille; i++){
        if (m_intListe[i] == nullptr){
            m_intListe[i] = attributs;
            àAjouter = true;
            indexe = i;
            break;
        }
    }
    if(!àAjouter){
        std::printf("[ERREUR] Maillage.ajouterAttribut() : impossible de rajouter un autre attribut booléen. Veuillez augmenter en augmenter le nombre.\n");
        return;
    }
    for (int i = 0; i < m_nbAttributs; i++){
        if(m_attributsIndexe[i] == -1){
            m_attributsIndexe[i] = indexe;
            m_attributsDimensions[i] = dimensions;
            m_attributsTypes[i] = INT;
        }
    }
}

void Maillage::ajouterAttribut(long attributs[], int dimensions){
    bool àAjouter = false;
    int indexe = -1;
    for (int i = 0; i < m_longListeTaille; i++){
        if (m_longListe[i] == nullptr){
            m_longListe[i] = attributs;
            àAjouter = true;
            indexe = i;
            break;
        }
    }
    if(!àAjouter){
        std::printf("[ERREUR] Maillage.ajouterAttribut() : impossible de rajouter un autre attribut booléen. Veuillez augmenter en augmenter le nombre.\n");
        return;
    }
    for (int i = 0; i < m_nbAttributs; i++){
        if(m_attributsIndexe[i] == -1){
            m_attributsIndexe[i] = indexe;
            m_attributsDimensions[i] = dimensions;
            m_attributsTypes[i] = LONG;
        }
    }
}

void Maillage::ajouterAttribut(float attributs[], int dimensions){
    bool àAjouter = false;
    int indexe = -1;
    for (int i = 0; i < m_floatListeTaille; i++){
        if (m_floatListe[i] == nullptr){
            m_floatListe[i] = attributs;
            àAjouter = true;
            indexe = i;
            break;
        }
    }
    if(!àAjouter){
        std::printf("[ERREUR] Maillage.ajouterAttribut() : impossible de rajouter un autre attribut booléen. Veuillez augmenter en augmenter le nombre.\n");
        return;
    }
    for (int i = 0; i < m_nbAttributs; i++){
        if(m_attributsIndexe[i] == -1){
            m_attributsIndexe[i] = indexe;
            m_attributsDimensions[i] = dimensions;
            m_attributsTypes[i] = FLOAT;
        }
    }
}

void Maillage::ajouterAttribut(double attributs[], int dimensions){
    bool àAjouter = false;
    int indexe = -1;
    for (int i = 0; i < m_doubleListeTaille; i++){
        if (m_doubleListe[i] == nullptr){
            m_doubleListe[i] = attributs;
            àAjouter = true;
            indexe = i;
            break;
        }
    }
    if(!àAjouter){
        std::printf("[ERREUR] Maillage.ajouterAttribut() : impossible de rajouter un autre attribut booléen. Veuillez augmenter en augmenter le nombre.\n");
        return;
    }
    for (int i = 0; i < m_nbAttributs; i++){
        if(m_attributsIndexe[i] == -1){
            m_attributsIndexe[i] = indexe;
            m_attributsDimensions[i] = dimensions;
            m_attributsTypes[i] = DOUBLE;
        }
    }
}

void Maillage::ajouterIndexes(int indexes[], int indexesLongueur){

    if(!m_estIndexé){
        std::printf("[ATTENTION] Vous n'avez pas indiqué que ce maillage était indexé.\n");
    }

    if(indexes != nullptr){
        m_indexes = indexes;
        m_nbSommets = indexesLongueur/3;
    }
}

void Maillage::construire(){
    
    for(int i = 0; i < m_nbAttributs; i++){
        if(m_attributsIndexe[i] == -1){
            std::printf("[ERREUR] Vous n'avez pas initialisé tout les attributs. Veuillez modifier le nombre d'attributs ou tous les initialiser.\n");
            return;
        }
    }

    if(m_estIndexé && m_indexes != nullptr){
        std::printf("[ERREUR] Vous avez indiqué que le maillage est indexé, mais n'avez pas fournis d'indexes. Veuillez fournir une liste d'indexes ou ne pas indiquer que le maillage est indexé.\n");
        return;
    }

    glGenVertexArrays(1, &m_VAO);
    glBindVertexArray(m_VAO);

    m_VBOs = new GLuint[m_nbAttributs];
    glGenBuffers(m_nbAttributs, m_VBOs);

    for(int i = 0; i < m_nbAttributs; i++){

        glBindBuffer(GL_ARRAY_BUFFER, m_VBOs[i]);

        switch(m_attributsTypes[i]){
            case BOOL:{
                bool* données = m_boolListe[m_attributsIndexe[i]];

                glBufferData(GL_ARRAY_BUFFER, m_nbDonnées*m_attributsDimensions[i]*sizeof(bool), données, GL_STATIC_DRAW);
                glVertexAttribPointer(i, m_attributsDimensions[i], GL_BYTE, false, 0, nullptr);
                break;
            }
            case CHAR:{
                char* données = m_charListe[m_attributsIndexe[i]];

                glBufferData(GL_ARRAY_BUFFER, m_nbDonnées*m_attributsDimensions[i]*sizeof(char), données, GL_STATIC_DRAW);
                glVertexAttribPointer(i, m_attributsDimensions[i], GL_BYTE, false, 0, nullptr);
                break;
            }
            case SHORT:{
                short* données = m_shortListe[m_attributsIndexe[i]];

                glBufferData(GL_ARRAY_BUFFER, m_nbDonnées*m_attributsDimensions[i]*sizeof(short), données, GL_STATIC_DRAW);
                glVertexAttribPointer(i, m_attributsDimensions[i], GL_SHORT, false, 0, nullptr);
                break;
            }
            case INT:{
                int* données = m_intListe[m_attributsIndexe[i]];

                glBufferData(GL_ARRAY_BUFFER, m_nbDonnées*m_attributsDimensions[i]*sizeof(int), données, GL_STATIC_DRAW);
                glVertexAttribPointer(i, m_attributsDimensions[i], GL_INT, false, 0, nullptr);
                break;
            }
            case LONG:{
                long* données = m_longListe[m_attributsIndexe[i]];

                glBufferData(GL_ARRAY_BUFFER, m_nbDonnées*m_attributsDimensions[i]*sizeof(long), données, GL_STATIC_DRAW);
                glVertexAttribPointer(i, m_attributsDimensions[i], GL_INT, false, 0, nullptr);
                break;
            }
            case FLOAT:{
                float* données = m_floatListe[m_attributsIndexe[i]];

                glBufferData(GL_ARRAY_BUFFER, m_nbDonnées*m_attributsDimensions[i]*sizeof(float), données, GL_STATIC_DRAW);
                glVertexAttribPointer(i, m_attributsDimensions[i], GL_FLOAT, false, 0, nullptr);
                break;
            }
            case DOUBLE:{
                double* données = m_doubleListe[m_attributsIndexe[i]];

                glBufferData(GL_ARRAY_BUFFER, m_nbDonnées*m_attributsDimensions[i]*sizeof(double), données, GL_STATIC_DRAW);
                glVertexAttribPointer(i, m_attributsDimensions[i], GL_DOUBLE, false, 0, nullptr);
                break;
            }
        }

        if(m_estIndexé){
            GLuint m_IVBO;
            glGenBuffers(1, &m_IVBO);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, m_nbSommets*3*sizeof(int), m_indexes, GL_STATIC_DRAW);
        }

        m_estConstruit = true;
    }
}

void Maillage::préparerAuDessin(){
    glBindVertexArray(m_VAO);
    for (int i = 0; i < m_nbAttributs; i++){
        glEnableVertexAttribArray(i);
    }
}

Maillage Maillage::copier(){
    std::map<TYPE_DONNÉE, int> map;
    Maillage m(map, m_nbDonnées, m_estIndexé);

    copierAttributs<bool>   (m_boolListe,   m_boolListeTaille,   BOOL,   m.m_boolListe);
    copierAttributs<char>   (m_charListe,   m_charListeTaille,   CHAR,   m.m_charListe);
    copierAttributs<short>  (m_shortListe,  m_shortListeTaille,  SHORT,  m.m_shortListe);
    copierAttributs<int>    (m_intListe,    m_intListeTaille,    INT,    m.m_intListe);
    copierAttributs<long>   (m_longListe,   m_longListeTaille,   LONG,   m.m_longListe);
    copierAttributs<float>  (m_floatListe,  m_floatListeTaille,  FLOAT,  m.m_floatListe);
    copierAttributs<double> (m_doubleListe, m_doubleListeTaille, DOUBLE, m.m_doubleListe);

    m.m_boolListeTaille = m_boolListeTaille;
    m.m_charListeTaille = m_charListeTaille;
    m.m_shortListeTaille = m_shortListeTaille;
    m.m_intListeTaille = m_intListeTaille;
    m.m_longListeTaille = m_longListeTaille;
    m.m_floatListeTaille = m_floatListeTaille;
    m.m_doubleListeTaille = m_doubleListeTaille;

    std::copy( m_indexes, m_indexes + m_nbAttributs, m.m_indexes);

    std::copy( m_attributsDimensions, m_attributsDimensions + m_nbAttributs, m.m_attributsDimensions);
    std::copy( m_attributsIndexe, m_attributsIndexe + m_nbAttributs, m.m_attributsIndexe);
    std::copy( m_attributsTypes, m_attributsTypes + m_nbAttributs, m.m_attributsTypes);

    m.m_estIndexé = m_estIndexé;
    m.m_nbSommets = m_nbSommets;
    m.m_nbAttributs = m_nbAttributs;
    m.m_estConstruit = false;

    return m;
}

template<typename T>
void Maillage::copierAttributs(T** &listeA, int &tailleListeA, TYPE_DONNÉE attributType, T** &listeB){

    for (int i = 0; i < tailleListeA; i++){

        int tailleAttribut;
        for (int j = 0; j < m_nbAttributs; j++){
            if(m_attributsTypes[j] == attributType && m_attributsIndexe[j] == i){
                tailleAttribut = m_attributsDimensions[j]*m_nbDonnées;
                break;
            }
        }

        listeB[i] = new T[tailleAttribut];
        for (int j = 0; j < tailleAttribut; j++){
            listeB[i][j] = listeA[i][j];
        }
    }
}

Maillage::~Maillage(){

    glBindVertexArray(m_VAO);

    glDeleteBuffers(m_nbAttributs, m_VBOs);
    glDeleteBuffers(1, &m_IVBO);
    glDeleteVertexArrays(1, &m_VAO);

    delete m_boolListe;
    delete m_charListe;
    delete m_shortListe;
    delete m_intListe;
    delete m_longListe;
    delete m_floatListe;
    delete m_doubleListe;

    delete m_indexes;

    delete m_attributsDimensions;
    delete m_attributsIndexe;
    delete m_attributsTypes;

    delete m_VBOs;
}