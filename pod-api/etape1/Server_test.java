import java.net.*;
import java.io.*;

import java.util.*;
import java.util.concurrent.locks.*;

public class Server_test {
    public static void main (String[] args){
        Thread t_server = null;
        try{
            lanceurServer lancementServeur = new lanceurServer();
            t_server = new Thread(lancementServeur);
            t_server.start();
            //t_server.join();
        } catch (Exception e){
            System.out.println("Fin du thread de lancement échec");            
			e.printStackTrace();
        }
        boolean client1Pret = false;
        boolean client2Pret = false;
        boolean testLanceeClient1 = false;
        boolean Client2RecuFinTest = false;
        boolean test10LanceeClient1 = false;

        try {
            System.out.println("Lancement server socket");
            ServerSocket ss = new ServerSocket(8080);
            while (!client1Pret || !client2Pret || !testLanceeClient1 || !Client2RecuFinTest){
                Socket s = ss.accept();

                OutputStream os = s.getOutputStream();
                InputStream is = s.getInputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                ObjectInputStream ois = new ObjectInputStream(is);

                int numClient = (int)ois.readObject();
                if (numClient == -1){
                    oos.writeObject(true);
                    System.out.println("Client 1 a terminé ses tests.");
                    testLanceeClient1 = true;
                } else if(numClient == -2){
                    oos.writeObject(testLanceeClient1);
                    Client2RecuFinTest = testLanceeClient1;
                } else if(numClient == -10){                    
                    oos.writeObject(true);                    
                    System.out.println("Client 1 réalise le test 10, en attente de client 2.");
                    test10LanceeClient1 = true;
                } else if (numClient == -20){
                    oos.writeObject(test10LanceeClient1);
                } else {
                    System.out.println("Client " + numClient + " prêt");
                    boolean renvoie = false;
                    if (numClient == 1){
                        client1Pret = true;
                        renvoie = client2Pret;
                    } else {
                        client2Pret = true;
                        renvoie = client1Pret;
                    }
                    oos.writeObject(renvoie);
                    System.out.println(renvoie+" envoyé au client " + numClient);
    
                }
                
                oos.close();
                ois.close();
                os.close();
                is.close();
            }
        } catch (Exception e) {            
            e.printStackTrace();
        }
        System.out.println("Tests réalisés, fermer ce programme à l'aide d'un Ctrl C pour pouvoir arrêter le serveur");
    }
}


class lanceurServer implements Runnable{
    Process processServeur;
    
    public lanceurServer(){
        this.processServeur=null;
    }
    public synchronized void run() {     
        String[] commandeLanceurServer = new String[] {"java","Server"};
        try{
            
            this.processServeur = Runtime.getRuntime().exec(commandeLanceurServer);
            System.out.println("Serveur lancé, vous pouvez lancé les clients de test.");
            this.processServeur.waitFor();
        }catch (Exception e){
            System.out.println("Lancement serveur échoué");           
            e.printStackTrace();
        }
        
        
    }
}