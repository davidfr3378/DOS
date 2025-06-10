package davidos;

import java.util.Scanner;
/*
 * @author david.ez
 */

public class DSLParser {
    String subject;

    DSLParser(String subject){
        this.subject = subject;

    }

//Add a funcion to control the output of each rather than have the return stuff
    private static void basicParser(String input){
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
            if(input.charAt(0) == '/'){
             System.out.println("\"/\" detected!" );
             break;
            }else if(command_token_index == 1){
                command += token;
            }else{
                //I don't need anything else
            }
            
        }
        parser.close();
        
    }

    
    public static String tokenize(String input){
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
        parser.close();

        return final_token;
    }

}
