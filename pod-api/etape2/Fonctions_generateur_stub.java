import java.lang.reflect.*;
import java.lang.Exception.*;

import java.util.*;
import java.io.*;

import javax.xml.stream.events.EndDocument;


public class Fonctions_generateur_stub {
/*
    public static void main(String[] args) throws ClassNotFoundException{
        if(args.length != 1) {
            System.out.println("java Generateur_stub <class>");
            Generateurstub(Class.forName(args[0]));
        }
        
    }
    */
    public static SharedObject CreateStub (int id, Object objet){
        SharedObject sharedObject = null;
        try {
            String className = objet.getClass().getName();
            Class<?> classStub = null;
            try {
                classStub = Class.forName(className+"_stub");
            } catch (ClassNotFoundException e){
                classStub = GenerateurClassStub(objet);
            }
            Object stub = null;
            Constructor <?> constructeur = classStub.getConstructor(new Class[]{int.class, Object.class});
            sharedObject = (SharedObject) constructeur.newInstance(new Object[]{id,objet});
        } catch (Exception e){
			e.printStackTrace();
		}
        return sharedObject;
    }

        
    public static Class<?> GenerateurClassStub(Object objet){
        Class<?> classStub = null;
        try{ 
            Class<?> classe = objet.getClass();
            String className = classe.getName();
            File classFile = new File(className+"_stub.java");
            if (classFile.createNewFile()) {
                FileWriter classFileWrite = new FileWriter(classFile);
                //writing start class
                classFileWrite.write("public class "+className+"_stub extends SharedObject implements "+className+"_itf, java.io.Serializable {");
                //writing constructor
                classFileWrite.write("public "+className+"_stub (int id, Object obj) {super(id,obj);}");
                Method[] methodes = classe.getMethods();
                for (Method methode : methodes){
                    
                    String typeReturn = methode.getReturnType().getName();
                    String nameMethode = methode.getName();
                    Class<?>[] paramTypes = methode.getParameterTypes();
                    int nbParam = methode.getParameterCount();
                    String paramEtTypesString = "";
                    String paramString = "";
                    
                    for (int i = 0;i<nbParam;i++){
                        paramEtTypesString+=paramTypes[i].getName()+" a"+Integer.toString(i);
                        paramString+="a"+Integer.toString(i);
                        if (i<nbParam - 1){
                            paramEtTypesString+=",";
                            paramString+=",";
                        }
                    
                    }
                    classFileWrite.write("public "+typeReturn+" "+nameMethode+" ("+paramEtTypesString+"){");
                    /*if(methode.getAnnotation(Write.class)!=null){ 
                        classFileWrite.write(className+" object = ("+className+") this.lock_write;");
                    } elseif (methode.getAnnotation(Read.class)!=null){
                        classFileWrite.write(className+" object = ("+className+") this.lock_read;");
                    } else {

                    */
                    classFileWrite.write(className+" object = ("+className+") obj;");
                    //}
                    if (typeReturn.equals("void")){
                        classFileWrite.write("object."+nameMethode+"("+paramString+");");
                        /*if(methode.getAnnotation(Write.class)!=null) ||  (methode.getAnnotation(Read.class)!=null) { 
                            classFileWrite.write("this.unlock();");
                        }*/
                    } else {
                        /*if(methode.getAnnotation(Write.class)!=null) ||  (methode.getAnnotation(Read.class)!=null) {
                         * classFileWrite.write("Object o =  object."+nameMethode+"("+paramString+"); this.unlock(); return o;");
                         * }else{
                        */
                        classFileWrite.write("return object."+nameMethode+"("+paramString+");");
                        //}
                    }
                    
                    classFileWrite.write("}");
                }
                classFileWrite.write("}");
        
                classFileWrite.close();
                //compiler et attendre fin compilation
                classStub = Class.forName(className+"_stub");
            } else {
                System.out.println("Internal Error : "+className+"_stub.java already exists.");
            }

        } catch (Exception e){
			e.printStackTrace();
		}
        return null;
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

