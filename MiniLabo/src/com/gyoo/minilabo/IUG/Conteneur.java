package com.gyoo.minilabo.IUG;

import javax.swing.text.AbstractDocument.BranchElement;

import org.lwjgl.opengl.GL46;

import com.gyoo.minilabo.Chargeur;
import com.gyoo.minilabo.Ressources;
import com.gyoo.minilabo.Vecteurs.Vecteur2f;

public class Conteneur {
	
	//Valeurs possibles de COLLER
	public enum COLLER {
		COLLER_HAUT,
		COLLER_BAS,
		COLLER_DROITE,
		COLLER_GAUCHE,
		COLLER_CENTRE
	};
	
	public Vecteur2f pos = new Vecteur2f(0); //Position du conteneur en pixels
	public Vecteur2f éch = new Vecteur2f(30f); //Échelle du conteneur. Facteur de grandeur en pixels
	
	public Vecteur2f tampon = new Vecteur2f(10f); //taille du tampon entre le conteneur et les bords du parent
	public float ratio = 1f; //Ratio largeur/hauteur du conteneur
	public boolean Xfixe = false; //Fixe la largeur en pixels. Si faux, s'ajustera à la taille du parent.
	public boolean Yfixe = false; //Fixe la hauteur en pixels. Si faux, s'ajustera à la taille du parent.
								//Si Xfixe et Y fixe sont faux, s'agrandira au maximum en conservant le ratio.
	public Vecteur2f taille = new Vecteur2f(50f); // Taille du conteneur en pixel. N'est utilisé que si Xfixe, Yfixe ou les deux sont vrais.
	public COLLER collerX = COLLER.COLLER_CENTRE; // Influence l'alignement horizontal du conteneur dans le parent. Peut ête COLLER_GAUCHE, COLLER_CENTRE ou COLLER_DROITE
	public COLLER collerY = COLLER.COLLER_CENTRE; // Influence l'alignement vertical du conteneur dans le parent. Peut être COLLER_BAS, COLLER_CENTRE ou COLLER_HAUT
	
	public Vecteur2f posParent; //Position du parent
	public Vecteur2f échParent; //Échelle du parent
	
	public int IDColorisateur; //ID du colorisateur (shader). À activer avant toute opération graphique.
	public int vaoID; //ID du Vertex Array Object. Pointe vers un liste qui contient les attributs/point.
	
	//Liste des positions des points du carré en x,y. Utilise GL_TRIANGLE_STRIP. NE PAS CHANGER L'ORDRE.
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
	
	private Image image = new Image("Images/TurtuleEdit1.png");
	
	//Initialisation
	public Conteneur(Vecteur2f pp, Vecteur2f ép) {
		posParent = pp;
		échParent = new Vecteur2f(0);
		faireColorisateur();
		faireVao();
	}
	
	private void faireColorisateur() {
		//Colorisateur de points (Vertex Shader)
		int pointColorisateur = GL46.glCreateShader(GL46.GL_VERTEX_SHADER); //Création
		GL46.glShaderSource(pointColorisateur, Chargeur.chargerTexte("Colorisateurs/Conteneur.vert.glsl")); //Liaison avec le code source
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
		GL46.glShaderSource(pixColorisateur, Chargeur.chargerTexte("Colorisateurs/Conteneur.frag.glsl")); //Liaison avec le code source
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
	
	public void miseÀJour(Vecteur2f pp, Vecteur2f ép) {
		
		posParent = pp; //Mettre à jour la position du parent
		échParent = ép; //Mettre à jour l'échelle du parent
		
		//Ajustement de l'échelle
		if(Xfixe && Yfixe) {
			//Si la largeur et la hauteur sont fixes
			éch = new Vecteur2f(taille.x,taille.y); //Simplement prendre la taille en pixels. !! NE PAS MODIFIER !! Il y a un bug où "éch = taille;" ne copie que taille.y.
		}else if(Xfixe && !Yfixe) {
			//Si la largeur est fixe, mais pas la hauteur
			éch = new Vecteur2f(taille.x, ép.y - 2f*tampon.y); //Garder la taille en X et ajuster à la taille du parent en Y
		}else if(!Xfixe && Yfixe) {
			//Si la hauteur est fixe, mais pas la largeur
			éch = new Vecteur2f(ép.x - 2f*tampon.x,taille.y); //Garder la taille en Y et ajuster à la taille du parent en X
		}else if(!Xfixe && !Yfixe) {
			//Si ni la hauteur, ni la largeur ne sont fixes. Ajuster à la taille du parent en s'assurant de garder le ratio.
			if( ép.y - tampon.y < (ép.x-tampon.x)*ratio ) {
				//Si la hauteur permise est plus petite.
				float t = Math.max(ép.y - tampon.y,10f); //S'assurer qu'il n'est pas plus petit que 10 pixels
				éch = new Vecteur2f( (t)*ratio,t); //Ajuster la taille
			}else {
				//Si la largeur permise est plus petite.
				float t = Math.max(ép.x - tampon.x,10f); //S'assurer qu'il n'est pas plus petit que 10 pixels.
				éch = new Vecteur2f(t,t/ratio); //Ajuster la taille
			}
		}
		
		//Ajuster la position
		switch(collerX) {
			case COLLER_DROITE:
				pos.x = ép.x - tampon.x - éch.x + pp.x;
				break;
			case COLLER_CENTRE:
				pos.x = pp.x;
				break;
			case COLLER_GAUCHE:
				pos.x = -ép.x + tampon.x + éch.x + pp.x;
				break;
			default:
				//Si collerX correspond à un valeur non-permise.
				System.err.println("Conteneur.miseÀJour() collerX enum " + (collerX==COLLER.COLLER_BAS?"COLLER_BAS":"COLLER_HAUT") + " invalide.");
				break;
		}
		
		switch(collerY) {
		case COLLER_HAUT:
			pos.y = ép.y - tampon.y - éch.y + pp.y;
			break;
		case COLLER_CENTRE:
			pos.y = pp.y;
			break;
		case COLLER_BAS:
			pos.y = -ép.y + tampon.y + éch.y + pp.y;
			break;
		default:
			//Si collerY correspond à une valeur non-permise.
			System.err.println("Conteneur.miseÀJour() collerX enum " + (collerX==COLLER.COLLER_DROITE?"COLLER_DROITE":"COLLER_GAUCHE") + " invalide.");
			break;
	}
	}
	
	//Dessiner le conteneur
	public void dessiner() {
		GL46.glUseProgram(IDColorisateur); //Activer les colorisateurs
		
		GL46.glBindVertexArray(vaoID); //Lier le vao avec le contexte
		GL46.glEnableVertexAttribArray(0); //Activer la liste de positions
		
		GL46.glUniform2f(COL_TRANSLATION,pos.x,pos.y); //Fournir la translation aux colorisateurs
		GL46.glUniform2f(COL_ÉCHELLE,éch.x,éch.y); //Fournir l'échelle aux colorisateurs
		GL46.glUniform2f(COL_RÉSOLUTION,ressources.résolution.x,ressources.résolution.y); //Fournir la résolution de l'écran aux colorisateurs
		
		GL46.glDrawArrays(GL46.GL_TRIANGLE_STRIP, 0, 4); //Dessiner le conteneur
		
		image.miseÀJour(pos, éch);
		image.dessiner();
	}
	
}
