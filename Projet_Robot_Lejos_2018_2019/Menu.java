package Utility;
//importations nécessaires
import lejos.hardware.Button;
import Moteurs.MoteurPince;
import Moteurs.MoteursDeplacement;
import Utility.lcd;
import lejos.hardware.Battery;

//affiche des menus et renvoi le résultat du choix

public class Menu implements Utility.Constantes{
	//choix 1 et choix 2 pour retenir la position de l'utilisateur sur le menu les autres menus renvoient 
	//chaque pression de bouton donc pas besoin
	//button sert à retenir le dernier boutton préssé on peut faire sans avec Button."   ".istouched() je crois
	private static int choix1,choix2,choix4,choix5,button;
	//constructeur n'initialise rien les valeurs ont besoin d'être initialisées au début de chaque méthode de toute façon
	public Menu() {
	}
	//meme principe pour tout les menus escape et OK étants les boutons de sortie tant qu'il ne sont pas pressés affichage du menu et incrémentation du
	//choix si bouton down pressé décrémentation si bouton up pressé si buton escape choix = 0 retour du choix si OK pressé retour du choix sur sa position
	//à chaque pression si choix n'est pas au maximum ou au minimum le menu se réaffiche en affichant en lettres capital le texte correspondant à
	//la position du choix actuel
	public static int menu1() {
		choix1=1;
		button=0;
		lcd.clear();
		lcd.print(LIGNE2, COLONNE1, "->	COMPETITION");
		lcd.print(LIGNE3, COLONNE2, "scenarios");
		lcd.print(LIGNE4, COLONNE2, "test couleurs");
		lcd.print(LIGNE6, COLONNE2, "battery : "+(((Battery.getVoltage()-6.5)*100)/1.5)+"%");
		
		do {
			button=Button.waitForAnyPress();
			if (button==Button.ID_ESCAPE) {
				choix1=0;
				lcd.clear(5);
			}
			if (button==Button.ID_UP) {
				choix1--;
				if (choix1<1) {
					choix1=3;
				}
			}
			if (button==Button.ID_DOWN) {
				choix1++;
				if (choix1>3) {
					choix1=1;
				}
			}
				switch(choix1) {
				case 1:
					lcd.clear();
					lcd.print(LIGNE2, COLONNE1, "->	COMPETITION");
					lcd.print(LIGNE3, COLONNE2, "scenarios");
					lcd.print(LIGNE4, COLONNE2, "test couleurs");
					lcd.print(LIGNE6, COLONNE2, "battery : "+(((Battery.getVoltage()-6.5)*100)/1.5)+"%");
				break;
				case 2:
					lcd.clear();
					lcd.print(LIGNE2, COLONNE2, "competition");
					lcd.print(LIGNE3, COLONNE1, "->	SCENARIOS");
					lcd.print(LIGNE4, COLONNE2, "test couleurs");
					lcd.print(LIGNE6, COLONNE2, "battery : "+(((Battery.getVoltage()-6.5)*100)/1.5)+"%");
					break;
				case 3:
					lcd.clear();
					lcd.print(LIGNE2, COLONNE2, "competition");
					lcd.print(LIGNE3, COLONNE2, "scenarios");
					lcd.print(LIGNE4, COLONNE1, "-> TEST COULEURS");
					lcd.print(LIGNE6, COLONNE2, "battery : "+(((Battery.getVoltage()-6.5)*100)/1.5)+"%");
				break;
				}
		}while (button!=Button.ID_ENTER&&choix1!=0);
		return choix1;
	}
	
