import java.net.*;
import java.io.*;
public class Client_test_etats extends Thread {
    static SharedObject[] listSharedObject = new SharedObject[10];
    static String[] attentuDepart1 = {"RLC","NL","NL","NL","WLC","NL","NL","NL","RLC","RLC"};
    static String[] attentuDepart2 = {"NL","NL","RLT","WLC","NL","RLT_WLC","RLC","WLC","RLC","RLT"};
    
    static String[] attentuArrivee1 = {"RLT","RLT","RLT","RLT","RLT_WLC","RLT","WLT","WLT","WLT","WLT"};
    static String[] attentuArrivee2 = {"NL","NL","RLT","RLC","NL","RLT","NL","NL","NL","NL"};

    
    static String[] attentuDepartServer = {"RL","NL","RL","WL","WL","WL","RL","WL","RL","RL"};
    static String[] attentuArriveeServer = {"RL","RL","RL","RL","WL","RL","WL","WL","WL","WL"};
    static int numClient;

    int envoieSocket;   
    public Client_test_etats(int i) {
        this.envoieSocket = i;
    }    
    public void run() {
        Boolean reponse = false;
        while(!reponse){
            try {
                Socket s = new Socket("localhost", 8080);

                OutputStream os = s.getOutputStream();
                InputStream is = s.getInputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
                ObjectInputStream ois = new ObjectInputStream(is);

                oos.writeObject(this.envoieSocket);
                reponse = (Boolean)ois.readObject();

                oos.close();
                ois.close();
                os.close();
                is.close(); 
                s.close(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main (String[] args) throws Exception{
        if (args.length != 1){
            System.out.println("java Client_test_etats <numClient>");
			return;
        }

        if (args[0].equals("1")){
            numClient = 1;
        } else if (args[0].equals("2")){
            numClient = 2;
        } else {            
            System.out.println("java Client_test_etats <numClient> : numClient = 1 ou 2");
			return;
        }
        Client.init();
        System.out.println("----Création sharedObject pour les 10 tests pour le client "+numClient);
        
        System.out.println("Création test 1");
        SharedObject s1 = Client.lookup("Test 1");
        if (s1 == null) {
            s1 = Client.create(new Sentence());
            Client.register("Test 1", s1);
        }
        if (numClient == 1) {                
            s1.lock_read();
            s1.unlock();
        } else {
            ;;
        }
        listSharedObject[0] = s1;

        System.out.println("Création test 2");
        SharedObject s2 = Client.lookup("Test 2");
        if (s2 == null) {
            s2 = Client.create(new Sentence());
            Client.register("Test 2", s2);
        }
        listSharedObject[1] = s2;
        
        System.out.println("Création test 3");
        SharedObject s3 = Client.lookup("Test 3");
        if (s3 == null) {
            s3 = Client.create(new Sentence());
            Client.register("Test 3", s3);
        }
        if (numClient == 1) {    
            ;;
        } else {
            s3.lock_read();
        }
        listSharedObject[2] = s3;

        System.out.println("Création test 4");
        SharedObject s4 = Client.lookup("Test 4");
        if (s4 == null) {
            s4 = Client.create(new Sentence());
            Client.register("Test 4", s4);
        }
        if (numClient == 1) {    
            ;;
        } else {
            s4.lock_write();
            s4.unlock();
        }
        listSharedObject[3] = s4;

        System.out.println("Création test 5");
        SharedObject s5 = Client.lookup("Test 5");
        if (s5 == null) {
            s5 = Client.create(new Sentence());
            Client.register("Test 5", s5);
        }
        if (numClient == 1) {    
            s5.lock_write();
            s5.unlock();
        } else {
            ;;
        }
        listSharedObject[4] = s5;

        System.out.println("Création test 6");
        SharedObject s6 = Client.lookup("Test 6");
        if (s6 == null) {
            s6 = Client.create(new Sentence());
            Client.register("Test 6", s6);
        }
        if (numClient == 1) {  
            ;;
        } else {  
            s6.lock_write();
            s6.unlock();
            s6.lock_read();
        }
        listSharedObject[5] = s6;

        System.out.println("Création test 7");
        SharedObject s7 = Client.lookup("Test 7");
        if (s7 == null) {
            s7 = Client.create(new Sentence());
            Client.register("Test 7", s7);
        }
        if (numClient == 1) {  
            ;;
        } else {  
            s7.lock_read();
            s7.unlock();
        }
        listSharedObject[6] = s7;

        System.out.println("Création test 8");
        SharedObject s8 = Client.lookup("Test 8");
        if (s8 == null) {
            s8 = Client.create(new Sentence());
            Client.register("Test 8", s8);
        }
        if (numClient == 1) {  
            ;;
        } else {  
            s8.lock_write();
            s8.unlock();
        }
        listSharedObject[7] = s8;

        System.out.println("Création test 9");
        SharedObject s9 = Client.lookup("Test 9");
        if (s9 == null) {
            s9 = Client.create(new Sentence());
            Client.register("Test 9", s9);
        }
        if (numClient == 1) {
            s9.lock_read();
            s9.unlock();
        } else {  
            s9.lock_read();            
            s9.unlock();
        }
        listSharedObject[8] = s9;

        System.out.println("Création test 10");
        SharedObject s10 = Client.lookup("Test 10");
        if (s10 == null) {
            s10 = Client.create(new Sentence());
            Client.register("Test 10", s10);
        }
        if (numClient == 1) {
            s10.lock_read();            
            s10.unlock();
        } else {  
            s10.lock_read();
        }
        listSharedObject[9] = s10;

        System.out.println("----Vérification des tests initialisés pour le client "+numClient);
        for (int test = 0; test<10; test++){
            System.out.println("Test "+Integer.toString(test+1)+"...");
            if (numClient == 1) {
                if (!verificationEtat(listSharedObject[test].getLock().toString(),attentuDepart1[test])){
                    throw new Exception("Initialisation test "+Integer.toString(test+1)+" du client "+numClient+" a échoué.");
                }
            } else {
                if (!verificationEtat(listSharedObject[test].getLock().toString(),attentuDepart2[test])){
                    throw new Exception("Initialisation test "+Integer.toString(test+1)+" du client "+numClient+" a échoué.");                    
                }
            }
            
            System.out.println("... bien initialisé");
            
        }       

        System.out.println("----Différents tests initialisés pour le client "+numClient+", attente en socket de la préparation du client 2");
        try {            
            Thread attenteAutreClient = new Client_test_etats(numClient);
            attenteAutreClient.start();
            attenteAutreClient.join();
        } catch (Exception e) {
            e.printStackTrace();
        }       
        System.out.println("Les deux clients sont initialisés");
        if (numClient == 1){
            System.out.println("----Lancement test Client 1");
            for (int test = 0; test<10; test++){                
                System.out.println("Test "+Integer.toString(test+1)+"...");
                if (test<=5){
                    listSharedObject[test].lock_read(); 
                } else if (test<=8) {
                    listSharedObject[test].lock_write(); 
                } else {
                    try {
                        Thread attenteAutreClient = new Client_test_etats(-10);
                        final Thread test10 = new Thread(new test10());
                        test10.start();                        
                        attenteAutreClient.start();
                        System.out.println("Attente unlock de la part du Client 2");
                        attenteAutreClient.join();
                        test10.join();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    

                }

                if (verificationEtat(listSharedObject[test].getLock().toString(),attentuArrivee1[test])){
                    System.out.println("Client "+numClient+", test "+Integer.toString(test+1)+" réussi");           
                } else {
                    throw new Exception("Réalisation test "+Integer.toString(test+1)+" du client "+numClient+" a échoué.");  
                }
            }
            System.out.println("Tous les tests sont passés\nEnvoie par socket signal au client 2 que les changements ont été faits");
            try {            
                Thread attenteAutreClient = new Client_test_etats(-numClient);
                attenteAutreClient.start();
                attenteAutreClient.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            
            System.out.println("----Attente lancement test Client 1");
            try {            
                Thread attenteAutreClient = new Client_test_etats(-20);
                attenteAutreClient.start();
                attenteAutreClient.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Client 1 a lancé le test 10");
            if (!verificationEtat(listSharedObject[9].getLock().toString(),attentuDepart2[9])){
                throw new Exception("Changement imprévu test 10 du client 2");                  
            }
            listSharedObject[9].unlock();
            System.out.println("Attente fin complète des tests de Client 1");
            try {            
                Thread attenteAutreClient = new Client_test_etats(-numClient);
                attenteAutreClient.start();
                attenteAutreClient.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int test = 0; test<10; test++){
                System.out.println("Test "+Integer.toString(test+1)+"...");
                
                if (!verificationEtat(listSharedObject[test].getLock().toString(),attentuArrivee2[test])){
                    throw new Exception("Réalisation test "+Integer.toString(test+1)+" du client "+numClient+" a échoué.");                    
                } else {                    
                    System.out.println("... réussi");
                }                
                
            }  

        }
        System.out.println("Fin tests client "+numClient);
        System.exit(0);
        
        

    }

    

    public static boolean verificationEtat(String courant, String attendu) {
        System.out.println("Etat Shared Object courant : "+courant+"\nEtat Shared Object attendu : "+attendu);
        if (courant.equals(attendu)) {
            return true;
        } else {            
            return false;
        }
    }

    static class test10 implements Runnable{
        public test10(){
            ;;
        }

        public void run() {            
            listSharedObject[9].lock_write(); 
            
        }

    }

}
