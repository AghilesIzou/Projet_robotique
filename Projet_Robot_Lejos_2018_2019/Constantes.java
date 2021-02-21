package Utility;

public interface Constantes {
	 int ROTATION200 = 200, ROTATION400=398;//valeur utilisé pour les demi tours et quarts de tours
	 int DELAI500 = 500, DELAI200=200, DELAI250=250, DELAI1000=1000,DELAI2000=2000, DELAI300=300, DELAI800=800, DELAI400=400, DELAI5000=5000,DELAI100=100;//utilisé pour des délais de 500ms ou DELAI250ms
	 int VITESSE_CORRECTION = 120;//utilisé pour ralentir le robot dans les méthodes de corrections
	 int ARC_ROTATION1=94,  ARC_ROTATION2=482;//degrés de rotation particuliers pour un mouvement précis d'arc de cercle
	 int ANGLEPINCE=1020;//angle auquel on veut faire tourner la pince
	 int VITESSE400 = 400, ACCELERATION2000=2000,VITESSE600=600;//vitesse et accelération de base
	 int BLANC=1,NOIR=2, BLEU=3, VERT=4, ROUGE=5, MARRON=6, JAUNE=7, GRIS=8,NO_COULEUR=0;//références des couleurs
	 float MAX_RANGE=Float.parseFloat("0.19"), RANGE_ESQUIVE=Float.parseFloat("0.25"), RANGE_PETIT=Float.parseFloat("0.07");/*range maximum au delà de laquelle le capteur ultrason voit les palets et range convenable pour
																										esquiver un palet lorsque qu'on ne souhaite pas rentrer dedans*/
	 int LIGNE1=1, LIGNE2=2,LIGNE3=3, LIGNE4=4, LIGNE5=5,LIGNE6=6, LIGNE7=7,LIGNE8=8;
	 int COLONNE1=1, COLONNE2=2, COLONNE3=3, COLONNE4=4, COLONNE5=5,COLONNE6=6, COLONNE7=7,COLONNE8=8;/*constantes pour l'affichage sur l'écran*/
	 int EST=1, OUEST=2;//passé en argument pour certaines fonctions qui changent en fonction du camp adverse 
	 int QUITTER=0 ,VALIDER=1,CHOIX1=1,CHOIX2=2,CHOIX3=3;//constantes pour les choix des menus
	 boolean SPEED_MODE=true,SLOW_MODE=false;
	 int DEPART=0,GOAL=1;
}