	public static int menu2() {
		choix2=1;
		button=0;
		lcd.clear();
		lcd.print(LIGNE1, COLONNE1, "->	SCENARIO 1");
		lcd.print(LIGNE2, COLONNE2, "scenario 2");
		lcd.print(LIGNE3, COLONNE2, "scenario 3");
		do {
			button=Button.waitForAnyPress();
			if (button==Button.ID_ESCAPE) {
				choix2=0;
			}
			if (button==Button.ID_UP) {
				choix2--;
				if (choix2<1) {
					choix2=3;
				}
			}
			if (button==Button.ID_DOWN) {
				choix2++;
				if (choix2>3) {
					choix2 = 1;
				}
			}
				switch (choix2) {
				case 1:
					lcd.clear();
					lcd.print(LIGNE1, COLONNE1, "->	SCENARIO 1");
					lcd.print(LIGNE2, COLONNE2, "scenario 2");
					lcd.print(LIGNE3, COLONNE2, "scenario 3");
					
					break;
				case 2:
					lcd.clear();
					lcd.print(LIGNE1, COLONNE2, "scenario 1");
					lcd.print(LIGNE2, COLONNE1, "-> SCENARIO 2");
					lcd.print(LIGNE3, COLONNE2, "scenario 3");
					
					break;
				case 3:
					lcd.clear();
					lcd.print(LIGNE1, COLONNE2, "scenario 1");
					lcd.print(LIGNE2, COLONNE2, "scenario 2");
					lcd.print(LIGNE3, COLONNE1, "-> SCENARIO 3");
					
					break;
				}
		}while (button!=Button.ID_ENTER&&choix2!=0);
		
		return choix2;
	}
	//simple choix lancer le programme ou retourner à la sélection des programmes
	public static int menu3() {
		button=0;
		lcd.clear();
		lcd.print(2, "ready:");
		lcd.print(3, "press enter or");
		lcd.print(4, "escape to return");
		do {
		button=Button.waitForAnyPress();
		}while (button!=Button.ID_ENTER&&button!=Button.ID_ESCAPE);
		if (button==Button.ID_ENTER) {lcd.clear();return 1;}
		else {lcd.clear();return 0;}
	}
	
	public static int menu4(int i) {
		choix4=1;
		button=0;
		do {
			switch(choix4) {
			case 1:
				lcd.clear();
				lcd.print(LIGNE1, COLONNE1, ((i==0)?"DEPART:":"GOAL:"));
				lcd.print(LIGNE3, COLONNE1, "-> EST");
				lcd.print(LIGNE4, COLONNE2, "ouest");
				break;
			case 2:
				lcd.clear();
				lcd.print(LIGNE1, COLONNE1, ((i==0)?"DEPART:":"GOAL:"));
				lcd.print(LIGNE3, COLONNE2, "est");
				lcd.print(LIGNE4, COLONNE1, "-> OUEST");
				break;
			}
			button=Button.waitForAnyPress();
			if (button==Button.ID_UP) {
				choix4--;
				if (choix4<1) {
					choix4=2;
				}
			}
			if (button==Button.ID_DOWN) {
				choix4++;
				if (choix4>2) {
					choix4=1;
				}
			}
			if (button==Button.ID_ESCAPE) {
				choix4=0;
			}
		}while (button!=Button.ID_ENTER&& button!=Button.ID_ESCAPE);
		return choix4;
	}
	public static int menu5() {
		choix5=1;
		button=0;
		do {
			switch(choix5) {
			case 1:
				lcd.clear();
				lcd.print(LIGNE1, COLONNE1, "SPEED MODE");
				lcd.print(LIGNE3, COLONNE1, "-> YES");
				lcd.print(LIGNE4, COLONNE2, "no");
				break;
			case 2:
				lcd.clear();
				lcd.print(LIGNE1, COLONNE1, "DEPART:");
				lcd.print(LIGNE3, COLONNE2, "yes");
				lcd.print(LIGNE4, COLONNE1, "-> NO");
				break;
			}
			button=Button.waitForAnyPress();
			if (button==Button.ID_UP) {
				choix5--;
				if (choix5<1) {
					choix5=2;
				}
			}
			if (button==Button.ID_DOWN) {
				choix5++;
				if (choix5>2) {
					choix5=1;
				}
			}
			if (button==Button.ID_ESCAPE) {
				choix5=0;
			}
		}while (button!=Button.ID_ENTER&& button!=Button.ID_ESCAPE);
		return choix5;
	}
	
}