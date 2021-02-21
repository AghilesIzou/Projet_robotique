package Moteurs;
import Utility.lcd;
import lejos.robotics.navigation.*;
import lejos.robotics.chassis.*;
//importations nécessaires
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.utility.Delay;

//class contenant des fonctions pour activer les moteurs des roues

public class MoteursDeplacement implements Utility.Constantes {
	//attributs 2 moteurs du robot qui commandent les roues
	private static EV3LargeRegulatedMotor motorA;
	private static EV3LargeRegulatedMotor motorB;
	
	//constructeur alloue l'espace des deux moteurs
	public MoteursDeplacement () {
		motorA = new EV3LargeRegulatedMotor(MotorPort.A);
		motorB = new EV3LargeRegulatedMotor(MotorPort.B);
		motorA.synchronizeWith(new EV3LargeRegulatedMotor[] {motorB});
	}
	
	public void avancer() {
		motorA.startSynchronization();
		motorA.forward();//forward met le moteur vers l'avant
		motorB.forward();
		motorA.endSynchronization();
	}
	// deuxième version qui prend une duré en argument peut être utile pour alléger le code
	public void avancer(long delai) {
		motorA.startSynchronization();
		motorA.forward();
		motorB.forward();
		motorA.endSynchronization();
		Delay.msDelay(delai);
		motorA.startSynchronization();
		motorA.stop();
		motorB.stop();
		motorA.endSynchronization();
	}
	//stop arrêt les moteurs
	public void stop() {
		motorA.startSynchronization();
		motorA.stop();
		motorB.stop();
		motorA.endSynchronization();
	}
	//close libère l'espace alloué
	public void close() {
		motorA.close(); 
		motorB.close();
	}
	//fait un angle de 90° à gauche avec ROTATION des roues à 200° ne peut rien faire pendant la ROTATION
	//les DELAI servent à laisser le temps au moteur de changer d'instruction sinon une suite de commande trop rapide 
	//engendre des erreurs
	public void tournerGauche(){
		Delay.msDelay(DELAI500);
		motorA.startSynchronization();
		motorA.rotate(ROTATION200);
		motorB.rotate(-ROTATION200);
        motorA.endSynchronization();
        motorA.waitComplete();
        motorB.waitComplete();
        Delay.msDelay(DELAI200);
	}
	//même chose que tournergauche à droite
	public void tournerDroite(){
		Delay.msDelay(DELAI500);
		motorA.startSynchronization();
		motorA.rotate(-ROTATION200);
		motorB.rotate(ROTATION200);
        motorA.endSynchronization();
        motorA.waitComplete();
        motorB.waitComplete();
        Delay.msDelay(DELAI200);
        }
	//même chose que tournergauche à droite mais tourne à 180°
	public void demiTourDroit() {
		Delay.msDelay(DELAI500);
		motorA.startSynchronization();
		motorA.rotate(-ROTATION400);
		motorB.rotate(ROTATION400);
        motorA.endSynchronization();
        motorA.waitComplete();
        motorB.waitComplete();
        Delay.msDelay(DELAI200);
        }
	//même chose que tournergauche mais tourne à 180°
	public void demiTourGauche() {
		Delay.msDelay(DELAI500);
		motorA.startSynchronization();
		motorA.rotate(ROTATION400);
		motorB.rotate(-ROTATION400);
        motorA.endSynchronization();
        motorA.waitComplete();
        motorB.waitComplete();
        Delay.msDelay(DELAI200);
        }
	//recule jusqu'à être interrompu peut exécuter d'autres instructions pendant ce temps
	public void reculer() {
		motorA.startSynchronization();
		motorA.backward();
		motorB.backward();
		motorA.endSynchronization();
	}
	//recule pendant le temps DELAI en ms ne peut rien faire d'autre pendant ce temps
	public void reculer(long delai) {
		motorA.startSynchronization();
		motorA.backward();
		motorB.backward();
		motorA.endSynchronization();
		Delay.msDelay(delai);
		motorA.startSynchronization();
		motorA.stop();
		motorB.stop();
		motorA.endSynchronization();
	}
	public static void getMaxSpeed() {
		lcd.print(6, "moteur A:"+motorA.getMaxSpeed());
		lcd.print(7, "moteur B:"+motorB.getMaxSpeed());
	}
	public void setSpeed(int puissance) {
		motorA.startSynchronization();
		motorA.setSpeed(puissance+2);
		motorB.setSpeed(puissance);
		motorA.endSynchronization();
	}
	public void setAcceleration(int acceleration) {
		motorA.startSynchronization();
		motorA.setAcceleration(acceleration);
		motorB.setAcceleration(acceleration);
		motorA.endSynchronization();
	}
	public void correctionDroite() {
		motorA.startSynchronization();
		motorA.rotate(-10);
		motorB.rotate(10);
        motorA.endSynchronization();
        motorA.waitComplete();
        motorB.waitComplete();
      
	}
	public void correctionGauche() {
		motorA.startSynchronization();
		motorA.rotate(10);
		motorB.rotate(-10);
        motorA.endSynchronization();
        motorA.waitComplete();
        motorB.waitComplete();	
		
	}
	public static void getRotationSpeed() {
		lcd.print(1, "moteur A:"+motorA.getRotationSpeed());
		lcd.print(2, "moteur B:"+motorB.getRotationSpeed());
	}
	public static void getTachoCount() {
		lcd.print(3, "moteur A:"+motorA.getTachoCount());
		lcd.print(4, "moteur B:"+motorB.getTachoCount());
	}
	public void resetTachoCount() {
		motorA.resetTachoCount();
		motorB.resetTachoCount();
	}
	public void arcDroit() {
		Delay.msDelay(DELAI500);
		motorA.startSynchronization();
		motorB.rotate(ARC_ROTATION2);
		motorA.rotate(ARC_ROTATION1);
        motorA.endSynchronization();
		motorA.waitComplete();
        motorB.waitComplete();
        Delay.msDelay(DELAI200);
	}
	public void arcGauche() {
		Delay.msDelay(DELAI500);
		motorA.startSynchronization();
		motorB.rotate(ARC_ROTATION1);
		motorA.rotate(ARC_ROTATION2);
        motorA.endSynchronization();
        motorA.waitComplete();
        motorB.waitComplete();
        Delay.msDelay(DELAI200);
	}
	public void tourcomplet() {
		Delay.msDelay(DELAI500);
		motorA.startSynchronization();
		motorA.backward();
		motorB.forward();
		motorA.endSynchronization();
	}
	public void rotateA() {
		//motorA.rotate(50);
		motorA.setSpeed(ROTATION200);
		motorA.forward();
	}
	public void rotateB() {
		//motorB.rotate(50);
		motorB.setSpeed(ROTATION200);
		motorB.forward();
	}
	public void correctionGauche2() {
		motorA.setSpeed(VITESSE_CORRECTION);
		motorB.setSpeed(VITESSE_CORRECTION);
		Delay.msDelay(DELAI250);
		motorA.startSynchronization();
		motorB.backward();
		motorA.forward();
		motorA.endSynchronization();
	}
	public void correctionDroite2() {
		motorA.setSpeed(VITESSE_CORRECTION);
		motorB.setSpeed(VITESSE_CORRECTION);
		Delay.msDelay(DELAI250);
		motorA.startSynchronization();
		motorA.backward();
		motorB.forward();
		motorA.endSynchronization();
	}
	public void correctionGauche3() {
		stop();
		motorA.setSpeed(VITESSE_CORRECTION);
		motorB.setSpeed(VITESSE_CORRECTION);
		Delay.msDelay(DELAI250);
		motorA.startSynchronization();
		motorB.backward();
		motorA.endSynchronization();
	}
	public void correctionDroite3() {
		stop();
		motorA.setSpeed(VITESSE_CORRECTION);
		motorB.setSpeed(VITESSE_CORRECTION);
		Delay.msDelay(DELAI250);
		motorA.startSynchronization();
		motorA.backward();
		motorA.endSynchronization();
	}
}
