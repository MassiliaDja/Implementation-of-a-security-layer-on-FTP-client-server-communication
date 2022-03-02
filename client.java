package RX;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;
import java.security.*;

public class client {	
	static int clefi ;
   public static void main(String[] args)   throws Exception  {
		
	  //Port 20 et 21 pour le protocole FTP.
	  Socket duclient = new Socket("127.0.0.1", 21);
	  
	  //Demander et recuperer le nom d'utilisateur entré par l'utilisateur au clavier.
	  System.out.println(" Entrer votre nom d'utilisateur : ");	
	  Scanner sc = new Scanner(System.in);	
	  String s1 = sc.next(); 
	
	  //Envoie les données du nom d'utilisateur au serveur.
	  DataOutputStream outclient = new DataOutputStream(duclient.getOutputStream());
	  outclient.writeUTF(s1); 
	
	  //Recup les données envoye par le serveur : Bienvenue...
	  DataInputStream inclient = new DataInputStream(duclient.getInputStream());
	  String s2 = inclient.readUTF();
	  System.out.println(s2);	
	
	  //Demander et recuperer le mot de pass entré par l'utilisateur au clavier.
	  System.out.println(" Enter votre mot de pass : ");
	  String pass = sc.next(); 
	  sc.close();	
	
	  //Recup le nombre aléatoire envoyé par le serveur.
	  String nbraleat = inclient.readUTF(); 	
	  
	  //Mes clef de chiffrement que le serveur connait aussi.
	  String clefs1 = "66548858";	
		String clefs2 = "TfGiuZErg";	
		clefi = 5;		  
	  
	  //Clefs1 + Hashage + pass : representent le mot de pass a hacher en MD5.
	  String mdpahacher= clefs1+nbraleat+pass;
		
	  //Hacher le mot de pass.
	  String resultchifrmnt = MD5(mdpahacher);
	  
	  //Clef2 + Hashage representent le mot de pass a envoyer au serveur.
	  String mdpc= clefs2+resultchifrmnt;	 
	
	  //Envoie le resultat au serveur.
	  outclient.writeUTF(mdpc);
	  
	  //Lire la cond reçue par le serveur: si le mot de pass est bon ou pas.
	  String cond = inclient.readUTF();	
	
	  //Le mot de pass est bon.
    if(cond.equals("1")) {
	
      System.out.println(" Réception du fichier en cours...");
	  //Le mot de pass est bon donc; recevoir le fichier.
	  InputStream is = duclient.getInputStream();	
      new client().reception(is);     
      System.out.println(" Fichier reçu !");
      duclient.close();}
	
	
	   //Le mot de pass est erroné.
    else{System.out.println(" Mot de pass incorrect !");}
	
}	
	
	
	
      //Fonction de reception du fichier :  FichierATransferer.txt et le stocker localement.
   public void reception(InputStream ins) throws Exception {
	   
	   //reception du fichier
        int fichiertaille = 6022386;
        byte[] mbt = new byte[fichiertaille];
        FileOutputStream fouts = new FileOutputStream("FichierATransferer.txt");    
        BufferedOutputStream bouts = new BufferedOutputStream(fouts);       
        ins.read(mbt);
        bouts.write(mbt);
        bouts.flush();
        bouts.close();
        
        
        //Lecture du fichier 
        File monfichier = new File("FichierATransferer.txt") ;        
        FileReader lecture = new FileReader(monfichier);
        BufferedReader br = new BufferedReader(lecture);
        String monmessage = br.readLine();
        
       //Dechiffrer le message     
		String monmessagedechiff = "" ;
	    char[]  decchars = monmessage.toCharArray();
		for(char cle : decchars) {
	    cle -= clefi;
	    monmessagedechiff = monmessagedechiff+cle;}
		
	   //Lecture a l'ecran du message chiffré se trouvant dans le fichier  
        System.out.println(" Le message du fichier : "+monmessage);
                
       //Lecture a l'ecran du message en clair 
        System.out.println(" Le message en clair : "+monmessagedechiff);}
    
    
      //Fonction de hachage MD5.
   public static String MD5(String s1) throws Exception  {
	   
		String sortiemdp;		
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s1.getBytes(),0, s1.length());
		byte[] dig = m.digest();
	    sortiemdp = new BigInteger(1,dig).toString();		
		return sortiemdp;}
	
	
}
