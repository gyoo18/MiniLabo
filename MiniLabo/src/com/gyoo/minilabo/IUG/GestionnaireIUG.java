package com.gyoo.minilabo.IUG;

import java.util.ArrayList;

import com.gyoo.minilabo.Ressources;
import com.gyoo.minilabo.Vecteurs.V2f;
import com.gyoo.minilabo.Vecteurs.Vecteur2f;

/*Cette classe gère les éléments d'interface utilisateur situés à l'intérieur du parent.
 * Le parent peut être la fenêtre ou un autre panneau.
 * Le curseur peut ajuster la taille des panneaux en cliquant du leurs côtés et peut ajouter/retirer
 * des panneaux en cliquant sur le coin de l'un d'entre eux et en glissant vers l'intérieur pour en 
 * créer un nouveau et vers l'extérieur pour en retirer un.*/

public class GestionnaireIUG {
	
	//Repère pixel : fait référence aux pixels de l'écran
	//Repère (-1;+1) : coordonnées entre -1 et +1, -1 étant la borne inférieure du parent et +1 étant la borne supérieure.
	//parent -> [-1.....0..... 0.5 (position) .....+1]
	
	public Vecteur2f parentPos; //Position du parent en pixels.
	public Vecteur2f parentÉch; //Échelle du parent en pixels.
	
	public ArrayList<Panneau> panneaux = new ArrayList<>(); //Liste des panneaux.
	public Barre barre = new Barre();
	public float barreTaille = 30f;
	
	private ArrayList<Float> CôtéPos = new ArrayList<>(); //Position du côté sur son axe en (-1;+1). Les côtés sont perpendiculaires à leur axe.
	private ArrayList<Boolean> CôtéOr = new ArrayList<>(); //Orientation de la translation du côté. x = faux, y = vrai.
	private ArrayList<ArrayList<Integer>> CôtéIndexePanneau = new ArrayList<>(); // indexes des panneaux que les côtés modifients
	private ArrayList<int[]> PanneauIndexeCôté = new ArrayList<>(); // indexes des côtés qui modifient les panneaux
	
	private Ressources ressources = Ressources.avoirRessources();
	
	private boolean côtéAttrapé = false; //Indique si le curseur a cliqué sur un côté
	private int côtéAttrapéI = -1; //Indexe du côté cliqué
	
	private boolean coinAttrapé = false; //Indique si un coin a été cliqué
	private Vecteur2f curPosCoin = null; //La position du curseur au moment su clic sur un coin en pixels.
	private int coinAttrapéCôtéX = 0; //Indexe du côté X du coin
	private int coinAttrapéCôtéY = 0; //Indexe du côté Y du coin
	private int coinAttrapéPanneau = -1; //Indexe du panneau contenant le coin
	private int coinAttrapéPanneau2 = -1; //Indexe du panneau adjascent
	private boolean coinAttrapéPanneauxAxe = false; //Indique l'axe sur lequel les deux panneaux sont liés. S'ils sont côt-à-côte, c'est x, s'ils sont l'un par-dessus l'autre, c'est y. faux = x, vrai =y.
	
