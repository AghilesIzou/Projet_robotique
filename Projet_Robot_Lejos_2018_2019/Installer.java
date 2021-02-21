package installer;

import java.io.File;
import javax.swing.JOptionPane;



public class Installer {
	
	public static void main(String[] args){
		boolean valide = false;
		File f = new File("text.txt");
		StringBuffer local = new StringBuffer();
		do {
		local.delete(0, local.length());
		local.append(JOptionPane.showInputDialog("entrez le chemin du fichier"));
		f= new File(local.toString());
		valide = f.exists();
		if (valide) {
		valide = local.substring(local.length()-3).equals("jar");
		}
		if (!f.getName().equals("null"))
		JOptionPane.showMessageDialog(null,""+f.getName()+" "+(valide?"fichier valide":"fichier non valide"),"fichier sélectionné",JOptionPane.INFORMATION_MESSAGE);
		}while (!valide&&!f.getName().equals("null"));
		if (valide) {
			String target = "/home/lejos/programs";
			String fileName = f.getName();
        	String user = "root";
        	String host = "10.0.1.1";
        	int port = 22;
        	String keyFilePath = null;
        	String keyPassword = null;
        	String command = "scp " + "-oKexAlgorithms=+diffie-hellman-group1-sha1 -c aes128-cbc " + local + " " + user + "@" + host + ":"+target;
        	try {
        		Process p = Runtime.getRuntime().exec(command);
        	}catch (Exception e) {}
		}
	}
	
	
}
