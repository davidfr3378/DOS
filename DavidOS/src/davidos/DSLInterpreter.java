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
    private static void detect(String token){
        //Utilizes and enhanced switch
        switch(token){
        
            case "cp" -> System.out.println("Computing");
            case "st" -> System.out.println("Storing");
        
            default -> System.out.println(token + " is not a command");
        }
    
    }
    
    
    //Simple Computation
    private static double compute(Scanner input){
        double sum = 0;
        //While Scanner still has a token
        while(input.hasNextInt()){
            //Set int to token
            int next = input.nextInt();
            //Cumulative Sum
            sum += next;
        }
    return sum; 
    } 
    
}
