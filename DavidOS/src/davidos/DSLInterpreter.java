package davidos;

import java.util.Scanner;
/*
 * @author wilma
 */

public class DSLInterpreter {
    static Scanner scanner = new Scanner(System.in);
    
    DSLInterpreter(){
        
    }
    
    /*
    Used to interpret the comand token and call the corresponding method.
    */
    static void detect(String command, String subject){
        //Utilizes and enhanced switch
        switch(command){
        
            case "cp" -> System.out.println(compute(subject));
            case "st" -> System.out.println("Storing");
        
            default -> System.out.println(command + " is not a command");
        }
    
    }
    
    
    //Simple Computation
    private static int compute(String input){
        int sum = 0;
        //While Scanner still has a token
        //System.out.println(input);
        Scanner comp = new Scanner(input);
        
        while(sum < 10){
            System.out.println("Exists");
            int next = comp.nextInt();
            sum += next;
        }
    return sum; 
    }
    
//    static void printType(int x) { System.out.println("int"); }
//    static void printType(String x) { System.out.println("String"); }
//    static void printType(char x) { System.out.println("char"); }
//    static void printType(boolean x) { System.out.println("boolean"); }
    
}
