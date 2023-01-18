import java.lang.reflect.*;
import java.lang.Exception.*;

import java.util.*;
import java.io.*;

import javax.xml.stream.events.EndDocument;


public class Fonctions_generateur_stub {

    public static SharedObject CreateStub (int id, Object objet){
        SharedObject sharedObject = null;
        try {
            String className = objet.getClass().getName();
            Class<?> classStub = null;
            try {
                System.out.println("Recherche "+className+"_stub.class ...");
                classStub = Class.forName(className+"_stub");                
                System.out.println(className+"_stub.class trouvée");
            } catch (ClassNotFoundException e){                
                System.out.println("Création class...");
                classStub = GenerateurClassStub(objet);
                System.out.println("...Class créée et récupérée");
            }
            System.out.println("Récupération constructeur...");
            Constructor <?> constructeur = classStub.getConstructor(new Class[]{int.class, Object.class});
            System.out.println("Création Shared Object...");
            sharedObject = (SharedObject) constructeur.newInstance(new Object[]{id,objet});
        } catch (Exception e){
			e.printStackTrace();
		}
        System.out.println("Retour du Shared Object");
        return sharedObject;
    }

        
    public static Class<?> GenerateurClassStub(Object objet){
        Class<?> classStub = null;
        try{ 
            Class<?> classe = objet.getClass();
            String className = classe.getName();
            File classFileStub = new File(className+"_stub.java");
            File classFileItf = new File(className+"_itf.java");
            if (classFileStub.createNewFile()) {
                
                System.out.println("Début écriture "+className+"_stub.java");
                FileWriter classFileWriteStub = new FileWriter(classFileStub);
                //writing start class
                classFileWriteStub.write("public class "+className+"_stub extends SharedObject implements "+className+"_itf, java.io.Serializable {\n");
                //writing constructor
                classFileWriteStub.write("public "+className+"_stub(int id, Object obj) {\n    super(id,obj);\n}\n");
                Method[] basicMethodes = Class.forName("vide").getMethods();
                Method[] methodes = classe.getMethods();
                for (Method methode : methodes){
                    Boolean NotNeeded = false;
                    for (Method basicMethode : basicMethodes){
                        NotNeeded = basicMethode.equals(methode) || NotNeeded;
                    }
                    if (!NotNeeded) {
                        String typeReturn = methode.getReturnType().getName();
                        if (typeReturn.equals("java.lang.Class")){
                            typeReturn="Class<?>";
                        } else if (typeReturn.equals("java.lang.String")){
                            typeReturn="String";
                        } else if (typeReturn.equals("java.lang.Object")){
                            typeReturn="Object";
                        }
                        String nameMethode = methode.getName();
                        System.out.println("Ecriture dans stub méthode : "+nameMethode);
                        Class<?>[] paramTypes = methode.getParameterTypes();
                        int nbParam = methode.getParameterCount();
                        String paramEtTypesString = "";
                        String paramString = "";
                        
                        for (int i = 0;i<nbParam;i++){
                            String typeParam = paramTypes[i].getName();
                            if (typeParam.equals("java.lang.Class")){
                                typeParam="Class<?>";
                            } else if (typeParam.equals("java.lang.String")){
                                typeParam="String";
                            } else if (typeParam.equals("java.lang.Object")){
                                typeParam="Object";
                            }
                            paramEtTypesString+=typeParam+" a"+Integer.toString(i);
                            
                            paramString+="a"+Integer.toString(i);
                            if (i<nbParam - 1){
                                paramEtTypesString+=",";
                                paramString+=",";
                            }
                        
                        }
                        classFileWriteStub.write("public "+typeReturn+" "+nameMethode+"("+paramEtTypesString+"){\n");

                        if (methode.getAnnotation(Write.class)!=null) { 
                            classFileWriteStub.write("    this.lock_write();\n");
                        } else if (methode.getAnnotation(Read.class)!=null) {
                            classFileWriteStub.write("    this.lock_read();\n");
                        } 
                        classFileWriteStub.write("    "+className+" object = ("+className+") obj;\n");
                        if (typeReturn.equals("void")){
                            classFileWriteStub.write("    object."+nameMethode+"("+paramString+");\n");
                            if ((methode.getAnnotation(Write.class)!=null) ||  (methode.getAnnotation(Read.class)!=null)) { 
                                classFileWriteStub.write("    this.unlock();\n");
                            }
                        } else {
                            if ((methode.getAnnotation(Write.class)!=null) ||  (methode.getAnnotation(Read.class)!=null)) {
                                classFileWriteStub.write(typeReturn+ "    r = object."+nameMethode+"("+paramString+");\n    this.unlock();\n    return r;\n");
                            }else{
                            
                            classFileWriteStub.write("  return object."+nameMethode+"("+paramString+");\n");
                            }
                        }
                        
                        classFileWriteStub.write("}\n");
                    }
                }
                classFileWriteStub.write("}");
        
                classFileWriteStub.close(); 
                System.out.println("Fin écriture "+className+"_stub.java");
                
            } else {
                System.out.println("Internal Error : "+className+"_stub.java already exists.");
            }

            if (classFileItf.createNewFile()) {
                
                System.out.println("Début écriture "+className+"_itf.java");
                FileWriter classFileWriteItf = new FileWriter(classFileItf);
                //writing start class
                classFileWriteItf.write("public interface "+className+"_itf extends SharedObject_itf {\n");
                
                Method[] basicMethodes = Class.forName("vide").getMethods();
                Method[] methodes = classe.getMethods();
                for (Method methode : methodes){
                    Boolean NotNeeded = false;
                    for (Method basicMethode : basicMethodes){
                        NotNeeded = basicMethode.equals(methode) || NotNeeded;
                    }
                    if (!NotNeeded) {
                    
                        String typeReturn = methode.getReturnType().getName();
                        if (typeReturn.equals("java.lang.Class")){
                            typeReturn="Class<?>";
                        } else if (typeReturn.equals("java.lang.String")){
                            typeReturn="String";
                        } else if (typeReturn.equals("java.lang.Object")){
                            typeReturn="Object";
                        }
                        String nameMethode = methode.getName();
                        System.out.println("Ecriture dans itf méthode : "+nameMethode);
                        Class<?>[] paramTypes = methode.getParameterTypes();
                        int nbParam = methode.getParameterCount();
                        String paramEtTypesString = "";
                        
                        for (int i = 0;i<nbParam;i++){
                            String typeParam = paramTypes[i].getName();
                            if (typeParam.equals("java.lang.Class")){
                                typeParam="Class<?>";
                            } else if (typeParam.equals("java.lang.String")){
                                typeParam="String";
                            } else if (typeParam.equals("java.lang.Object")){
                                typeParam="Object";
                            }
                            paramEtTypesString+=typeParam+" a"+Integer.toString(i);
                            if (i<nbParam - 1){
                                paramEtTypesString+=",";
                            }
                        
                        }
                        classFileWriteItf.write("public "+typeReturn+" "+nameMethode+" ("+paramEtTypesString+");\n");
                        }
                }
                classFileWriteItf.write("}");
        
                classFileWriteItf.close();
                System.out.println("Fin écriture "+className+"_itf.java");
            } else {
                System.out.println("Internal Error : "+className+"_itf.java already exists.");
            }            
            System.out.println("Compilation...");
            String[] commandeItf = new String[] {"javac",className+"_itf.java"};
            Process processEnCours = Runtime.getRuntime().exec(commandeItf);
            processEnCours.waitFor();
            String[] commandeStub = new String[] {"javac",className+"_stub.java"};
            processEnCours = Runtime.getRuntime().exec(commandeStub);
            processEnCours.waitFor();
            
            System.out.println("Réucpération class...");
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();

            classStub = classLoader.loadClass(className+"_stub");

        } catch (Exception e){
			e.printStackTrace();
		}
        return classStub;
    }
}

/*Constructor<?>[] constructeurs = null;
        try {
            constructeurs = classStub.getConstructors();
        } catch (SecurityException e){
            e.printStackTrace();
        } 
        Boolean IdFirst = false;
        Constructor<?> constructeur = null;
        for (Constructor<?> constructor : constructeurs) {
            if (constructor.getParameterCount()==2){
                Class<?>[] listesTypes = constructor.getParameterTypes();
                if (listesTypes[1].getTypeName().equals("int") || listesTypes[1].getTypeName().equals("java.lang.Object") & (listesTypes[0].getTypeName().equals("int") || listesTypes[0].getTypeName().equals("java.lang.Object")) {
                    constructeur = constructor;
                }
                    
            }
        }   */

/*
    public static String MethodWrite(Class class){
        Method[] methodes = class.getMethods();
        for (Method methode : methodes){
            if(methode.getAnnotation(Write.class)!=null){

            }

        }



        /*
        Pour créer stub besoin créer itf
        Pour créer itf besoin lire objet
         
    }*/

