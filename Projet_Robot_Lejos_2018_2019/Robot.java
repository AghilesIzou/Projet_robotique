package Algorithmmes;

import lejos.hardware.Button;
import lejos.utility.Delay;
import Utility.lcd;
import Utility.Menu;
import Moteurs.*;
import Capteur.ColorSensor;
import lejos.robotics.Color;
import Capteur.TouchSensor;
import Capteur.UltrasonicSensor;
import lejos.robotics.Color;

//class contenant tout les algorithmmes faisant appel au moteurs et aux capteurs en même temps

public class Robot implements Utility.Constantes{
	private MoteursDeplacement roues;
	private MoteurPince pince;
	private ColorSensor cs;
	private TouchSensor ts;
	private UltrasonicSensor uss;
	private boolean ferme=false,perdu=true,droite=true,gauche=false;
	private int palets=9,pRouge=3,pNoirEO=3,pNoirNS=3,pJaune=3,pVert=3,pBleu=3,arRouge=0,arJaune=0,arVert=0,arBleu=0,arNoirEO=0,arNoirNS=0,derniereCouleurLue=GRIS;//évite un problème de double lecture de blanc lorsque le capteur lis mal;
	private int couleur;
	private float range;
	private boolean speedMode;
	
	public Robot(MoteursDeplacement roues,MoteurPince pince,ColorSensor cs,TouchSensor ts,UltrasonicSensor uss) {
		//ouverture des objets à tester et de l'objet menu
				this.roues = roues;
				this.pince = pince;
				this.cs = cs;
				this.ts = ts;
				this.uss= uss;
	}
	public void close() {
		roues.close();
		pince.close();
		cs.close();
		ts.close();
		uss.close();
	}
	public void affichercouleur() {
		cs.setRGBMode();
		cs.setFloodLight(Color.WHITE);
		while (Button.ESCAPE.isUp()) {
			lcd.clear();
			cs.getRGB();
			lcd.print(LIGNE4,ColorSensor.nomCouleur(cs.idColorRGB()));
			Delay.msDelay(100 );
		}
	}
	public void suivreLIGNE1(int color) {
		do {
    		roues.avancer();
    		couleur = cs.idColorRGB();
    		//correction si le robot dévi sur du gris
    		correction(color);
    		//si capteur est touché pince se ferme et booléen passe à vrai
    		if (ts.isTouched()) {
    			roues.stop();
    			pince.fermer();
    			ferme=true;
    		}
		}while (couleur!=VERT&&couleur!=BLEU&&(!ferme)&&Button.ESCAPE.isUp());
		roues.stop();
	}
	public void ramenerPalet() {
		if (ferme) {
			do {
				roues.avancer();
				couleur = cs.idColorRGB();
			}while(couleur!=BLANC&&Button.ESCAPE.isUp());
			roues.stop();
			pince.ouvrir();
			roues.reculer(DELAI1000);
		}
	}
	public void suivreLIGNE2(int color1, int color2) {
		if (!ferme) {
			if ((color2==BLEU&&color1==ROUGE)||(color1==JAUNE&&color2==VERT)) {
				tournerDroite(color2);
			}
			else if ((color2==VERT&&color1==ROUGE)||(color1==JAUNE&&color2==BLEU)) {
				tournerGauche(color2);
			} else {
				tournerDroite(color2);
			}
			int allerretour=0;
			do {
				roues.avancer();
    			couleur = cs.idColorRGB();
    			range=uss.getRange();
    			correction(color2);
    			if (color1==NOIR&&range<MAX_RANGE&&allerretour==0) {
    				roues.stop();
    				roues.demiTourDroit();
    				range=uss.getRange();
    				allerretour++;
    			}
    			if (ts.isTouched()) {
    				roues.stop();
    				pince.fermer();
    				ferme=true;
    			}
			}while (range>MAX_RANGE&&!ferme&&Button.ESCAPE.isUp());
			roues.stop();
			if ((color2==BLEU&&color1==ROUGE)||(color1==JAUNE&&color2==VERT)) {
				roues.arcGauche();
			}
			else if ((color2==VERT&&color1==ROUGE)||(color1==JAUNE&&color2==BLEU)) {
				roues.arcDroit();
			} else {
				roues.arcDroit();
			}
		}
	}
	public void suivreLIGNE3(int color1, int color2) {
		if (!ferme) {
			do {
				roues.avancer();
    			couleur = cs.idColorRGB();
    			if (ts.isTouched()) {
    				roues.stop();
    				pince.fermer();
    				ferme=true;
    			}
			}while(couleur!=NOIR&&!ferme&&Button.ESCAPE.isUp());
			if (!ferme) {
				if ((color2==BLEU&&color1==ROUGE)||(color1==JAUNE&&color2==VERT)) {
					tournerGauche(NOIR);
				}
				else if ((color2==VERT&&color1==ROUGE)||(color1==JAUNE&&color2==BLEU)) {
					tournerDroite(NOIR);
				} else {
					tournerDroite(NOIR);
				}
				do {
					roues.avancer();
					couleur = cs.idColorRGB();
					range=uss.getRange();
					correction(NOIR);
					if (ts.isTouched()) {
						roues.stop();
						pince.fermer();
						ferme=true;
					}
				}while (range>MAX_RANGE&&!ferme&&Button.ESCAPE.isUp());
				roues.stop();
				if ((color2==BLEU&&color1==ROUGE)||(color1==JAUNE&&color2==VERT)) {
					roues.arcDroit();
				}
				else if ((color2==VERT&&color1==ROUGE)||(color1==JAUNE&&color2==BLEU)) {
					roues.arcGauche();
				} else {
					roues.arcGauche();
				}
			}
		}
	}
	public void suivreLIGNE4(int color1, int color2) {
		if (!ferme) {
			do {
				roues.avancer();
    			couleur = cs.idColorRGB();
    			if (ts.isTouched()) {
    				roues.stop();
    				pince.fermer();
    				ferme=true;
    			}
			}while(couleur!=VERT&&couleur!=BLEU&&!ferme&&Button.ESCAPE.isUp());
			if (!ferme) {
				int color3=couleur;
				if ((color2==BLEU&&color1==ROUGE)||(color1==JAUNE&&color2==VERT)) {
					tournerDroite(color3);
				}
				else if ((color2==VERT&&color1==ROUGE)||(color1==JAUNE&&color2==BLEU)) {
					tournerGauche(color3);
				} else {
					tournerGauche(color3);
				}
				do {
					roues.avancer();
					couleur = cs.idColorRGB();
					range=uss.getRange();
					correction(color3);
					if (ts.isTouched()) {
						roues.stop();
						pince.fermer();
						ferme=true;
					}
				}while (range>MAX_RANGE&&!ferme&&Button.ESCAPE.isUp());
				roues.stop();
				if ((color2==BLEU&&color1==ROUGE)||(color1==JAUNE&&color2==VERT)) {
					roues.arcGauche();
				}
				else if ((color2==VERT&&color1==ROUGE)||(color1==JAUNE&&color2==BLEU)) {
					roues.arcDroit();
				} else {
					roues.arcDroit();
				}
			}
		}
	}
	public void scenario1() {
		roues.resetTachoCount();//utilisé éventuellement pour detecter des problèmes liés à la rotation
		//initialisation des paramètres du capteur
		cs.setRGBMode();
		cs.setFloodLight(Color.WHITE);
		//vitesse et acceleration de base
		roues.setSpeed(VITESSE400);
		roues.setAcceleration(ACCELERATION2000);
		//booléen pour représenter l'état de la pince
        ferme=false;
        //stockage des valeurs des capteurs dans des variables
        couleur = cs.idColorRGB();
        range=uss.getRange();
        //avance jusqu'à trouver du rouge du jaune ou du noir supposément en partant du blanc aligné sur les LIGNEs de départ
        roues.avancer();
        while (couleur!=ROUGE&&couleur!=NOIR&&couleur!=JAUNE&&Button.ESCAPE.isUp()){
        	couleur = cs.idColorRGB();
        	lcd.clear(LIGNE6);
        	lcd.print(LIGNE6, ColorSensor.nomCouleur(couleur));
        }
        int couleur1=couleur;
        roues.stop();
        suivreLIGNE1(couleur1);
        int couleur2=couleur;
        lcd.clear(LIGNE6);
    	lcd.print(LIGNE6, ColorSensor.nomCouleur(couleur1)+" "+ColorSensor.nomCouleur(couleur2));
        suivreLIGNE2(couleur1,couleur2);
        suivreLIGNE3(couleur1,couleur2);
        suivreLIGNE4(couleur1,couleur2);
        ramenerPalet();
	    Button.waitForAnyPress();
	}
	public void scenario7(int goal) {//cherche un palet hors des lignes et le ramèner au camp adverse de son camp de départ.
		
		cs.setRGBMode();
		cs.setFloodLight(Color.WHITE);
		roues.setSpeed(VITESSE400);
		roues.setAcceleration(ACCELERATION2000);
		ferme=false;
		couleur=cs.idColorRGB();
		range = uss.getRange();
		//roues.avancer();
		
		chercherBlanc();
		chercherMur();
		chercherPalet();
		
		if(ferme)
			isFerme(goal);   // aller déposer le palet dans le camp opposé à notre camp de départ.
	
	}
	public void competition(int i,boolean speedMode) {/*avance jusqu'à rencontrer une LIGNE puis la parcours lorsqu'un palet est rencontré il est ramené au camps ouest car on a commencé de l'ouest
	si une LIGNE est parcourue une fois en entier elle ne sera plus parcourus car le nombre de palets trouvés sur cette LIGNE passe à 0 si 3 palets sont trouvés sur une LIGNE elle
	ne sera plus parcourus non plus si le nombre de palets n'est pas à 0 mais que tt les nombres de palets sur les LIGNEs sont à 0 on rentre dans une 
	méthode qui cherche à l'aide du capteur ultrason les palets, la méthode de compétition ouest est la même mais appel ramenerEst(int i) lorsqu'un palet est trouvé*/
		palets=9;pRouge=3;pNoirEO=3;pNoirNS=3;pJaune=3;pVert=3;pBleu=3;
		this.speedMode=speedMode;
		cs.setRGBMode();
		cs.setFloodLight(Color.WHITE);
		roues.setSpeed(VITESSE400);
		roues.setAcceleration(ACCELERATION2000);
		if (speedMode) {pVert=2;pBleu=2;
			roues.setSpeed(VITESSE600);
			roues.setAcceleration(ACCELERATION2000);
		}
		ferme=false;
		couleur=cs.idColorRGB();
		range=uss.getRange();
		roues.avancer();
		Delay.msDelay(DELAI400);//avance 400ms depuis sa position de départ pour passer la LIGNE blanche et lire la LIGNE de laquelle il est parti lorsqu'il rentre dans la première boucle
		do {
			roues.avancer();
			couleur=cs.idColorRGB();//avance en lisant la couleur et la distance tant qu'il n'a rien detecté
			range=uss.getRange();
			lcd.clear(LIGNE1);
			lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur)+"    "+range);
			if (ts.isTouched()) {
				if (i==EST) {
				ramenerOuest(NO_COULEUR);//si le capteur de toucher rencontre un palet il le ramène sans effectuer de manoeuvre particulière avant
				}
				if (i==OUEST) {
					ramenerEst(NO_COULEUR);//si ouest a été passé en argument on appel ramener Ouest
				}
			}
			if (derniereCouleurLue==BLANC&&couleur==BLANC) {
				couleur=GRIS;
			} else {
				derniereCouleurLue=couleur;
			}
			if (couleur==BLANC) {
				roues.stop();
				roues.reculer(DELAI1000);
				roues.stop();
				roues.tournerGauche();
				roues.avancer(DELAI250);
				couleur=cs.idColorRGB();
				derniereCouleurLue=BLANC;
			}
			if (range<MAX_RANGE) {
				roues.stop();
				roues.tournerGauche();
			}
			if (couleur==ROUGE) {
				roues.stop();
				chercherRouge();
				if (ferme) {
					if (i==EST) {
						ramenerOuest(couleur);
						}
					else if (i==OUEST) {
						ramenerEst(couleur);
					}
					pRouge--;
					if (pRouge==0) {
						pVert--;pNoirNS--;pBleu--;
					}
				}
			}
			if (couleur==JAUNE) {
				roues.stop();
				chercherJaune();
				if (ferme) {
					if (i==EST) {
						ramenerOuest(couleur);
						}
					else if (i==OUEST) {
						ramenerEst(couleur);
					}
					pJaune--;
					if (pJaune==0) {
						pVert--;pNoirNS--;pBleu--;
					}
				}
			}
			if (couleur==BLEU) {
				roues.stop();
				chercherBleu();
				if (ferme) {
					if (i==EST) {
						ramenerOuest(couleur);
						}
					else if (i==OUEST) {
							ramenerEst(couleur);
					}
					pBleu--;
					if (pBleu==0) {
						pJaune--;pNoirEO--;pRouge--;
					}
				}
			}
			if (couleur==VERT) {
				roues.stop();
				chercherVert();
				if (ferme) {
					if (i==EST) {
						ramenerOuest(couleur);
						}
					else if (i==OUEST) {
							ramenerEst(couleur);
					}
					pVert--;
					if (pVert==0) {
						pJaune--;pNoirEO--;pRouge--;
					}
				}
			}
			if (couleur==NOIR) {
				roues.stop();
				chercherNoir();
				if (ferme) {
					if (i==EST) {
						ramenerOuest(couleur);
						}
					else if (i==OUEST) {
							ramenerEst(couleur);
					}
					pNoirNS--;
				}
			}
			if (palets>0&&pVert<=0&&pRouge<=0&&pBleu<=0&&pJaune<=0&&pNoirEO<=0&&pNoirNS<=0) {
				chercherHorsLIGNEs();
				if (ferme) {
					ramenerOuest(NO_COULEUR);
				}
			}
			lcd.clear(LIGNE5);
			lcd.print(LIGNE5, "palets : "+palets);
		}while(palets>0&& Button.ESCAPE.isUp());
		roues.stop();
		Button.waitForAnyPress();
	}
	public void ramenerOuest(int color) {
		roues.stop();
		pince.fermer();
		if (color==ROUGE||color==JAUNE) {//sort de la LIGNE ou il a trouvé le palet en faisant 2 arc de cercles
				roues.arcDroit();
				roues.avancer(DELAI500);
				roues.arcGauche();
		}
		if (color==BLEU) {//se tourne à 90° de la LIGNE ou il a trouvé le palet
			if (arBleu>0) {
				roues.reculer(DELAI500);
				roues.tournerGauche();
			}else {
				roues.reculer(DELAI500);
				roues.tournerDroite();
			}
		}
		if (color==VERT) {//se tourne à 90° de la LIGNE ou il a trouvé le palet
			if (arVert>0) {
				roues.reculer(DELAI500);
				roues.tournerGauche();
			}else {
				roues.reculer(DELAI500);
				roues.tournerDroite();
			}
		}
		if (color==NOIR) {
			roues.reculer(DELAI500);
			roues.tournerDroite();
			while (couleur!=BLEU&&couleur!=VERT&&couleur!=JAUNE&&couleur!=ROUGE&&Button.ESCAPE.isUp()) {
				roues.avancer();
				couleur=cs.idColorRGB();
				range=uss.getRange();
				eviterMurs(RANGE_ESQUIVE);
			}
			roues.stop();
			if (couleur==JAUNE||couleur==ROUGE) {
				roues.reculer(DELAI500);
				roues.tournerGauche();
				pNoirEO--;
				
			}
			else {
				if (couleur==VERT) {
					roues.demiTourDroit();
				}
				roues.avancer(DELAI500);
				pNoirNS--;
				
				color=couleur;
			}
		}
		ferme=true;
		couleur=cs.idColorRGB();
		range=uss.getRange();
		do {
			roues.avancer();
			couleur=cs.idColorRGB();
			range=uss.getRange();
			eviterMurs(RANGE_ESQUIVE);
		}while(couleur!=BLANC&&couleur!=BLEU&&couleur!=VERT && Button.ESCAPE.isUp());
			if (couleur==BLANC&&color==BLEU) {
				roues.stop();
				pince.ouvrir();
				palets--;
				roues.reculer(DELAI1000);
				roues.demiTourDroit();
				ferme=false;
				couleur=cs.idColorRGB();
			}else if (couleur==BLANC && color!=BLEU) {
				roues.reculer(DELAI1000);
				roues.stop();
				roues.demiTourGauche();
				couleur=cs.idColorRGB();
				do {
					roues.avancer();
					couleur=cs.idColorRGB();
					lcd.clear(LIGNE1);
					lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur));range=uss.getRange();
					eviterMurs(RANGE_ESQUIVE);
					if (couleur==BLANC) {
						roues.reculer(DELAI1000);
						roues.stop();
						roues.demiTourGauche();
						couleur=cs.idColorRGB();
					}
				}while(couleur!=BLEU&&Button.ESCAPE.isUp());
				do {
					roues.avancer();
					couleur=cs.idColorRGB();
					lcd.clear(LIGNE1);
					lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur)+" boucle 2");
					range=uss.getRange();
					eviterMurs(RANGE_ESQUIVE);
					if (couleur==VERT) {
						roues.stop();
						roues.demiTourGauche();
					}
				}while(couleur!=BLANC&&Button.ESCAPE.isUp());
			}
			else if (couleur==VERT||couleur==BLEU) {
				if (couleur==VERT) {
				 	roues.stop();
				 	roues.demiTourDroit();
				 do {
						roues.avancer();
						couleur=cs.idColorRGB();
						lcd.clear(LIGNE1);
						lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur));range=uss.getRange();
						eviterMurs(RANGE_ESQUIVE);
						if (couleur==BLANC) {
							roues.reculer(DELAI1000);
							roues.stop();
							roues.demiTourGauche();
							couleur=cs.idColorRGB();
						}
					}while(couleur!=BLEU&&Button.ESCAPE.isUp());
				do {
					roues.avancer();
					couleur=cs.idColorRGB();
					lcd.clear(LIGNE1);
					lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur)+" boucle 2");
					range=uss.getRange();
					eviterMurs(RANGE_ESQUIVE);
					if (couleur==VERT) {
						roues.stop();
						roues.demiTourGauche();
					}
				}while(couleur!=BLANC&&Button.ESCAPE.isUp());
			}else if (couleur==BLEU) {
					do {
						roues.avancer();
						couleur=cs.idColorRGB();
						range=uss.getRange();
						lcd.clear(LIGNE1);
						lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur)+" boucle 2");
						eviterMurs(RANGE_ESQUIVE);
						if (couleur==VERT) {
							roues.stop();
							roues.demiTourGauche();
						}
					}while(couleur!=BLANC&&Button.ESCAPE.isUp());
				}
			}
			if (ferme) {
				roues.stop();
				pince.ouvrir();
				palets--;
				roues.reculer(DELAI1000);
				roues.demiTourGauche();
				ferme=false;
				couleur=cs.idColorRGB();
			}
	}
	public void ramenerEst(int color) {
		roues.stop();
		pince.fermer();
		if (color==ROUGE||color==JAUNE) {//sort de la LIGNE ou il a trouvé le palet en faisant 2 arc de cercles
				roues.arcDroit();
				roues.avancer(DELAI500);
				roues.arcGauche();
		}
		if (color==BLEU) {//se tourne à 90° de la LIGNE ou il a trouvé le palet
			if (arBleu>0) {
				roues.reculer(DELAI500);
				roues.tournerGauche();
			}else {
				roues.reculer(DELAI500);
				roues.tournerDroite();
			}
		}
		if (color==VERT) {//se tourne à 90° de la LIGNE ou il a trouvé le palet
			if (arVert>0) {
				roues.reculer(DELAI500);
				roues.tournerGauche();
			}else {
				roues.reculer(DELAI500);
				roues.tournerDroite();
			}
		}
		if (color==NOIR) {
			roues.reculer(DELAI500);
			roues.tournerDroite();
			while (couleur!=BLEU&&couleur!=VERT&&couleur!=JAUNE&&couleur!=ROUGE&&Button.ESCAPE.isUp()) {
				roues.avancer();
				couleur=cs.idColorRGB();
				range=uss.getRange();
				eviterMurs(RANGE_ESQUIVE);
			}
			roues.stop();
			if (couleur==JAUNE||couleur==ROUGE) {
				roues.reculer(DELAI500);
				roues.tournerGauche();
				roues.avancer(DELAI500);
				pNoirEO--;
				
			}
			else {
				if (couleur==BLEU) {
					roues.demiTourDroit();
				}
				roues.avancer(DELAI500);
				pNoirNS--;
				
				color=couleur;
			}
		}
		ferme=true;
		couleur=cs.idColorRGB();
		range=uss.getRange();
		
		do {roues.avancer();
			couleur=cs.idColorRGB();
			range=uss.getRange();
			eviterMurs(RANGE_ESQUIVE);
		}while(couleur!=BLANC&&couleur!=BLEU&&couleur!=VERT && Button.ESCAPE.isUp());
			if (couleur==BLANC&&color==VERT) {
				roues.stop();
				pince.ouvrir();
				palets--;
				roues.reculer(DELAI1000);
				roues.demiTourDroit();
				ferme=false;
				couleur=cs.idColorRGB();
			}else if (couleur==BLANC && color!=VERT) {
				roues.reculer(DELAI1000);
				roues.stop();
				roues.demiTourGauche();
				couleur=cs.idColorRGB();
				do {
					roues.avancer();
					couleur=cs.idColorRGB();
					lcd.clear(LIGNE1);
					lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur));
					range=uss.getRange();
					eviterMurs(RANGE_ESQUIVE);
					if (couleur==BLANC) {
						roues.reculer(DELAI1000);
						roues.stop();
						roues.demiTourDroit();
						couleur=cs.idColorRGB();
					}
				}while(couleur!=VERT&&Button.ESCAPE.isUp());
				roues.avancer(DELAI500);
				do {
					roues.avancer();
					couleur=cs.idColorRGB();
					lcd.clear(LIGNE1);
					lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur)+" boucle 2");
					range=uss.getRange();
					eviterMurs(RANGE_ESQUIVE);
					if (couleur==BLEU) {
						roues.stop();
						roues.demiTourGauche();
					}
				}while(couleur!=BLANC&&Button.ESCAPE.isUp());
			}
			if (couleur==VERT||couleur==BLEU) {
					if (couleur==BLEU) {
				 	roues.stop();
				 	roues.demiTourDroit();
				 	do {
						roues.avancer();
						couleur=cs.idColorRGB();
						lcd.clear(LIGNE1);
						lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur));
						range=uss.getRange();
						eviterMurs(RANGE_ESQUIVE);
						if (couleur==BLANC) {
							roues.reculer(DELAI1000);
							roues.stop();
							roues.demiTourDroit();
							couleur=cs.idColorRGB();
						}
					}while(couleur!=VERT&&Button.ESCAPE.isUp());
					roues.avancer(DELAI500);
					do {
						roues.avancer();
						couleur=cs.idColorRGB();
						lcd.clear(LIGNE1);
						lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur)+" boucle 2");
						range=uss.getRange();
						eviterMurs(RANGE_ESQUIVE);
						if (couleur==BLEU) {
							roues.stop();
							roues.demiTourGauche();
						}
					}while(couleur!=BLANC&&Button.ESCAPE.isUp());
			}else if (couleur==VERT) {
					do {
						roues.avancer();
						couleur=cs.idColorRGB();
							range=uss.getRange();
						lcd.clear(LIGNE1);
						lcd.print(LIGNE1, ColorSensor.nomCouleur(couleur)+" boucle 2");
						eviterMurs(RANGE_ESQUIVE);
						if (couleur==BLEU) {
							roues.stop();
							roues.demiTourGauche();
						}
					}while(couleur!=BLANC&&Button.ESCAPE.isUp());
				}
			}
			if (ferme) {
				roues.stop();
				pince.ouvrir();
				palets--;
				roues.reculer(DELAI1000);
				roues.demiTourGauche();
				ferme=false;
				couleur=cs.idColorRGB();
			}
	}
	public void chercherRouge(){
		/*toutes les fonctions chercher LIGNEs sont les mêmes à part la LIGNE noire qui est commenté à part les autres LIGNEs ne seront pas commentées*/
		if(pRouge>0&&arRouge<2) {/*vérification qu'il reste encore des palets sur la LIGNE et qu'elle n'a pas été parcourues en entier sinon la fonction ne fait rien*/
			roues.stop();
			arRouge=0;/*si la LIGNE avait été parcourus dans un sens mais pas dans l'autre on repart à 0 et on doit refaire un aller retour dessus sinon on pourrait
			avoir raté un palet*/
			Delay.msDelay(DELAI200);//laisse le temps au robot de s'arrêter avant la première lecture ce qui lui permettra de savoir si il est allé trop loin
			couleur=cs.idColorRGB();
			if (couleur==GRIS) {//si la première lecture après avoir trouvé la LIGNE est du gris il est allé trop loin la méthode tourner le remet sur la LIGNE
			tournerDroite(ROUGE);
			}
			perdu=false;
			do {
				roues.avancer();
				couleur=cs.idColorRGB();
				correction(ROUGE);
				if (derniereCouleurLue==BLANC&&couleur==BLANC) {
					couleur=ROUGE;
				} else {
					derniereCouleurLue=couleur;
				}
				if (couleur==BLANC) {
					roues.stop();
					roues.reculer(DELAI1000);
					roues.demiTourDroit();
					arRouge++;
					roues.avancer(DELAI250);
					couleur=cs.idColorRGB();
					derniereCouleurLue=BLANC;
				}
				if (ts.isTouched()) {
					ferme=true;
				}
				
			}while(arRouge<2&&!ferme&&!perdu&&Button.ESCAPE.isUp());//avance jusqu'à être touché ou jusuq'à avoir effectué un aller retour sur la LIGNE
			if (arRouge>=2) {
				pRouge=0;
			}
			perdu=true;
		}
	}
	public void chercherJaune() {
		if(pJaune>0&&arJaune<2) {
			int derniereCouleurLue=GRIS;//évite un problème de double lecture de blanc lorsque le capteur lis mal
			lcd.clear(LIGNE6);
			lcd.print(LIGNE6, "boucle jaune");
			roues.stop();
			arJaune=0;
			Delay.msDelay(DELAI200);
			couleur=cs.idColorRGB();
			if (couleur==GRIS) {
			tournerDroite(JAUNE);
			}
			if (derniereCouleurLue==BLANC&&couleur==BLANC) {
				couleur=JAUNE;
			} else {
				derniereCouleurLue=couleur;
			}
			perdu=false;
			do {
				roues.avancer();
				couleur=cs.idColorRGB();
				lcd.clear(LIGNE2);
				lcd.print(LIGNE2, ""+couleur);
				correction(JAUNE);
				if (couleur==BLANC) {
					roues.reculer(DELAI1000);
					roues.stop();
					roues.demiTourGauche();
					roues.avancer(DELAI250);
					couleur=cs.idColorRGB();
					arJaune++;
					derniereCouleurLue=BLANC;
				}
				if (ts.isTouched()) {
					ferme=true;
				}
			}while(arJaune<2&&!ferme&&!perdu&&Button.ESCAPE.isUp());
			if (arJaune>=2) {
				pJaune=0;
			}
			perdu=true;
		}
	}
	public void chercherBleu() {
		if(pBleu>0&&arBleu<2) {
			roues.stop();
			arBleu=0;if (pNoirEO==0) {
				pVert--;pBleu--;
			}
			perdu=false;
			Delay.msDelay(DELAI200);
			couleur=cs.idColorRGB();
			if (couleur==GRIS) {
			tournerDroite(BLEU);
			}
			do {
				roues.avancer();
				couleur=cs.idColorRGB();
				range=uss.getRange();
				correction(BLEU);
				if (range<MAX_RANGE) {
					arBleu++;
					if (arVert>=2) {
						roues.stop();
						Delay.msDelay(DELAI100);
						roues.arcGauche();
						roues.arcGauche();
						perdu=true;
					}else{
						roues.stop();
						roues.demiTourDroit();
					}
				}
				if (ts.isTouched()) {
					ferme=true;
				}
			}while(arBleu<2&&!ferme&&!perdu&&Button.ESCAPE.isUp());
			if (arBleu>=2) {
				pBleu=0;
			}
			perdu=true;
		}
	}
	public void chercherVert() {
		if(pVert>0&&arVert<2) {
			roues.stop();
			arVert=0;
			Delay.msDelay(DELAI200);
			couleur=cs.idColorRGB();
			if (couleur==GRIS) {
			tournerDroite(VERT);
			}
			perdu=false;
			do {
				roues.avancer();
				couleur=cs.idColorRGB();
				range=uss.getRange();
				correction(VERT);
				if (range<MAX_RANGE) {
					arVert++;
					if (arVert>=2) {
						roues.stop();
						Delay.msDelay(DELAI100);
						roues.arcGauche();
						roues.arcGauche();
						perdu=true;Delay.msDelay(DELAI100);
					}else{
						roues.stop();
						roues.demiTourDroit();
					}
				}
				if (ts.isTouched()) {
					ferme=true;
				}
			}while(arVert<2&&!ferme&&!perdu&&Button.ESCAPE.isUp());
			if (arVert>=2) {
				pVert=0;
			}
			perdu=true;
		}
	}
	public void chercherNoir() {
			if((pNoirEO>0||pNoirNS>0)&&(arNoirNS<2||arNoirEO<2)) {
				roues.stop();
				if(arNoirNS<2) {
					arNoirNS=0;
				}
				if(arNoirEO<2) {
					arNoirEO=0;
				}
				Delay.msDelay(DELAI200);
				couleur=cs.idColorRGB();
				if (couleur==GRIS) {
				tournerDroite(NOIR);
				}
				perdu=false;
				do {
					roues.avancer();
					couleur=cs.idColorRGB();
					range=uss.getRange();
					correction(NOIR);
					if (range<MAX_RANGE) {
						arNoirNS++;
						if (arNoirNS>=2||pNoirNS==0) {
							roues.stop();
							roues.arcGauche();
							roues.arcGauche();
							perdu=true;
						}else{
							roues.stop();
							roues.demiTourDroit();
						}
					}
					if (couleur==BLANC) {
						arNoirEO++;
						if (arNoirEO>=2||pNoirEO==0) {
							roues.stop();
							roues.tournerDroite();
							roues.avancer(DELAI1000);
							roues.tournerDroite();
							perdu=true;
						}else{
							roues.stop();
							roues.reculer(DELAI1000);
							roues.demiTourGauche();
							roues.avancer(DELAI250);
							couleur=cs.idColorRGB();
							if (couleur==BLANC) {
								couleur=GRIS;
							}
						}
					}
					if (ts.isTouched()) {/*il faut déterminer sur quel LIGNE le palet à été trouvé si on a déjà fait un aller sur une des LIGNEs c'est évident
					sinon il faut trouver une autre LIGNE avec le else pour savoir sur quel LIGNE on se trouvais c'est un cas particulier de la LIGNE noire*/
						roues.stop();
						ferme=true;
					}
				}while((arNoirNS<2||arNoirEO<2)&&!ferme&&!perdu&&Button.ESCAPE.isUp());
				if (arNoirNS>=2) {
					pNoirNS=0;
				}
				if (arNoirEO>=2) {
					pNoirEO=0;
				}
				perdu=true;
			}
	}
	public void chercherHorsLIGNEs() {
		Boolean inverse=false;
		lcd.print(3,"chercherHorsLIGNEs");
		while (!ferme&&Button.ESCAPE.isUp()) {
			int deplacement=1000;
			roues.correctionDroite2();
			do {
				range=uss.getRange();
				lcd.clear(LIGNE4);
				
				lcd.print(LIGNE4, "range : "+range);
			}while(range>1&&deplacement>0&&Button.ESCAPE.isUp());
			roues.stop();
			roues.setSpeed(VITESSE400);
			roues.setAcceleration(ACCELERATION2000);
			if (speedMode) {
				roues.setSpeed(VITESSE600);
				roues.setAcceleration(ACCELERATION2000);
			}
			if (range<=1) {
				lcd.clear(LIGNE7);
				lcd.print(LIGNE7, "objet trouve");
				roues.stop();
				while (couleur!=BLANC&&(range>RANGE_ESQUIVE)&&!ferme&&Button.ESCAPE.isUp()) {
					roues.avancer();
					couleur=cs.idColorRGB();
					range=uss.getRange();
					lcd.clear(LIGNE4);
					lcd.print(LIGNE4, "range : "+range);
					if (ts.isTouched()) {
						ferme=true;
					}
				}
			}
			if (couleur==BLANC) {
				roues.correctionDroite2();
				do {
					couleur=cs.idColorRGB();
				}while(couleur!=BLANC&&Button.ESCAPE.isUp()
						);
				roues.stop();
				roues.correctionDroite2();
				do {
					couleur=cs.idColorRGB();
				}while(couleur==BLANC&&Button.ESCAPE.isUp());
				roues.stop();
				roues.setSpeed(VITESSE400);
				roues.setAcceleration(ACCELERATION2000);
				if (speedMode) {
					roues.setSpeed(VITESSE600);
					roues.setAcceleration(ACCELERATION2000);
				}
			}
			if (range<RANGE_ESQUIVE) {
					roues.correctionDroite2();
					do {
						range=uss.getRange();
					}while(range<1&&Button.ESCAPE.isUp());
					roues.stop();
					roues.setSpeed(VITESSE400);
					roues.setAcceleration(ACCELERATION2000);
					if (speedMode) {
						roues.setSpeed(VITESSE600);
						roues.setAcceleration(ACCELERATION2000);
					}
				if (ts.isTouched()) {
					ferme=true;
				}
			}
			roues.stop();
			roues.tournerDroite();
			roues.avancer(DELAI5000);
			lcd.clear(LIGNE7);
			
		}
	}
	public void tournerDroite(int i) {
		lcd.clear(LIGNE2);
		lcd.print(LIGNE2, "tourner Droite");
		roues.stop();
		Delay.msDelay(DELAI500);
		couleur=cs.idColorRGB();
		roues.rotateB();
		while (couleur!=i&&Button.ESCAPE.isUp()){
			couleur=cs.idColorRGB();
			if (couleur==i) {
				roues.stop();
				Delay.msDelay(DELAI500);
				roues.setSpeed(VITESSE400);
				roues.setAcceleration(ACCELERATION2000);
				if (speedMode) {
					roues.setSpeed(VITESSE600);
					roues.setAcceleration(ACCELERATION2000);
				}
				gauche=true;
				
				droite=false;
			}
			if (couleur==BLANC) {
				if (i==ROUGE) {
					arRouge++;
				}
				if (i==NOIR) {
					arNoirEO++;
				}
				if (i==JAUNE) {
					arJaune++;
				}
				tournerGauche(i);
			}
			range=uss.getRange();
			if (range<RANGE_ESQUIVE) {
				if (i==BLEU) {
					arBleu++;
				}
				if (i==NOIR) {
					arNoirNS++;
				}
				if (i==VERT) {
					arVert++;
				}
				tournerGauche(i);
			}
		}
		lcd.clear(LIGNE2);
	}
	public void tournerGauche(int i) {
		lcd.clear(LIGNE2);
		lcd.print(LIGNE2, "tourner Gauche");
		roues.stop();
		Delay.msDelay(DELAI500);
		couleur=cs.idColorRGB();
		roues.rotateA();
		while (couleur!=i&&Button.ESCAPE.isUp()){
			couleur=cs.idColorRGB();
			if (couleur==i) {
				roues.stop();
				Delay.msDelay(DELAI500);
				roues.setSpeed(VITESSE400);
				roues.setAcceleration(ACCELERATION2000);
				if (speedMode) {
					roues.setSpeed(VITESSE600);
					roues.setAcceleration(ACCELERATION2000);
				}
				droite=true;
				gauche=false;
			}
		}
		roues.avancer(300);
		tournerDroite(i);
		lcd.clear(LIGNE2);
	}
	
	public void correction(int j) {
		if (couleur==GRIS) {
			perdu=false;
			int deplacement=10;
			while((couleur!=j&&couleur!=BLANC) && Button.ESCAPE.isUp()&&!perdu) {
				roues.stop();
				deplacement=2*deplacement;
				int i=deplacement;
				roues.correctionGauche2();
				while (deplacement>0 && (couleur!=j&&couleur!=BLANC)&&Button.ESCAPE.isUp()) {
					couleur=cs.idColorRGB();
					lcd.clear(LIGNE4);
					lcd.print(LIGNE4, ColorSensor.nomCouleur(couleur)+" "+deplacement);
					deplacement --;
					if(j==7||j==5) {
						if (couleur==BLEU||couleur==VERT||couleur==NOIR) {
							roues.avancer(DELAI200);
						}
					}
					if (j==2) {
						if (couleur==BLEU||couleur==VERT) {
							roues.avancer(DELAI200);
						}
					}
				}
				roues.stop();
				//Delay.msDelay(DELAI250);
				if (couleur==j) {
					droite=true;
					gauche=false;
				}else {
					deplacement=2*i;
					i=deplacement;
					if(deplacement<900) {
						roues.correctionDroite2();
						while (deplacement>0 && (couleur!=j&&couleur!=BLANC)&&Button.ESCAPE.isUp()) {
							couleur=cs.idColorRGB();
							lcd.clear(LIGNE4);
							lcd.print(LIGNE4, ColorSensor.nomCouleur(couleur)+" "+deplacement);
							deplacement --;
							if(j==7||j==5) {
								if (couleur==BLEU||couleur==VERT||couleur==NOIR) {
									roues.avancer(DELAI200);
								}
							}
							if (j==2) {
								if (couleur==BLEU||couleur==VERT) {
									roues.avancer(DELAI200);
								}
							}
						}
						roues.stop();
						//Delay.msDelay(DELAI250);
						if (couleur==j) {
							droite=false;
							gauche=true;
						}
					}
				}
				deplacement=i+1;
				lcd.print(3, ""+deplacement);
				if(deplacement>900) {
					perdu=true;
				}
			}
			roues.stop();
			Delay.msDelay(DELAI100);
			roues.setSpeed(VITESSE400);
			roues.setAcceleration(ACCELERATION2000);
			if (speedMode) {
				roues.setSpeed(VITESSE600);
				roues.setAcceleration(ACCELERATION2000);
			}
		}
	}
	public void eviterMurs(float distance) {//longe un mur rencontré
		if (range<distance) {
			roues.correctionDroite2();//lance une rotation vers la droite
			do {
				range=uss.getRange();//lis la distance tant que l'ont est 
				lcd.print(LIGNE8, ""+range);
			}while(range<distance&&Button.ESCAPE.isUp());
			roues.stop();
			roues.setSpeed(VITESSE400);
			roues.setAcceleration(ACCELERATION2000);
			if (speedMode) {
				roues.setSpeed(VITESSE600);
				roues.setAcceleration(ACCELERATION2000);
			}
		}
	}