	//Initialisation (Position du parent, Échelle du parent)
	public GestionnaireIUG(Vecteur2f pPos, Vecteur2f pÉch) {
		parentPos = pPos;
		parentÉch = pÉch;
		
		Panneau p = new Panneau(); //Ajouter un panneau.
		panneaux.add(p);
		PanneauIndexeCôté.add( new int[] {0, 1, 2, 3} ); //Ajouter des références à ses côtés.
		
		//Ajouter un panneau
		CôtéPos.add( (parentPos.x + parentÉch.x)/parentÉch.x ); //Position : +1
		CôtéOr.add(false); 										//Axe : x
		ArrayList<Integer> al = new ArrayList<>();
		al.add(0);
		CôtéIndexePanneau.add(al); // Ajouter référence au panneau
		
		CôtéPos.add( (parentPos.y + parentÉch.y - barreTaille)/parentÉch.y ); //Position : +1, - la taille du panneau
		CôtéOr.add(true);										//Axe : y
		al = new ArrayList<>();
		al.add(0);
		CôtéIndexePanneau.add(al); //Ajouter référence au panneau
		
		CôtéPos.add( (parentPos.x - parentÉch.x)/parentÉch.x ); //Position : -1
		CôtéOr.add(false);										//Axe : x
		al = new ArrayList<>();
		al.add(0);
		CôtéIndexePanneau.add(al); //Ajouter référence au panneau
		
		CôtéPos.add( (parentPos.y - parentÉch.y)/parentÉch.y ); //Position : -1
		CôtéOr.add(true);										//Axe : y
		al = new ArrayList<>();
		al.add(0);
		CôtéIndexePanneau.add(al); //Ajouter référence au panneau
		
		barre.pos = new Vecteur2f(parentPos.x,parentÉch.y-(barreTaille*0.5f));
		barre.éch = new Vecteur2f(parentÉch.x,(barreTaille)*0.5f);
		
		ajusterPanneaux();
	}
	
	public void ajusterParent(Vecteur2f pPos, Vecteur2f pÉch) {
		parentPos = pPos;
		parentÉch = pÉch;
		ajusterPanneaux(); //Mettre à jour les panneaux
	}
	
