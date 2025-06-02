package davidos;

import java.util.Scanner;
/*
 * @author wilma
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
            
            //Get command token and the rest of input
            String command = getCommand(input);
            String tokenised_input = tokenize(input);
            
            
            System.out.println("\n Command: " + command);
            System.out.println("\n COntent: " + tokenised_input);
            
            interpreter.detect(command, tokenised_input);
            //int count = outputTokens(input);
            //System.out.println("\nCount of tokens: " + count);

            //parse(input);
            
        }
        
    }
    
    
    private static String getInput() {
        String input = scanner.nextLine();
        
        return input;
    }
    
    private static String getCommand(String input) {
        
        //Initialize Final Token string. 
        String command = "";
        
        //create a parser object
        Scanner parser = new Scanner(input);
        //while the parser object has tokens left in it
        int command_token_index = 2;
        
        while(parser.hasNext()){
            //get the token
            String token = parser.next();
            command_token_index--;
            
            //Try to interpret
            if(command_token_index == 1){
                command += token;
            }else{
                //I don't need anything else
            }
            
        }
        
        return command;
    }
    
    private static String tokenize(String input){
        //Initialize Final Token string. 
        String final_token = "";
        
        //create a parser object
        Scanner parser = new Scanner(input);
        //while the parser object has tokens left in it
        int command_token_index = 2;
        
        while(parser.hasNext()){
            //get the token
            String token = parser.next();
            command_token_index--;
            
            //Try to interpret
            if(command_token_index == 1){
                //System.out.println("Command toekn index that should be 1: " + command_token_index);
               // This sysout above only exists for later tests. 
            }else{
                //If not a command token (i.e. not the first token), add to a string called final token
                final_token += token;
                //final_token += " ";
            }
            
        }       

        return final_token;
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