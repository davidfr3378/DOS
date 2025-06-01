package davidos;

import java.util.Scanner;
/*
 * @author wilma
 */

public class Shell {
    
    //DSLInterpreter interpreter = new DSLInterpreter();
    
    static Scanner scanner = new Scanner(System.in);
    static String input = "";

    public Shell(){
        
    }

    static void shell() {
        while(!input.equalsIgnoreCase("quit")){
            System.out.print("> ");
            input = getInput();
            int count = outputTokens(input);
            System.out.println("\nCount of tokens: " + count);

            //parse(input);
            
        }
        
    }
    
    private static String getInput() {
        String input = scanner.nextLine();
        
        return input;
    }
    
    private static int outputTokens(String input) {
        System.out.println("This is the input: \"" + input + "\"");
        
        
        //create a parser object
        Scanner parser = new Scanner(input);
        //while the parser object has tokens left in it
        int count = 0;
        while(parser.hasNext()){
            //get the token
            String token = parser.next();
            //print the token
            System.out.println("Token #" + count + " is: " + token);
            //keep track of the count of tokens
            count++;
        }        
            return count;
    }
    
    private static void parse(String input) {
        System.out.println("Parsed");
        
        
    }
    
}


/* Saved for later:

while(!input.equalsIgnoreCase("quit")){
    outputTokens(input);
    input = getInput();
}



*/