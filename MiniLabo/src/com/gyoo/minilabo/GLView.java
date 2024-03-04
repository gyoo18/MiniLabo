package com.gyoo.minilabo;

import org.lwjgl.opengl.GL46;

import com.gyoo.minilabo.IUG.GestionnaireIUG;
import com.gyoo.minilabo.IUG.Panneau;

public class GLView {
	//Initialisation
	public GLView(){
		//Couleur d'arrière-plan
		GL46.glClearColor(0.5f, 0.5f, 0.5f, 1f);
		GL46.glEnable(GL46.GL_BLEND);
		GL46.glBlendFunc(GL46.GL_SRC_ALPHA,GL46.GL_ONE_MINUS_SRC_ALPHA);
		GL46.glClearStencil(0);
	}
	
	//Mise à jour de la taille de la fenêtre
	public void mettreÀJourFenêtre(int largeur, int hauteur) {
		GL46.glViewport(0, 0, largeur, hauteur);
	}
	
	//Dessiner la fenêtre
	public void dessiner(GestionnaireIUG gI){
		
		//Effacer la trame précédente
		GL46.glClear( GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT );
		
		for (Panneau p : gI.panneaux) {
			if(p != null) {
				// Si le panneau existe
				p.dessiner(); //Dessiner tout les panneaux du gestionnaire d'IUG
			}
		}
		
		gI.barre.dessiner();
				
		//Vérifier les erreurs OpenGL
		int error = GL46.glGetError();
		switch(error){
		    case GL46.GL_INVALID_ENUM: System.err.println("GLError | INVALID ENUM");
		        break;
		    case GL46.GL_INVALID_VALUE: System.err.println("GLError | INVALID VALUE");
		        break;
		    case GL46.GL_INVALID_OPERATION: System.err.println("GLError | INVALID OPERATION");
		        break;
		    case GL46.GL_INVALID_FRAMEBUFFER_OPERATION: System.err.println("GLError | INVALID FRAMEBUFFER OPERATION");
		        break;
		    case GL46.GL_OUT_OF_MEMORY: System.err.println("GLError | OUT OF MEMORY");
		        break;
		}
		
	}

}
