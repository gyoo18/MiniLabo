package com.gyoo.minilabo;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.lwjgl.opengl.GL46;
import org.lwjgl.stb.STBImage;

import com.gyoo.minilabo.Vecteurs.Vecteur2f;

/* Classe qui conserve toutes les ressources qui doivent
 * être disponibles à travers différentes parties du programme.
 * Il s'agit souvent de données sur le contexte. 
 * 
 * Cette classe est une instance unique (singleton), ce faisant
 * il n'en existe qu'une seule instance, partagée à travers
 * le programme et distribuée par la partie statique.*/


public class Ressources {
	
	public Vecteur2f résolution; //Résolution de l'écran
	public Vecteur2f curseurPos = new Vecteur2f(0); //Position du curseur
	public boolean clicDroitEnfoncé = false;
	public boolean clicGaucheEnfoncé = false;
	public boolean clicMilieuEnfoncé = false;
	
	private ArrayList<Integer> images = new ArrayList<>();
	private ArrayList<Vecteur2f> imageRes = new ArrayList<>();
	private ArrayList<String> imageRéps = new ArrayList<String>();
	
	//Instance de la classe en version objet, possédée par la version statique. La version objet possède toutes les données.
	private static Ressources ressources;
	
	//Initialisation
	private Ressources () {}
	
	//Obtenir les ressources. Fonction statique, indépendante du contexte qui distribue la version objet.
	public static Ressources avoirRessources() {
		if(ressources == null) {
			ressources = new Ressources(); //S'assurer que la version objet est initialisée
		}
		return ressources;
	}

	public int[] avoirImage(String rép) {
		if(imageRéps.contains(rép)) {
			int indexe = imageRéps.indexOf(rép);
			return new int[] {images.get(indexe),(int) imageRes.get(indexe).x,(int) imageRes.get(indexe).y};
		}else {
			int largeur = 0;
			int hauteur = 0;
			//Charger l'image depuis la mémoire.
			ByteBuffer tampon = null;
			
			try {
				File image = new File(System.getProperty("user.dir")+ "/src/ressources/" + rép); //Ouvrir le fichier
				FileInputStream fis = new FileInputStream(image);
				tampon = ByteBuffer.wrap(fis.readAllBytes()); //Verser toutes les données dans un ByteBuffer
				int[] l = new int[1];
				int[] h = new int[1];
				int[] comp = new int[1];
				//Décompresser l'image
				tampon = STBImage.stbi_load( System.getProperty("user.dir")+ "/src/ressources/" + rép, l, h, comp,STBImage.STBI_rgb_alpha);
				largeur = l[0];
				hauteur = h[0];
				fis.close(); //Fermer le fichier
			}catch(Exception e) {
				System.err.println("Uh-Oh...");
				e.printStackTrace();
			}
			
	        int IDTexture = GL46.glGenTextures(); //Créer l'élément de texture OpenGL
	        GL46.glBindTexture(GL46.GL_TEXTURE_2D, IDTexture); //Lier l'élément de texture
	        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MIN_FILTER, GL46.GL_LINEAR_MIPMAP_LINEAR); //Changer les paramètres de rapetissement
	        GL46.glTexParameteri(GL46.GL_TEXTURE_2D, GL46.GL_TEXTURE_MAG_FILTER, GL46.GL_LINEAR); //Changer les paramètres d'agrandissement
	        GL46.glTexImage2D(GL46.GL_TEXTURE_2D,0,GL46.GL_RGBA,largeur,hauteur,0,GL46.GL_RGBA,GL46.GL_UNSIGNED_BYTE,tampon); //Lier les données à l'élément de texture
	        GL46.glGenerateMipmap(GL46.GL_TEXTURE_2D); //Générer les mipmaps
	        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0); //Délier l'élément de texture
	        
	        imageRéps.add(rép);
	        images.add(IDTexture);
	        imageRes.add(new Vecteur2f(largeur,hauteur));
	        return new int[] {IDTexture,largeur,hauteur};
		}
	}
}
