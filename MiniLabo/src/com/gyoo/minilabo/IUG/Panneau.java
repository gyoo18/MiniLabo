package com.gyoo.minilabo.IUG;

import org.lwjgl.opengl.GL46;

import com.gyoo.minilabo.Chargeur;
import com.gyoo.minilabo.Ressources;
import com.gyoo.minilabo.Vecteurs.Vecteur2f;

public class Panneau {
	
	public Vecteur2f pos = new Vecteur2f(0); //Position du panneau en pixels
	public Vecteur2f éch = new Vecteur2f(100f); //Échelle du panneau. Facteur de grandeur en pixels
	
	public int IDColorisateur; //ID du colorisateur (shader). À activer avant toute opération graphique.
	public int vaoID; //ID du Vertex Array Object. Pointe vers un liste qui contient les attributs/points.
	
	public Liste liste = new Liste();
	
	//Liste des positions des points du carré en x,y. Utilise GL_TRIANGLE_STRIP. NE PAS CHANGER L'ORDRE
	private float[] points = new float[] {
			-1f,-1f,
			 1f,-1f,
			-1f, 1f,
			 1f, 1f
	};
	
	private int COL_TRANSLATION; //ID de la variable de translation pour le colorisateur
	private int COL_ÉCHELLE; //ID de la variable d'échelle pour le colorisateur
	private int COL_RÉSOLUTION; //ID de la variable de résolution de l'écran pour le colorisateur
	
	private Ressources ressources = Ressources.avoirRessources();
	
	//Initialisation
	public Panneau() {
		faireColorisateur();
		faireVao();
		for (int i = 0; i < 3; i++) {
			Conteneur c = new Conteneur(pos, éch);
			liste.ajouterConteneur(c);
		}
		liste.ajusterLongueurParent = true;
	}
	
	private void faireColorisateur() {
		//Colorisateur de points (Vertex Shader)
		int pointColorisateur = GL46.glCreateShader(GL46.GL_VERTEX_SHADER); //Création
		GL46.glShaderSource(pointColorisateur, Chargeur.chargerTexte("Colorisateurs/Panneau.vert.glsl")); //Liaison avec le code source
		GL46.glCompileShader(pointColorisateur); //Compilation
		final int[] compileStatus = new int[1];
		GL46.glGetShaderiv(pointColorisateur, GL46.GL_COMPILE_STATUS, compileStatus); //vérification d'erreur
	    if(compileStatus[0] == GL46.GL_FALSE){
	    	//S'il y a erreur, le programe plante avec un message
	        System.err.println("La compilation du colorisateur de points a échouée.");
	        System.err.println("[ICI]" + GL46.glGetShaderInfoLog(pointColorisateur));
	        System.err.println(GL46.glGetShaderSource(pointColorisateur));
	        System.exit(-1);
	    }
	    
	    //Colorisateur de fragments (Fragment Shader)
	    int pixColorisateur = GL46.glCreateShader(GL46.GL_FRAGMENT_SHADER); //Création
		GL46.glShaderSource(pixColorisateur, Chargeur.chargerTexte("Colorisateurs/Panneau.frag.glsl")); //Liaison avec le code source
		GL46.glCompileShader(pixColorisateur); //Compilation
		GL46.glGetShaderiv(pixColorisateur, GL46.GL_COMPILE_STATUS, compileStatus); //vérification d'erreur
	    if(compileStatus[0] == GL46.GL_FALSE){
	    	//S'il y a erreur, le programe plante avec un message
	        System.err.println("La compilation du colorisateur de pixels a échouée.");
	        System.err.println("[ICI]" + GL46.glGetShaderInfoLog(pixColorisateur));
	        System.err.println(GL46.glGetShaderSource(pixColorisateur));
	        System.exit(-1);
	    }
	    
	    //Liaison des colorisateurs
	    IDColorisateur = GL46.glCreateProgram(); //Création du programe
	    GL46.glAttachShader(IDColorisateur, pointColorisateur); //Liaison avec le colorisateur de points
	    GL46.glAttachShader(IDColorisateur, pixColorisateur); //Liaison avec le colorisateur de fragments
		
	    GL46.glLinkProgram(IDColorisateur); //Compilation
        //GL46.glValidateProgram(IDColorisateur);
        GL46.glGetProgramiv(IDColorisateur, GL46.GL_LINK_STATUS, compileStatus); //Vérification d'erreur
        if(compileStatus[0] == GL46.GL_FALSE){
        	//S'il y a erreur, le programe plante avec un message
            System.err.println("La liaison des colorisateurs a échouée.");
            System.err.println("[ICI]" + GL46.glGetProgramInfoLog(IDColorisateur));

            System.exit(-1);
        }
        
        GL46.glBindAttribLocation(IDColorisateur,0,"pos"); //Liaison entre les positions des points et les données
        
        COL_TRANSLATION = GL46.glGetUniformLocation(IDColorisateur,"trans"); //Pointeur vers la position de la valeur de translation
        COL_ÉCHELLE = GL46.glGetUniformLocation(IDColorisateur,"ech");  //Pointeur vers la position de la valeur d'échelle
        COL_RÉSOLUTION = GL46.glGetUniformLocation(IDColorisateur, "res");  //Pointeur vers la position de la valeur de résolution de l'écran
	}
	
