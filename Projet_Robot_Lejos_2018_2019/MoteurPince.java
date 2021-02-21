package Moteurs;
import Utility.lcd;
//importations nécessaires
import lejos.hardware.motor.*;
import lejos.hardware.port.*;

//class contenant des méthodes pour activer le moteur de la pince

public class MoteurPince implements Utility.Constantes {
	//attributs: le moteur moyen qui controle la pince
	private static EV3MediumRegulatedMotor motorC;
	
	//constructeur alloue la mémoire
	public MoteurPince() {
		motorC = new EV3MediumRegulatedMotor(MotorPort.C);
		motorC.setSpeed(1440);
		motorC.setAcceleration(3000);
	}
	//controle simples ouvrir fermer suffisent à saisir un palet pas besoin de controller avec précision
	//l'angle d'ouverture ou de fermeture depuis le main
	public void ouvrir() {
		motorC.rotate(ANGLEPINCE);//fait faire un tour complet à la vis dans le sens des aiguilles d'une montre ce 
	}					   //qui ouvre les pinces
	public void fermer() {
		motorC.rotate(-ANGLEPINCE);
	}
	//libère l'espace alloué
	public void close() {
		motorC.close();
	}
	public static void getMaxSpeed() {
		lcd.print(8, "moteur C:"+motorC.getMaxSpeed());
		
	}
}