public int retourOrientation(int coul) {
		
		lcd.clear(LIGNE6);
		lcd.print(LIGNE6, cs.nomCouleur(coul));
		
		
		
		
				
				roues.stop();
				Delay.msDelay(DELAI200);
				couleur=cs.idColorRGB();
				if (couleur==GRIS) {
				tournerDroite(coul);
				}
				
				do {
					roues.avancer();
					couleur=cs.idColorRGB();
					lcd.clear(LIGNE2);
					lcd.print(LIGNE2, ""+cs.nomCouleur(couleur));
					correction(coul);
					
					if (ts.isTouched()) {
						ferme=true;
					}
				}while(couleur!= BLEU && couleur != VERT&& couleur!=BLANC&&!ferme&&Button.ESCAPE.isUp());
				
				roues.stop();
				int couleur1 = couleur;
				if(couleur1 == BLANC) {
					
					roues.demiTourDroit();
				}
				roues.avancer(DELAI500);
				
				do {
					roues.avancer();
					couleur=cs.idColorRGB();
					lcd.clear(LIGNE2);
					lcd.print(LIGNE2, ""+cs.nomCouleur(couleur));
					correction(coul);
					
					if (ts.isTouched()) {
						ferme=true;
					}
				}while(couleur!= BLEU && couleur != VERT&& couleur!=BLANC&&!ferme&&Button.ESCAPE.isUp());
				
				roues.stop();
				int couleur2 = couleur;
				lcd.clear(LIGNE2);
				lcd.print(LIGNE2, ""+cs.nomCouleur(couleur1)+" "+cs.nomCouleur(couleur2));
				
				if(couleur1 == BLEU&& couleur2==BLANC) {
					
					roues.demiTourDroit();
					return EST;
					
				}
				
				if(couleur1==BLANC && couleur2==BLEU) {
					
					return EST;
				}
				
				if(couleur1==BLEU && couleur2 == VERT) {
					
					return EST;
				}
				
				if(couleur1==BLANC && couleur2==VERT) {
					return OUEST;
				}
				if(couleur1==VERT&&couleur2==BLEU) {
					return OUEST;
				}
				
				if(couleur1==VERT && couleur2==BLANC) {
					
					roues.demiTourDroit();
					return OUEST;
					
				}
				else {
					return 0;
				}
	}
	/*public void scenario4(int but) {
		cs.setRGBMode();
		cs.setFloodLight(Color.WHITE);
		roues.setSpeed(VITESSE400);
		roues.setAcceleration(ACCELERATION2000);
		ferme=false;
		couleur=cs.idColorRGB();
		range=uss.getRange();
		do {
			roues.avancer();
			couleur=cs.idColorRGB();
			range=uss.getRange();
			eviterMurs(MAX_RANGE);
			if(couleur==1) {
				roues.stop();
				roues.tournerDroite();
			}
			if (ts.isTouched()) {
				ferme=true;
			}
		}while(couleur!=JAUNE&&couleur!=ROUGE&&couleur!=NOIR&& Button.ESCAPE.isUp()&&!ferme);
		roues.stop();
		if (ferme) {
			switch (but) {
			case EST:
				ramenerOuest(NO_COULEUR);
				break;
			case OUEST:
				ramenerEst(NO_COULEUR);
				break;
			}
		} else {
		int	orientation=retourOrientation(couleur);
		lcd.print(LIGNE5,((orientation==EST)?"Est":(orientation==OUEST)?"Ouest":"inconnue"));
		}
		Button.waitForAnyPress();
	}*/
	// avance jusqu'à la ligne blanche,évite aussi le mur  pendant sont déplacement. 
	public void chercherBlanc() {
			
			do {
				
				roues.avancer();
				couleur= cs.idColorRGB();
				range=uss.getRange();
				eviterMurs(MAX_RANGE);
				
				if(couleur == BLANC){
					roues.stop();
				}
				
				if(ts.isTouched()) {
					ferme=true;
				}
				
				
			}while(couleur!=BLANC&& Button.ESCAPE.isUp()&& !ferme);
			
			
		}
		
		
		// avancer sur la ligne blanche jusqu'à ce que la distance qui le spéare du mur devienne inférieur à 8 cm.
		public void chercherMur() {
			
			if(!ferme) {
				roues.tournerDroite();
				perdu=false;
				do {
					roues.avancer();
					couleur = cs.idColorRGB();
					correction(BLANC);
					range = uss.getRange();
					if(ts.isTouched()) {
						ferme=true;
					}
					
				}while(range>RANGE_PETIT && Button.ESCAPE.isUp()&&!ferme);
				roues.stop();
				roues.tournerDroite();
			}
			
			
		}
		
		// ramèner le palet au camp indiqué 
		
		public void isFerme(int goal) {
			
			if(goal==OUEST)
				ramenerOuest(NO_COULEUR);
			
			if(goal==EST)
				ramenerEst(NO_COULEUR);
		}
		
		
		// chercher un palet déposé n'import ou sur la table , que cela soit aux intersections des lignes de couleurs ou en dehors.
		
		public void chercherPalet() {
			
			if(!ferme) {
				
				do {
				
					chercherBlanc();
					
					if(!ferme) {
					roues.tournerDroite();
					roues.avancer(DELAI500);
					roues.tournerDroite();
					}
					
					chercherBlanc();
					
					if(!ferme) {
					roues.tournerGauche();
					roues.avancer(DELAI500);
					roues.tournerGauche();
					
					}
				
				}while(!ferme);
			}
		}
		public void scenario4(int orient,int camp,int color) {
		//color=cs.idColorRGB();
		//retourOrientation(color);
		/*cs.setRGBMode();
		cs.setFloodLight(Color.WHITE);*/
		couleur=cs.idColorRGB();
		range=uss.getRange();
		if(camp==EST && orient==EST) {
			roues.demiTourDroit();	
			do {
					roues.avancer();
					couleur=cs.idColorRGB();
					do{
						color=cs.idColorRGB();
						correction(color);
					}while(color!=NOIR || color!=JAUNE || color!=ROUGE);
					if(ts.isTouched()) {
					    color=cs.idColorRGB();
						ramenerOuest(color);
					}
				}while(couleur!=BLANC &&Button.ESCAPE.isUp());
				roues.demiTourDroit();
				couleur=cs.idColorRGB();
					scenario1();	
		}
	else if(camp==EST && orient==OUEST){
		//roues.demiTourDroit();
		//color=cs.idColorRGB();
		do {
			roues.avancer();
			//color=cs.idColorRGB();
			do {
				color=cs.idColorRGB();
				correction(color);
			}while(color!=JAUNE || color!=NOIR || color!=ROUGE);
			if(ts.isTouched()) {
			    color=cs.idColorRGB();
				ramenerOuest(color);
			}
			
		}while(couleur!=BLANC &&Button.ESCAPE.isUp());
		roues.stop();
		roues.demiTourGauche();
		couleur=cs.idColorRGB();
			scenario1();	
	}	
	if(camp==OUEST && orient==OUEST) {
		roues.demiTourDroit();
		do{
			color=cs.idColorRGB();
			correction(color);
		}while(color!=JAUNE || color !=NOIR || color!=ROUGE);
		do {
			roues.avancer();
			couleur=cs.idColorRGB();
			do{
				color=cs.idColorRGB();
				correction(color);
			}while(color!=JAUNE ||color!=NOIR || color!=ROUGE);
			if(ts.isTouched()) {
			    color=cs.idColorRGB();
				ramenerEst(color);
			}
		}while(couleur!=BLANC &&Button.ESCAPE.isUp());
		roues.stop();
		roues.demiTourDroit();
		couleur=cs.idColorRGB();
			scenario1();
	}
	else if(camp==OUEST && orient==EST) {
		do {
			roues.avancer();
			couleur=cs.idColorRGB();
			do{
				color=cs.idColorRGB();
				correction(color);
			}while(color!=NOIR || color!=ROUGE || color!=JAUNE);
			if(ts.isTouched()) {
			    color=cs.idColorRGB();
				ramenerEst(color);
			}
		}while(couleur!=BLANC &&Button.ESCAPE.isUp());
		roues.stop();
		roues.demiTourDroit();
		couleur=cs.idColorRGB();
			scenario1();
	}
  }
  public void scenario3(int orienta,int colore ) {	
	  couleur=cs.idColorRGB();
	  range=uss.getRange();
	  if( orienta==EST) {
			//roues.demiTourDroit();	
			do {
					roues.avancer();
					couleur=cs.idColorRGB();
					do{
						colore=cs.idColorRGB();
						correction(colore);
					}while(colore!=NOIR || colore!=JAUNE || colore!=ROUGE);
					if(ts.isTouched()) {
					    colore=cs.idColorRGB();
						ramenerEst(colore);
					}
				}while(couleur!=BLANC &&Button.ESCAPE.isUp());
				roues.demiTourDroit();
				couleur=cs.idColorRGB();
					scenario1();	
		}  
	  
  if( orienta==OUEST) {
		roues.demiTourDroit();
		do{
			colore=cs.idColorRGB();
			correction(colore);
		}while(colore!=JAUNE || colore !=NOIR || colore!=ROUGE);
		do {
			roues.avancer();
			couleur=cs.idColorRGB();
			do{
				colore=cs.idColorRGB();
				correction(colore);
			}while(colore!=JAUNE ||colore!=NOIR || colore!=ROUGE);
			if(ts.isTouched()) {
			    colore=cs.idColorRGB();
				ramenerOuest(colore);
			}
		}while(couleur!=BLANC &&Button.ESCAPE.isUp());
		roues.stop();
		roues.demiTourDroit();
		couleur=cs.idColorRGB();
			scenario1();
	}
}
}