	private void faireVao() {
		vaoID = GL46.glGenVertexArrays(); //Générer le vao
		GL46.glBindVertexArray(vaoID); //Lier le vao avec le contexte
		
		int vboID = GL46.glGenBuffers(); //Générer un tampon (buffer) pour contenir les données
		GL46.glBindBuffer(GL46.GL_ARRAY_BUFFER,vboID); //Lier le tampon avec le vao
		GL46.glBufferData(GL46.GL_ARRAY_BUFFER, points, GL46.GL_STATIC_DRAW); //Fournir les données du tampon
		GL46.glVertexAttribPointer(0,2,GL46.GL_FLOAT,false,0,0); //Préciser la nature des données 
		
		GL46.glBindVertexArray(0); //Délier le vao du contexte
	}
	
	public Vecteur2f avoirPos() {
		return pos;
	}
	
	public Vecteur2f avoirÉch() {
		return éch;
	}
	
	//Dessiner le panneau
	public void dessiner() {
		GL46.glClear(GL46.GL_STENCIL_BUFFER_BIT);
		GL46.glEnable(GL46.GL_STENCIL_TEST);
		GL46.glStencilFunc(GL46.GL_ALWAYS,1,0xFF);
		GL46.glStencilOp(GL46.GL_REPLACE, GL46.GL_REPLACE, GL46.GL_REPLACE);
		GL46.glStencilMask(0xFF);
		
		GL46.glUseProgram(IDColorisateur); //Activer les colorisateurs
		
		GL46.glBindVertexArray(vaoID); //Lier le vao avec le contexte
		GL46.glEnableVertexAttribArray(0); //Activer la liste de positions
		
		GL46.glUniform2f(COL_TRANSLATION,pos.x,pos.y); //Fournir la translation aux colorisateurs
		GL46.glUniform2f(COL_ÉCHELLE,éch.x,éch.y); //Fournir l'échelle aux colorisateurs
		GL46.glUniform2f(COL_RÉSOLUTION,ressources.résolution.x,ressources.résolution.y); //Fournir la résolution de l'écran aux colorisateurs
		
		GL46.glDrawArrays(GL46.GL_TRIANGLE_STRIP, 0, 4); //Dessiner le panneau
		
		GL46.glStencilFunc(GL46.GL_EQUAL,1,0xFF);
		GL46.glStencilOp(GL46.GL_KEEP,GL46.GL_KEEP,GL46.GL_KEEP);
		
		liste.miseÀJour(pos, éch);
		liste.dessiner();
		
		GL46.glDisable(GL46.GL_STENCIL_TEST);
	}

}
