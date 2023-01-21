import java.lang.reflect.*;
import java.lang.Exception.*;
import java.util.*;

public class Test_creation_stub {
    public static void main(String args[]) {
        System.out.println("Test créateur de classe à partir d'un objet avec annotation :");
        Object test = new Sentence_test();
        String className = test.getClass().getName();

        try{/*
            System.out.println("    Suppression ancienne classe déjà créée :");
            String[] commandeSupprJavaItf = new String[] {"rm",className+"_itf.java"};
            Process processEnCours = Runtime.getRuntime().exec(commandeSupprJavaItf);
            processEnCours.waitFor();
            System.out.println("fichier interface java supprimée");
            
            String[] commandeSupprClassItf = new String[] {"rm",className+"_itf.class"};
            processEnCours = Runtime.getRuntime().exec(commandeSupprClassItf);
            processEnCours.waitFor();            
            System.out.println("fichier interface class supprimée");
             */

            String[] commandeSupprJavaStub = new String[] {"rm",className+"_stub.java"};
            Process processEnCours = Runtime.getRuntime().exec(commandeSupprJavaStub);
            processEnCours.waitFor();
            System.out.println("fichier stub java supprimée");
            
            String[] commandeSupprClassStub = new String[] {"rm",className+"_stub.class"};
            processEnCours = Runtime.getRuntime().exec(commandeSupprClassStub);
            processEnCours.waitFor();
            System.out.println("fichier stub class supprimée");

        } catch(Exception e){
            System.out.println("Error suppression ancien résultat");
        }
        try {
            System.out.println("    Génération fichiers :");
            SharedObject sharedObject = Fonctions_generateur_stub.CreateStub(0, test);
            if (sharedObject == null){
                System.out.println("Echec création : sharedObject vide");
            } else {                
                System.out.println("Réussite création : sharedObject non vide");
            }
        } catch(Exception e){
            System.out.println("Error test génération stub");
        }
	}
}