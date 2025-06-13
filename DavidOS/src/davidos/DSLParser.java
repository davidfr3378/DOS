package davidos;

import java.util.Scanner;
import java.util.Stack;
import java.io.*;

/*
 * @author david.ez
 */

public class DSLParser {
    String subject;

    DSLParser(String subject){
        this.subject = subject;

    }

//Add a funcion to control the output of each rather than have the return stuff
    
//Basic method to parse stuff
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

//Gets the command at head
public static String getCommand(String input) {
        
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
        
        return command;
    }

//Gets the text with head commmand removed. 
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

/*
 * Main parser method and auxilary functions STARTS
 */
private static String parser(String input){
    Stack<String> commands = new Stack<String>();
    int last_cmd_index = 0, command_count = 0;
    String token = "", token_output = "";
     //Iterate over input and splitting it by the delimiter, "(" ")"
     for(String Token : input.split(" ")){
        //if token is a command,
        if(isCommand(Token)){
            //push to the stack commands
            commands.push(Token);
            //and update last_command to its index in input. 
            last_cmd_index = input.indexOf(Token);
            token = Token;
            command_count++;
        }
     }
     //get text after last token
     String after_token = input.substring(last_cmd_index + token.length()).trim();
    //plug text into last command
    token_output = DSLInterpreter.detect(token, after_token);
    //System.out.println(token_output);
    //If therer is more than one token
    if(command_count > 1){
        //System.out.println("Second time" + "Token output:" + token_output + "e");
        //gets the string before the token 
        String before_token = input.substring(0, last_cmd_index).trim();
        
        //Calls the function on the string before the token + the result of any computation
        //Note that this is highly problematic if the second function is not cp
        parser(before_token+token_output);
    }
    return token_output;
}

//Checks if a given input is a recognized command
private static boolean isCommand(String input){
    boolean isCommand = false;
    String[] commands = {"cp", "st", "var", "check", "let", "clear", "parse", 
                            "set", "graph", "help", "quit"};
    //checks if input is a command
    for(int i = 0; i < commands.length; i++){
        if(input.equals(commands[i])){
            isCommand = true;
        }
    }
    return isCommand;
}

//Manages output from parser. Potentially not needed
public static void parserManager(String input){
    String output = parser(input);
    //System.out.println(output);
}

/*
 * Main parser method and auxilary functions ENDS
 */

}
