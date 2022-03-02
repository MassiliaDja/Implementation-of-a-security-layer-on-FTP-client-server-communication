package RX;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;



public class serveur {
	
	static int clefi ;
   public static void main(String[] args) throws Exception{
		
	    //Port 20 et 21 pour le protocole FTP.
        ServerSocket serv = new ServerSocket(21); 
        System.out.println(" En attente du client...");
        
        //Acceptation de la connexion du client.
        Socket duserveur = serv.accept(); 	
		System.out.println(" Connexion acceptée !");
		
		//Recup les données du nom d'utilisateur envoyé par le client.
		DataInputStream inserveur = new DataInputStream(duserveur.getInputStream()); 
		String s1 = inserveur.readUTF(); 	
		String s3 = " Bienvenue "+s1+" ,vous etes bien connecté.";
		
		//Envoie au client les données : Bienvenue...
		DataOutputStream outserveur = new DataOutputStream(duserveur.getOutputStream()); 
		outserveur.writeUTF(s3);			
		
		//Lire le mot de pass envoyé pas le client, LE MOT DE PASS POUR FAIRE UN TEST EST : thepass .
		String pass= "thepass";
			
		//Generation du nombre aléatoire r.
		int r1 = ThreadLocalRandom.current().nextInt(247,461);
		int r2 = ThreadLocalRandom.current().nextInt(135,256);
		int nbral = r1*r2;
		String nbraleat = String.valueOf(nbral);
		
		//Envoie du nombre aléatoire au client.
		outserveur.writeUTF(nbraleat);		
		
		//Ma clefs de chiffrement que le client connait aussi.
		String clefs1 = "66548858";	
		String clefs2 = "TfGiuZErg";	
		clefi = 5;	  
		
		//Clefs1 + Hashage + pass : representent le mot de pass a hacher en MD5.
		String mdpahacher= clefs1+nbraleat+pass;
				
		//Hachage du mot de pass.
		String mdps = MD5(mdpahacher);		    
		
		//Recup le mot de pass envoye par le client.
		String mdprecu  = inserveur.readUTF();
		
		//Dechiffrer le mot de pass reçu par le client avec la clef2.
		String mdpc = mdprecu.replace(clefs2, "");
				
		//Condition pour le client.
		String cond ;
				
	    //Le mot de passe est bon.
	 if (mdps.equals(mdpc)) {
		cond="1";   // indiquer au client que la mot de pass est bon.
		outserveur.writeUTF(cond);			
		System.out.println(" Authentification reussie ! ");			
			
	    //Le mot de pass est bon donc; recevoir le fichier.
		OutputStream os = duserveur.getOutputStream();
        new serveur().envoie(os);         
        System.out.println(" Transfert du fichier effectué !");
        duserveur.close();
        serv.close();}		
	
	    //Le mot de passe est erroné.
	  else {
		cond="-1";  // indiquer au client que la mot de pass n'est pas bon.
		outserveur.writeUTF(cond);	
		System.out.println(" Authentification echoué ! le mot de pass est incorrect.");}
	  
	}
	
   
   
	   //Fonction d'envoie du fichier :  FichierATransferer.txt .
   public void envoie(OutputStream ous) throws Exception {
	   
	   //Recup le message au clavier   
	    System.out.println(" Le message que vous voulez faire parvenir au client :");
	    Scanner sc = new Scanner(System.in);	
	    String monmessage = sc.nextLine(); 
	    sc.close();  
	   
	    //Chiffrer le message
        String monmessagechiffre = "" ;
		int cledechiff  = clefi;		 
		char[]  chars = monmessage.toCharArray();
		for(char c : chars) {
	     c += cledechiff;
	     monmessagechiffre = monmessagechiffre+c;}
		
		//Mettre le message dans le fichier txt
		File monfichier = new File("C:/Users/DELL/OneDrive/Documents/eclipse-workspace/Sec_des_ptcl/FichierATransferer.txt");
		BufferedWriter bw  = new BufferedWriter( new FileWriter(monfichier));       
        bw.write(monmessagechiffre);
        bw.newLine();
        bw.flush();
        bw.close();
   
        //Envoie du fichier 
        byte[] mba = new byte[(int) monfichier.length() + 1];
        FileInputStream fouts = new FileInputStream(monfichier);
        BufferedInputStream bouts = new BufferedInputStream(fouts);
        bouts.read(mba);
        System.out.println(" Envoie du fichier en cours...");
        ous.write(mba);
        ous.flush();
        bouts.close();}	
   
	
	
	   //Fonction de hachage MD5.
   public static String MD5(String s) throws Exception  {
	  
		String sortiemdp;
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(),0, s.length());
		byte[] dig = m.digest();
		sortiemdp = new BigInteger(1,dig).toString();						
		return sortiemdp;}	 	 
	 
} 