	//Interractions entre le curseur et les panneaux
	public void ajusterCurseur(Vecteur2f curPos) {
		if(ressources.clicDroitEnfoncé) {
			//Si le curseur est cliqué
			if(!côtéAttrapé && !coinAttrapé) {
				//Si le curseur n'a pas un côté ou un coin de cliqué
				for (int i = 0; i < CôtéPos.size(); i++) {
					//Pout tout les côtés
					
					//Vérifier si le curseur à cliqué un coin.
					for (int j = 0; j < CôtéPos.size(); j++) {
						//Pour tout les côtés
						if(CôtéOr.get(i) != CôtéOr.get(j)) {
							//Si les deux côtés ne sont pas orientés dans la même direction. Évite la situation i=j.
							int ix = CôtéOr.get(i)? j:i; //Si i est x => ix = j, sinon ix = i. Contient l'index du côté orienté x.
							int iy = CôtéOr.get(i)? i:j; //Si i est y => iy = i, sinon iy = j. Contient l'index du côté orienté y.
							
							//On divise la position du curseur par l'échelle du parent pour obtenir sa position en (-1;+1), puis on obtient la distance entre le curseur et le côté.
							//Si la distance est plus petite que (20 pixels divisés par la taille du parent pour l'avoir en (-1;+1)), on procède.
							//On vérifie que le curseur est à l'intérieur des limites des deux axes, indiquant qu'il se trouve dans un coin.
							if(Math.abs( (curPos.x/parentÉch.x)-CôtéPos.get(ix) ) < 20f/parentÉch.x && Math.abs( (curPos.y/parentÉch.y)-CôtéPos.get(iy) ) < 20f/parentÉch.y) {
								//Si le curseur a cliqué dans un coin
								coinAttrapé = true;
								curPosCoin = curPos.copier(); //Stocke la position du curseur lors du clic.
								coinAttrapéCôtéX = ix; //Stocke l'indexe du côté x
								coinAttrapéCôtéY = iy; //Stocke l'indexe du côté y
								
								//curDir représente le vecteur entre l'intersection des deux côtés et le curseur, en (-1;+1)
								Vecteur2f curDir = Vecteur2f.sous( Vecteur2f.div(Vecteur2f.sous(curPos,parentPos),parentÉch), new Vecteur2f( CôtéPos.get(ix), CôtéPos.get(iy) ) );
								int ixc = curDir.x > 0? 2 : 0; //Si curDir.x > 0 => ixc = 2, sinon ixc = 0. Représente l'indexe de référence du côté x depuis le panneau cliqué.
								int iyc = curDir.y > 0? 3 : 1; //Si curDir.y > 0 => iyc = 3, sinon iyc = 1. Représente l'indexe de référence du côté y depuis le panneau cliqué.
								//Si on trouve les références aux côtés aux bons endroits dans le panneau, alors nous savons que c'est celui que le curseur a cliqué.
								//Par contre, on n'a qu'à vérifier les panneaux auxquels les côtés font références, puisque les autres ne les touchent pas.
								for (int k = 0; k < CôtéIndexePanneau.get(ix).size(); k++) {
									//Pour tout les panneaux du côté x
									for(int l = 0; l < CôtéIndexePanneau.get(iy).size(); l++) {
										//Pour tout les panneaux du côté y
										int ixp = CôtéIndexePanneau.get(ix).get(k); //Le panneau k du côté x
										int iyp = CôtéIndexePanneau.get(iy).get(l); //Le panneau l du côté y
										if(panneaux.get(ixp) == panneaux.get(iyp)) {
											//Si k et l font référence au même panneau, cela implique que ce panneau touche les deux côtés.
											//Seuls deux panneaux peuvent remplir cette condition : celui qui est cliqué et celui qui lui est adjascent.
											//Si le deuxième panneau existe, les côtés seront organisés en T, l'un d'eux représentant une interface entre les deux panneaux,
											//	l'autre limitant la première sur un de ses côtés.
											//Les deux côtés touchent les deux panneaux, ils faut donc les départager.
											if(PanneauIndexeCôté.get(ixp)[ixc] == ix && PanneauIndexeCôté.get(iyp)[iyc] == iy) {
												//Si les côtés sont dans la configuration attendue.
												coinAttrapéPanneau = ixp; //Le panneau est celui cliqué.
											}else {
												//Si les côtés ne sont pas dans la configuration attendue.
												coinAttrapéPanneau2 = ixp; //Le panneau est celui adjascent. !! N'est pas garantis d'exister !! coinAttrapéPanneau2= -1 si n'existe pas.
												//Il faut maintenant déterminer l'axe qu'ils partagent
												//L'un des deux côté sera dans la même position relative que celui du panneau cliqué.
												//Ce ne serat pas celui qui est une interface entre les deux panneaux.
												//Si c'est celui de l'axe x, alors l'interface est en y et vice-versas.
												if(PanneauIndexeCôté.get(ixp)[ixc] == ix) {
													coinAttrapéPanneauxAxe = true; //Si c'est l'axe x, indiquer l'axe y.
												}else {
													coinAttrapéPanneauxAxe = false; //Si c'est l'axe y, indiquer l'axe x.
												}
											}
										}
									}
								}
							}
						}
					}
					
					//Si le curseur n'a pas cliqué sur un coin, on vérifie s'il a cliqué sur un côté.
					if(!CôtéOr.get(i) && !coinAttrapé) {
						//Si l'axe du côté est X
						//On divise la position du curseur par l'échelle du parent pour obtenir sa position en (-1;+1), puis on obtient la distance entre le curseur et le côté.
						//Si la distance est plus petite que (20 pixels divisés par la taille du parent pour l'avoir en (-1;+1)), on procède.
						if(Math.abs( (curPos.x/parentÉch.x)-CôtéPos.get(i) ) < 20f/parentÉch.x) {
							//Si le curseur a cliqué un côté
							côtéAttrapé = true;
							côtéAttrapéI = i; //Stocker l'indexe du côté.
						}
					}else if(!coinAttrapé) {
						//Si l'axe du côté est Y
						if(Math.abs( (curPos.y/parentÉch.y)-CôtéPos.get(i) ) < 20f/parentÉch.y) {
							//Si le curseur a cliqué un côté
							côtéAttrapé = true;
							côtéAttrapéI = i; //Stocker l'indexe du côté
						}
					}
				}
			}else if(côtéAttrapé) {
				//Si un côté est cliqué.
				//Vérifier la collision avec les autres côtés et les bords du parent.
				float BorneP = 1000f; //Indique la position, en (-1;+1), du côté dans la direction positive le plus proche
				float BorneN = -1000f; //Indique la position, en (-1;+1), du côté dans la direction négative le plus proche 
				float pos = CôtéPos.get(côtéAttrapéI); //Position du côté cliqué
				for (int i = 0; i < CôtéPos.size(); i++) {
					//Pour tout les côtés
					if(i != côtéAttrapéI && CôtéOr.get(côtéAttrapéI) == CôtéOr.get(i)) {
						//Si i n'est pas le côté cliqué et que l'axe de i est le même que celui cliqué. (On ne collisionne pas avec un axe dans l'autre sens).
						//Vérifier que i touche le même panneau. (On ne collisionne pas avec un côté qui n'est pas dans notre chemin, ex.: [ | | ] collisionnent, mais [--][--] ne collisionnent pas).
						boolean mêmePanneau = false;
						for (int j = 0; j < CôtéIndexePanneau.get(i).size(); j++) {
							//Pour tout les panneaux de i
							if( CôtéIndexePanneau.get(côtéAttrapéI).contains( CôtéIndexePanneau.get(i).get(j) ) ) {
								//Si le côté cliqué contient une référence au panneau j, les deux côtés touchent le même panneau.
								mêmePanneau = true;
								break; //Inutile de vérifier pour d'autres panneaux, sortir de la boucle.
							}
						}
						//Vérifier s'il n'y a pas déjà un côté plus proche.
						if(CôtéPos.get(i)-pos < BorneP-pos && CôtéPos.get(i)-pos > 0 && mêmePanneau) {
							//Si la distance dans les positifs est plus petite, qu'elle est positive et qu'ils touchent le même panneau.
							BorneP = CôtéPos.get(i); //Changer de position positive la plus proche.
						}else if(pos-CôtéPos.get(i) < pos-BorneN && pos-CôtéPos.get(i) > 0 && mêmePanneau) {
							//Si la distance dans les négatifs est plus petite, qu'elle est positive et qu'ils touchent le même panneau.
							BorneN = CôtéPos.get(i); //Changer de position positive la plus proche.
						}
					}
				}
				
				//Si on a trouvé des bornes, bouger le côté vers le curseur.
				//Sinon, ça veux dire que le côté touche le bord du parent et il ne faut pas le bouger, de peine de révéler l'arrière-plan inexistant.
				if(BorneN != -1000f && BorneP != 1000f) {
					//Si on a trouvé des bornes
					if(!CôtéOr.get(côtéAttrapéI)) {
						//Si l'axe du côté est X
						float posFin = curPos.x/parentÉch.x; //Convertir la position de pixels vers(-1;+1)
						if(posFin > BorneN + 20f/parentÉch.x && posFin < BorneP - 20f/parentÉch.x) {
							//Si le curseur se trouve à plus de 20 pixels des bornes
							CôtéPos.set(côtéAttrapéI,posFin); //Bouger le côté
						}
					}else {
						//Si l'axe du côté est Y
						float posFin = curPos.y/parentÉch.y; //Convertir la position de pixels vers(-1;+1)
						if(posFin > BorneN + 20f/parentÉch.y && posFin < BorneP - 20f/parentÉch.y) {
							//Si le curseur se trouve à plus de 20 pixels des bornes
							CôtéPos.set(côtéAttrapéI,posFin); //Bouger le côté
						}
					}
				}
			}else if(coinAttrapé) {
				//Si un coin est cliqué
				//Ne rien faire tant que le curseur ne s'est pas déplacé suffisamment.
				if(Vecteur2f.distance(curPos, curPosCoin) > 10f) {
					//Si le curseur s'est déplacé de plus de 10 pixels. (P.S. V2f = Vecteur2f, mais en plus court)
					//Calculer le vecteur entre le coin et la position du curseur au moment du clic, en (-1;+1)
					Vecteur2f curPosCoinRel = V2f.sous( V2f.div(V2f.sous(curPosCoin,parentPos),parentÉch), new V2f( CôtéPos.get(coinAttrapéCôtéX), CôtéPos.get(coinAttrapéCôtéY) ) );
					
					//Calculer le vecteur de déplacement du curseur en pixels
					Vecteur2f voyagé = V2f.sous(curPos,curPosCoin);
					//Calculer le vecteur entre le coin et le curseur, en (-1;+1), s'il voyageait 1000 fois plus loin (pour éviter la situation où il se dirige vers l'extérieur, mais ne sort pas du panneau)
					Vecteur2f curPosRel = V2f.sous( V2f.div( V2f.sous( V2f.addi( curPos, V2f.mult( voyagé, 1000f ) ), parentPos ),parentÉch ), new V2f( CôtéPos.get( coinAttrapéCôtéX ),CôtéPos.get( coinAttrapéCôtéY ) ) );	
					//Si une composante de curPosCoinRel multiplié par cette composante de curPosRel est négative, cela veut dire que le curseur a traversé le côté (car elle ne serat négative que si le signe est différent et le zéro est situé sur le côté).
					//Cela veut dire qu'il tente de fermer le panneau adjascent
					//De plus, il faut s'assurer qu'il soit sortis du panneau sur le même axe que celui vers lequel il se déplace, sinon cela résulte en du comportement indéfinis.
					if( ( (curPosRel.x * curPosCoinRel.x < 0f && Math.abs(voyagé.x)>Math.abs(voyagé.y) ) || (curPosRel.y * curPosCoinRel.y < 0f && Math.abs(voyagé.y)>Math.abs(voyagé.x))) && coinAttrapéPanneau2 != -1) {
						//Si le curseur est dirigé vers l'extérieur et que le panneau adjascent existe
						coinAttrapé = false;
						
						//Il faut s'assurer que le panneau adjascent touche le panneau cliqué sur toute la longeur de sa surface,
						//car sinon cela veut dire qu'on tente de glisser le panneau sur plusieurs panneau, ce qui consiste en du comportement indéfini.
						//Il faut aussi s'assurer que le curseur se dirige en direction du panneau adjascent, car sinon il se dirige vers un panneau plus grand,
						//ce qui consiste en du comportement indéfini.
						if(Math.abs(voyagé.x)>Math.abs(voyagé.y) && !coinAttrapéPanneauxAxe && panneaux.get(coinAttrapéPanneau).avoirÉch().y == panneaux.get(coinAttrapéPanneau2).avoirÉch().y) {
							//Si le curseur a glissé en direction des X, que l'axe de l'interface est X et que les deux panneaux ont la même hauteur.
							PanneauIndexeCôté.get(coinAttrapéPanneau)[voyagé.x > 0? 0 : 2] = PanneauIndexeCôté.get(coinAttrapéPanneau2)[voyagé.x > 0? 0 : 2]; //Assigner le côté du fond du panneau adjascent au panneau cliqué (remplir son espace)
							int pi = CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[voyagé.x > 0? 0 : 2]).indexOf(coinAttrapéPanneau2); //Obtenir l'indexe du côté du fond.
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[voyagé.x > 0? 0 : 2]).set(pi,coinAttrapéPanneau); //Assigner le panneau cliqué au côté du fond.
							
							//Retirer la référence au panneau adjascent de tout les côtés qu'il touche.
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[0]).remove(Integer.valueOf(coinAttrapéPanneau2)); //Il pourrait y a confusion entre ArrayList.remove(Object int) et ArrayList.remove(int index)
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[1]).remove(Integer.valueOf(coinAttrapéPanneau2)); //Alors on force Object int par la formulation Integer.valueOf(int).
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[2]).remove(Integer.valueOf(coinAttrapéPanneau2));
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[3]).remove(Integer.valueOf(coinAttrapéPanneau2));
							
