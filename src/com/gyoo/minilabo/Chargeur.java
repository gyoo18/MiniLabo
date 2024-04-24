package com.gyoo.minilabo;

import java.io.File;
import java.io.FileInputStream;

/*Classe responsable pour tout ce qui est chargement de fichier
 * et transformation de données.
 * 
 * Cette classe est une instance unique (singleton), ce faisant
 * il n'en existe qu'une seule instance, partagée à travers
 * le programme et distribuée par la partie statique.*/

public class Chargeur {
	
	private static Chargeur chargeur;
	
	private Chargeur(){}
	
	//Obtenir les ressources. Fonction statique, indépendante du contexte qui distribue la version objet.
	public static Chargeur avoirChargeur() {
		if(chargeur == null) {
			chargeur = new Chargeur(); //S'assurer que la version objet est initialisée
		}
		return chargeur;
	}
	
	//Charger un fichier sous forme de texte depuis le dossier des ressources.
	public static String chargerTexte(String rép) {
		File file = new File(System.getProperty("user.dir")+ "/src/ressources/" + rép); //Ouverture du fichier
		String doc = null;
		try {
			FileInputStream is = new FileInputStream(file);
			doc = new String(is.readAllBytes()); //Lecture du fichier et transformation en String
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return doc;
	}
	
}
