package Main;
//importations nécessaires
import lejos.hardware.Button;
import lejos.utility.Delay;
import Utility.lcd;
import Utility.Menu;
import Moteurs.*;
import Algorithmmes.Robot;
import Capteur.ColorSensor;
import lejos.robotics.Color;
import Capteur.TouchSensor;
import Capteur.UltrasonicSensor;
import lejos.robotics.Color;

public class L2A3 implements Utility.Constantes {
	public static void main (String[]args) {
		//ouverture des objets à tester et de l'objet menu
		MoteursDeplacement roues = new MoteursDeplacement();
		MoteurPince pince = new MoteurPince();
		
		ColorSensor cs = new ColorSensor();
		TouchSensor ts = new TouchSensor();
		UltrasonicSensor uss= new UltrasonicSensor();
		Robot robot= new Robot(roues,pince,cs,ts,uss);
		
		//affichage du premier menu escape termine le programme
		//sinon on choisit le mode
		int choix1=VALIDER,choix2,choix3,choix4,choix5;//ces valeurs servent uniquement à être passé à 0 si les menus retournent escape pour sortir des boucles sinon elles restent sur VALIDER
		do {
			switch(Menu.menu1()) {
			case 1:
				//mode competition
				choix4=VALIDER;
				do {
					switch (Menu.menu4(DEPART)) {//choix de la position de départ
					case EST://est
						choix5=VALIDER;
						do {
							switch (Menu.menu5()) {
							case CHOIX1:
								choix3=VALIDER;
								do  {
									choix3=Menu.menu3();
									if (choix3==VALIDER) {
										robot.competition(EST,SPEED_MODE);
									}
								}while (choix3!=QUITTER);
								break;
							case CHOIX2:
								choix3=VALIDER;
								do  {
									choix3=Menu.menu3();
									if (choix3==VALIDER) {
										robot.competition(EST,SLOW_MODE);
									}
								}while (choix3!=QUITTER);
								break;
							case QUITTER:
								choix5=QUITTER;
							}
						}while(choix5!=QUITTER);
						break;
					case OUEST://ouest
						choix5=VALIDER;
						do {
							switch (Menu.menu5()) {
							case CHOIX1:
								choix3=VALIDER;
								do  {
									choix3=Menu.menu3();
									if (choix3==VALIDER) {
										robot.competition(OUEST,SPEED_MODE);
									}
								}while (choix3!=QUITTER);
								break;
							case CHOIX2:
								choix3=VALIDER;
								do  {
									choix3=Menu.menu3();
									if (choix3==VALIDER) {
										robot.competition(OUEST,SLOW_MODE);
									}
								}while (choix3!=QUITTER);
								break;
							case QUITTER:
								choix5=QUITTER;
							}
						}while(choix5!=QUITTER);
						break;
					case 0:
						choix4=QUITTER;
						break;
					}
				}while(choix4!=QUITTER);
				break;
			case 2:
				choix2=VALIDER;
				//choix 1 effectué sur le premier menu affichage du deuxième autant de case que de réponse possible chaque case affiche le menu
				//de lancement presser retour revient au menu précédent jusqu'au premier
				do {
					switch(Menu.menu2()) {
					case 1:
						choix3=VALIDER;
						do  {
							choix3=Menu.menu3();
							if (choix3==VALIDER) {
								robot.scenario1();
								}
							}while(choix3!=QUITTER);
						break;
					case 2:
						choix4=VALIDER;
						do {
							switch (Menu.menu4(GOAL)) {//choix du but
							case EST://est
								choix3=VALIDER;
								do  {
									choix3=Menu.menu3();
									if (choix3==VALIDER) {
										robot.scenario4(EST);
									}
								}while (choix3!=QUITTER);
								break;
							case OUEST://ouest
								choix3=VALIDER;
								do  {
									choix3=Menu.menu3();
									if (choix3==VALIDER) {
										robot.scenario4(OUEST);
									}
								}while (choix3!=QUITTER);
								break;
							case 0:
								choix4=QUITTER;
								break;
							}
						}while(choix4!=QUITTER);
						break;
					case 3:
						choix4=VALIDER;
						do {
							switch (Menu.menu4(GOAL)) {//choix de la position de départ
							case EST://est
								choix3=VALIDER;
								do  {
									choix3=Menu.menu3();
									if (choix3==VALIDER) {
										robot.scenario7(OUEST);
									}
								}while (choix3!=QUITTER);
								break;
							case OUEST://ouest
								choix3=VALIDER;
								do  {
									choix3=Menu.menu3();
									if (choix3==VALIDER) {
										robot.scenario7(EST);
									}
								}while (choix3!=QUITTER);
								break;
							case 0:
								choix4=QUITTER;
								break;
							}
						}while(choix4!=QUITTER);
						break;
					case 0:
						choix2=QUITTER;
						break;
					}
				}while (choix2!=QUITTER);
				break;
			case 3:
				robot.affichercouleur();
				break;
			case 0:
				choix1=QUITTER;
				break;
			}
		}while (choix1!=QUITTER);
		robot.close();
	}
}