							panneaux.set(coinAttrapéPanneau2,null); //Détruire le panneau adjascent.
							CôtéPos.set(coinAttrapéCôtéX,-1000f); //Envoyer le côté très loin pour qu'il ne bloque plus le chemin.
							//On ne retire pas les cases de la liste, puisqu'il faudrait réarranger tout les indexes par la suite.
						}else if(Math.abs(voyagé.y)>Math.abs(voyagé.x) && coinAttrapéPanneauxAxe && panneaux.get(coinAttrapéPanneau).avoirÉch().x == panneaux.get(coinAttrapéPanneau2).avoirÉch().x) {
							//Si le curseur a glissé en direction des Y, que l'axe de l'interface est Y et que les deux panneaux ont la même largeur.
							PanneauIndexeCôté.get(coinAttrapéPanneau)[voyagé.y > 0? 1 : 3] = PanneauIndexeCôté.get(coinAttrapéPanneau2)[voyagé.y > 0? 1 : 3]; //Assigner le côté du fond du panneau adjascent au panneau cliqué (remplir son espace)
							int pi = CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[voyagé.y > 0? 1 : 3]).indexOf(coinAttrapéPanneau2); //Obtenir l'indexe du côté du fond.
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[voyagé.y > 0? 1 : 3]).set(pi,coinAttrapéPanneau); //Assigner le panneau cliqué au côté du fond.
							
							//Retirer la référence au panneau adjascent de tout les côtés qu'il touche.
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[0]).remove(Integer.valueOf(coinAttrapéPanneau2));
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[1]).remove(Integer.valueOf(coinAttrapéPanneau2));
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[2]).remove(Integer.valueOf(coinAttrapéPanneau2));
							CôtéIndexePanneau.get(PanneauIndexeCôté.get(coinAttrapéPanneau2)[3]).remove(Integer.valueOf(coinAttrapéPanneau2));
							
							panneaux.set(coinAttrapéPanneau2,null); //Détruire le panneau adjascent.
							CôtéPos.set(coinAttrapéCôtéY,-1000f); //Envoyer le côté très loin pour qu'il ne bloque plus le chemin.
						}
					}else if( !( curPosRel.x * curPosCoinRel.x < 0f || curPosRel.y * curPosCoinRel.y < 0f ) ){
						//Si le curseur n'est pas sortis du panneau.
						//Dupliquer le panneau.
						coinAttrapé = false;
						côtéAttrapé = true; //Donner le nouveau côté au curseur, qu'il puisse le glisser.
					
						if(Math.abs(voyagé.x)>Math.abs(voyagé.y)) {
							//Si le curseur a glissé en direction des X
							Panneau p = new Panneau(); //créer un nouveau panneau
							panneaux.add(p);
							
							//Assigner le nouveau panneau aux côtés du haut et du bas de l'ancien panneau.
							int indexHaut = PanneauIndexeCôté.get(coinAttrapéPanneau)[1]; //Obtenir l'index du côté haut
							int indexBas = PanneauIndexeCôté.get(coinAttrapéPanneau)[3]; //Obtenir l'index du côté bas
							CôtéIndexePanneau.get(indexHaut).add(panneaux.size()-1); //Assigner le nouveau panneau au côté haut
							CôtéIndexePanneau.get(indexBas).add(panneaux.size()-1); //Assigner le nouveau panneau au côté bas
							
							//Assigner le nouveau panneau au côté cliqué en X.
							CôtéIndexePanneau.get(coinAttrapéCôtéX).set(CôtéIndexePanneau.get(coinAttrapéCôtéX).indexOf(coinAttrapéPanneau),panneaux.size()-1);
							
							CôtéPos.add(CôtéPos.get(coinAttrapéCôtéX)); //Créer un nouveau côté
							CôtéOr.add(false);							//Assigner l'axe X
							CôtéIndexePanneau.add(new ArrayList<Integer>());
							CôtéIndexePanneau.get(CôtéIndexePanneau.size()-1).add(coinAttrapéPanneau); //Assigner le panneau cliqué au nouveau côté
							CôtéIndexePanneau.get(CôtéIndexePanneau.size()-1).add(panneaux.size()-1); //Assigner le nouveau panneau au nouveau côté
							CôtéPos.set(CôtéPos.size() - 1, curPos.x/parentÉch.x); //Bouger le nouveau côté au curseur
							
							côtéAttrapéI = CôtéPos.size() - 1; //Stocker l'indexe du nouveau côté en tant que côté cliqué
							
							int indexDroite = voyagé.x < 0? coinAttrapéCôtéX : CôtéPos.size() - 1; //Représente l'indexe du côté droit du nouveau panneau 
							int indexGauche = voyagé.x > 0? coinAttrapéCôtéX : CôtéPos.size() - 1; //Représente l'indexe du côté gauche du nouveau panneau
							PanneauIndexeCôté.add(new int[] {indexDroite,indexHaut,indexGauche,indexBas}); //Assigner les côtés au nouveau panneau
							PanneauIndexeCôté.get(coinAttrapéPanneau)[voyagé.x > 0? 2 : 0] = CôtéPos.size()-1; //Assigner le nouveau côté au panneau cliqué
							
							//Ajuster la taille et l'échelle du nouveau panneau en fonction de ses côtés
							p.pos = new Vecteur2f( (CôtéPos.get(indexGauche) + CôtéPos.get(indexDroite))*0.5f*parentÉch.x, (CôtéPos.get(indexHaut) + CôtéPos.get(indexBas))*0.5f*parentÉch.y );
							p.éch = new Vecteur2f( (CôtéPos.get(indexGauche) - CôtéPos.get(indexDroite))*0.5f*parentÉch.x, (CôtéPos.get(indexHaut) - CôtéPos.get(indexBas))*0.5f*parentÉch.y );
						}else {
							//Si le curseur a glissé en direction des Y
							Panneau p = new Panneau(); //créer un nouveau panneau
							panneaux.add(p);
							
							//Assigner le nouveau panneau aux côtés du haut et du bas de l'ancien panneau.
							int indexDroite = PanneauIndexeCôté.get(coinAttrapéPanneau)[0]; //Obtenir l'index du côté haut
							int indexGauche = PanneauIndexeCôté.get(coinAttrapéPanneau)[2]; //Obtenir l'index du côté bas
							CôtéIndexePanneau.get(indexDroite).add(panneaux.size()-1); //Assigner le nouveau panneau au côté haut
							CôtéIndexePanneau.get(indexGauche).add(panneaux.size()-1); //Assigner le nouveau panneau au côté bas
							
							//Assigner le nouveau panneau au côté cliqué en X.
							CôtéIndexePanneau.get(coinAttrapéCôtéY).set(CôtéIndexePanneau.get(coinAttrapéCôtéY).indexOf(coinAttrapéPanneau),panneaux.size()-1);
							
							CôtéPos.add(CôtéPos.get(coinAttrapéCôtéY)); //Créer un nouveau côté
							CôtéOr.add(true);							//Assigner l'axe X
							CôtéIndexePanneau.add(new ArrayList<Integer>());
							CôtéIndexePanneau.get(CôtéIndexePanneau.size()-1).add(coinAttrapéPanneau); //Assigner le panneau cliqué au nouveau côté
							CôtéIndexePanneau.get(CôtéIndexePanneau.size()-1).add(panneaux.size()-1); //Assigner le nouveau panneau au nouveau côté
							CôtéPos.set(CôtéPos.size() - 1, curPos.y/parentÉch.y); //Bouger le nouveau côté au curseur
							
							côtéAttrapéI = CôtéPos.size() - 1; //Stocker l'indexe du nouveau côté en tant que côté cliqué
							
							int indexHaut = voyagé.y > 0? CôtéPos.size() - 1 : coinAttrapéCôtéY; //Représente l'indexe du côté droit du nouveau panneau 
							int indexBas = voyagé.y < 0? CôtéPos.size() - 1: coinAttrapéCôtéY; //Représente l'indexe du côté gauche du nouveau panneau
							PanneauIndexeCôté.add(new int[] {indexDroite,indexHaut,indexGauche,indexBas}); //Assigner les côtés au nouveau panneau
							PanneauIndexeCôté.get(coinAttrapéPanneau)[voyagé.y > 0? 3 : 1] = CôtéPos.size()-1; //Assigner le nouveau côté au panneau cliqué
							
							//Ajuster la taille et l'échelle du nouveau panneau en fonction de ses côtés
							p.pos = new Vecteur2f( (CôtéPos.get(indexBas) + CôtéPos.get(indexHaut))*0.5f*parentÉch.x, (CôtéPos.get(indexDroite) + CôtéPos.get(indexGauche))*0.5f*parentÉch.y );
							p.éch = new Vecteur2f( (CôtéPos.get(indexBas) - CôtéPos.get(indexHaut))*0.5f*parentÉch.x, (CôtéPos.get(indexDroite) - CôtéPos.get(indexGauche))*0.5f*parentÉch.y );
						}
					}
				}
			}
		}else {
			//Si le curseur ne clique pas, réinitialiser les états.
			coinAttrapé = false;
			côtéAttrapé = false;
			coinAttrapéPanneau = -1;
			coinAttrapéPanneau2 = -1;
		}
		ajusterPanneaux(); //Mettre à jour les panneaux
	}
	
	//Ajuster la taille et la position des panneaux en fonction de leurs côtés.
	private void ajusterPanneaux() {
		
		CôtéPos.set(1,(parentPos.y + parentÉch.y - barreTaille)/parentÉch.y);
		
		for (int i = 0; i < panneaux.size(); i++) {
			//Pour tout les panneaux
			if(panneaux.get(i) != null) {
				//Si le panneau n'est pas détruit
				try {
					//Calculer la distance (-1;+1) entre les côtés du panneau et multiplier par l'échelle du parent pour la transformer en pixels
					float échX = ( CôtéPos.get( PanneauIndexeCôté.get(i)[0] ) - CôtéPos.get( PanneauIndexeCôté.get(i)[2] ) )*0.5f*parentÉch.x;
					float échY = ( CôtéPos.get( PanneauIndexeCôté.get(i)[1] ) - CôtéPos.get( PanneauIndexeCôté.get(i)[3] ) )*0.5f*parentÉch.y;
					//Calculer la moyenne (-1:+1) entre les côtés du panneau et multiplier par l'échelle du parent pour la transformer en pixels, puis ajouter la position du parent pour le recentrer.
					float posX = ( CôtéPos.get( PanneauIndexeCôté.get(i)[0] ) + CôtéPos.get( PanneauIndexeCôté.get(i)[2] ) )*0.5f*parentÉch.x + parentPos.x;
					float posY = ( CôtéPos.get( PanneauIndexeCôté.get(i)[1] ) + CôtéPos.get( PanneauIndexeCôté.get(i)[3] ) )*0.5f*parentÉch.y + parentPos.y;
					
					panneaux.get(i).pos = new Vecteur2f(posX,posY); //Assigner la position
					panneaux.get(i).éch = new Vecteur2f(échX,échY); //Assigner l'échelle
				} catch (Exception e) {
					e.printStackTrace(); //TODO Il reste des bugs d'indexes, des fois.
				}
			}
			
			float échX = parentÉch.x;
			float échY = barreTaille*0.5f;
			float posX = parentPos.x;
			float posY = parentÉch.y-(barreTaille*0.5f);
			
			barre.pos = new Vecteur2f(posX,posY);
			barre.éch = new Vecteur2f(échX,échY);
			
		}
	}
}
