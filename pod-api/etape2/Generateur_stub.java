

import java.util.*;

import javax.xml.stream.events.EndDocument;

import java.lang.*;
import java.lang.reflect.Constructor;


public class Generateur_stub {
/*
    public static void main(String[] args) throws ClassNotFoundException{
        if(args.length != 1) {
            System.out.println("java Generateur_stub <class>");
            Generateurstub(Class.forName(args[0]));
        }
        
    }
    */
    public static void Generateurstub (int id, Object objet){
        
        String className = objet.getClass().getName();
        Class<?> classStub = null;
        try {
            classStub = Class.forName(className+"_stub");
        } catch (ClassNotFoundException e){
            
        }
        Object stub = null;
      
        if (classStub == null) {
            //Générer class stub

        } 
        Constructor<?>[] constructeurs = null;
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
        }  


    }

        
         
}

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

}