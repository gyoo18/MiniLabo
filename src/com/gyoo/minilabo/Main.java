package com.gyoo.minilabo;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;

import com.gyoo.minilabo.IUG.GestionnaireIUG;
import com.gyoo.minilabo.Vecteurs.Vecteur2f;

public class Main {
	
	private static int compteurFPS = 0;
	
	private static GLView glView;
	
	public static void main(String[] args) {
		System.out.println("Bienvenue dans MiniLabo");
		
		Ressources ressources = Ressources.avoirRessources();
		
		//Initialiser GLFW
		if(!GLFW.glfwInit()) {throw new IllegalStateException("GLFW n'a  pas pus être initialisé");}
		
		//Permettre le recadrage de la fenêtre
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		
		//Créer la fenêtre
		long window = 0;
		window = GLFW.glfwCreateWindow(512, 512, "Bonjour Triangle", 0, 0);
		ressources.résolution = new Vecteur2f(512f);
		if(window == 0) {
			throw new RuntimeException("La fenêtre n'a pas pus être créé");
		}
		
		//Activer la Sync-V
		GLFW.glfwSwapInterval(1);
		
		//Initialiser OpenGL
		GLFW.glfwMakeContextCurrent(window);	
		GL.createCapabilities();
		
		//Initialiser la classe de rendu
		glView = new GLView();
		GestionnaireIUG gérantIUG = new GestionnaireIUG( new Vecteur2f(0), Vecteur2f.mult( ressources.résolution,0.5f) );
		
		
		//Initialiser des moniteurs d'évenements
		//Moniteur de taille de fenêtre
		GLFWWindowSizeCallback wsc = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long fenêtre, int largeur, int hauteur) {
				glView.mettreÀJourFenêtre(largeur, hauteur);
				ressources.résolution = new Vecteur2f((float)largeur, (float)hauteur);
				gérantIUG.ajusterParent(new Vecteur2f(0), Vecteur2f.mult( ressources.résolution,0.5f) );
			}
		};
		GLFW.glfwSetWindowSizeCallback(window, wsc);
		
		//Moniteur de position de souris
		GLFWCursorPosCallback cpc = new GLFWCursorPosCallback() {
			
			@Override
			public void invoke(long fenêtre, double x, double y) {
				float posx = (float)x - ressources.résolution.x * 0.5f;
				float posy = ressources.résolution.y*0.5f - (float)y;
				ressources.curseurPos = new Vecteur2f( posx, posy );
				gérantIUG.ajusterCurseur( new Vecteur2f( posx, posy ) );
			}
		};
		GLFW.glfwSetCursorPosCallback(window, cpc);
		
		GLFWMouseButtonCallback mbc = new GLFWMouseButtonCallback() {
			
			@Override
			public void invoke(long window, int clickGauche, int clickDroit, int clickMilieu) {
				System.out.println(window + ", " + clickDroit + ", " + clickGauche + ", " + clickMilieu + ".");
				ressources.clicDroitEnfoncé = (clickDroit == 1)? true: false;
				ressources.clicGaucheEnfoncé = (clickGauche == 1)? true: false;
				ressources.clicMilieuEnfoncé = (clickMilieu == 1)? true: false;
				gérantIUG.ajusterCurseur(ressources.curseurPos);
			}
		};
		GLFW.glfwSetMouseButtonCallback(window, mbc);
		
		
		//Boucle de rendus
		long timer = System.currentTimeMillis();
		long timerFPS = System.currentTimeMillis();
				
		while(!GLFW.glfwWindowShouldClose(window)) {
						
			//Dessiner la fenêtre
			glView.dessiner(gérantIUG);
			
			//Afficher le dessin
			GLFW.glfwSwapBuffers(window);
			//Mettre à jour les moniteurs
			GLFW.glfwPollEvents();
			
			//Verrouillage de FPS
			if(System.currentTimeMillis() - timer < 33) {
				try {
					Thread.sleep( 33 - (System.currentTimeMillis() - timer) );
					timer = System.currentTimeMillis();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			compteurFPS ++;
			if(compteurFPS > 60) {
				System.out.println("FPS : " + 1f/( (float)(System.currentTimeMillis()-timerFPS)/(1000f*60f)));
				compteurFPS = 0;
				timerFPS = System.currentTimeMillis();
			}
		}
		
		//Lorsque sortis de la boucle de rendus, détruire la fenêtre
		GLFW.glfwDestroyWindow(window);
		
	}

}
