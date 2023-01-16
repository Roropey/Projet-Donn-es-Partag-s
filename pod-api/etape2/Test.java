import java.lang.reflect.*;
import java.lang.Exception.*;
import java.util.*;

public class Test {
    public static void main(String args[]) {
        try{
            Class<?> classTestee = Class.forName(args[0]);
        
		
        Method[] methodes = classTestee.getMethods();
        for (Method methode : methodes){
            if(methode.getAnnotation(Write.class)!=null){
                String typeReturn = methode.getReturnType().getName();
                Class<?>[] paramTypes = methode.getParameterTypes();
                String paramTypesString = "";
                for (Class<?> paramType : paramTypes){
                    paramTypesString+=paramType.getName();
                }

                String name = methode.getName();

                System.out.println("Methode : "+name+" return type "+typeReturn+" params types "+paramTypesString);

            }

        }
        } catch(Exception e){
            System.out.println("Error");
        }
	}
}