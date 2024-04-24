package com.gyoo.minilabo.IUG;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.util.zstd.ZDICTFastCoverParams;

import com.gyoo.minilabo.Vecteurs.Vecteur2f;

public class Liste {
	public boolean verticale = true; //La liste est-elle verticale(vrai) ou horizontale(faux)?
	public boolean tailleFixe = false; //
	public float taille = 100f;
	public float longueur = 0f;
	public Vecteur2f tampon = new Vecteur2f(10f);
	public boolean défilement = true;
	public boolean ajusterLongueurParent = true;
	
	public Vecteur2f pos = new Vecteur2f(0f); //Position de la première case en pixels
	
	private float décalageDéfilement = 0;
	
	private ArrayList<Conteneur> conteneurs = new ArrayList<>();
	private ArrayList<Liste> listes = new ArrayList<>();
	private ArrayList<Integer> indexeObjet = new ArrayList<>();
	private ArrayList<Boolean> estConteneur = new ArrayList<>();
	private ArrayList<Boolean> tailleCaseFixe = new ArrayList<>();
	private ArrayList<Float> tailleCase = new ArrayList<>();
	private ArrayList<Float> posCase = new ArrayList<>();
	
	public Liste() {}
	
	public void ajouterConteneur(Conteneur c) {
		conteneurs.add(c);
		indexeObjet.add( conteneurs.size()-1 );
		estConteneur.add(true);
		tailleCaseFixe.add(true);
		tailleCase.add(taille);
		if(indexeObjet.size() > 1) {
			posCase.add( posCase.get( posCase.size()-1 ) - 2f*taille);
		}else {
			posCase.add(verticale?pos.y:pos.x);
		}
		longueur += taille;
	}
	
	public void ajouterListe(Liste l) {
		listes.add(l);
		indexeObjet.add( listes.size()-1 );
		estConteneur.add(false);
		tailleCaseFixe.add(false);
		tailleCase.add(taille);
		if(indexeObjet.size() > 1) {
			posCase.add( posCase.get( posCase.size()-1 ) - taille);
		}else {
			posCase.add(verticale?pos.y:pos.x);
		}
		longueur += taille;
	}
	
	public void miseÀJour(Vecteur2f pp, Vecteur2f ép) {
		if(indexeObjet.size() > 0) {
			if(tailleFixe) {
				if(verticale) {
					pos.x = pp.x;
					pos.y = ép.y-tampon.y-tailleCase.get(0) + pp.y;
				}else {
					pos.x = ép.x-tampon.x-tailleCase.get(0) + pp.x;
					pos.y = pp.y;
				}
			}else {
				if(verticale) {
					pos.x = pp.x;
					pos.y = ép.y-tampon.y-tailleCase.get(0) + pp.y;
					taille = ép.x - 2f*tampon.x;
				}else {
					pos.x = ép.x-tampon.x-tailleCase.get(0) + pp.x;
					pos.y = pp.y;
					taille = ép.y-2f*tampon.y;
				}
			}
		}
		
		for(int j = 0; j < 2; j++) {
			float accumulationLongueur = 0f;
			for (int i = 0; i < indexeObjet.size(); i++) {
				
				if(i > 0) {
					if(ajusterLongueurParent) {
						posCase.set(i, (verticale?ép.y:ép.x)*( (posCase.get(i-1)-tailleCase.get(i-1) - tailleCase.get(i)) - (verticale?pos.y:pos.x) )/longueur  + (verticale?pos.y:pos.x));
					}else {
						posCase.set(i,posCase.get(i-1)-tailleCase.get(i-1) - tailleCase.get(i));
					}
				}else {
					if(ajusterLongueurParent) {
						posCase.set(0, (verticale?ép.y:ép.x)*( décalageDéfilement )/longueur + (verticale?pos.y:pos.x));
					}else {
						posCase.set(0,(verticale?pos.y:pos.x) + décalageDéfilement);
					}
				}
				
				int indexe = indexeObjet.get(i);
				if(estConteneur.get(i)) {
					if(tailleCaseFixe.get(i)) {
						conteneurs.get(indexe).miseÀJour(new Vecteur2f(verticale?pos.x:posCase.get(i),verticale?posCase.get(i):pos.y), new Vecteur2f( verticale?taille:tailleCase.get(i), verticale?tailleCase.get(i):taille ));
					}else {
						conteneurs.get(indexe).miseÀJour(new Vecteur2f(verticale?pos.x:posCase.get(i),verticale?posCase.get(i):pos.y), new Vecteur2f( verticale?taille:Float.MAX_VALUE, verticale?Float.MAX_VALUE:taille ));
						tailleCase.set(i, verticale? conteneurs.get(indexe).éch.y+conteneurs.get(indexe).tampon.y : conteneurs.get(indexe).éch.x+conteneurs.get(indexe).tampon.x);
					}
				}else {
					if(tailleCaseFixe.get(i)) {
						listes.get(indexe).miseÀJour(new Vecteur2f(verticale?pos.x:posCase.get(i),verticale?posCase.get(i):pos.y), new Vecteur2f( verticale?taille:tailleCase.get(i), verticale?tailleCase.get(i):taille ));
					}else{
						listes.get(indexe).miseÀJour(new Vecteur2f(verticale?pos.x:posCase.get(i),verticale?posCase.get(i):pos.y), new Vecteur2f( verticale?taille:Float.MAX_VALUE, verticale?Float.MAX_VALUE:taille ));
						if((listes.get(indexe).verticale && verticale) || (!listes.get(indexe).verticale && !verticale)) {
							tailleCase.set(i,listes.get(i).longueur + (verticale?listes.get(i).tampon.y:listes.get(i).tampon.x));
						}else {
							tailleCase.set(i,listes.get(i).taille + (verticale?listes.get(i).tampon.x:listes.get(i).tampon.y));
						}
					}
				}
				if(ajusterLongueurParent) {
					tailleCase.set(i,(verticale?ép.y:ép.x)*tailleCase.get(i)/longueur);
				}
				accumulationLongueur += tailleCase.get(i);
			}
			longueur = accumulationLongueur;
		}
		
		float décal = 500f*(float)Math.sin((double)System.currentTimeMillis()/1000.0);
		//décalageDéfilement = Math.max(Math.min(décal,longueur-2f*ép.y+2f*tampon.y),0f);
	}
	
	public void dessiner() {
		for (Conteneur conteneur : conteneurs) {
			conteneur.dessiner();
		}
		for (Liste liste : listes) {
			liste.dessiner();
		}
	}
}
