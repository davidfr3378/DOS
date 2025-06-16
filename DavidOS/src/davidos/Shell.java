package davidos;

import java.util.Scanner;
/*
 * @author david.ez
 */

public class Shell {
    
    static DSLInterpreter interpreter = new DSLInterpreter();
    static Scanner scanner = new Scanner(System.in);
    static String input = "";

    public Shell(){
        
    }

    static void shell() {
        while(!input.equalsIgnoreCase("quit")){
            System.out.print("> ");
            input = getInput();

            // //Get command token and the rest of input
            // String command = DSLParser.getCommand(input);
            // String tokenised_input = DSLParser.tokenize(input);
            
            // DSLInterpreter.detect(command, tokenised_input);
        }
        
    }
    
    
    private static String getInput() {
        String input = scanner.nextLine();
        
        return input;
    }
    
    public static String processInput(String input){  
        String output = null;
        output = DSLParser.parserManager(input);

        return output;
    }
}

/* Saved for later:

while(!input.equalsIgnoreCase("quit")){
    outputTokens(input);
    input = getInput();
}

*/
