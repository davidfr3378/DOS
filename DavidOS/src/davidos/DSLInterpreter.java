package davidos;

import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;
import java.util.HashMap;
//Exceptions
import java.io.IOException;

/*
 * @author david.ez
 */

/* 
Goals: [✓] [X] [-]
1. Re-structure the computing algorithm to include basic algebra{1. Involve Brackets[]; 2. Involve variables [✓]; 3. Add: Exponents, square roots []; REACH: Add: log, sin {and the others}}
2. Add graphing ability using JFreeChart {Seperate file. 1. Ability to plot linear and quadratic functions[]; Ability to plot with a specified domain []; Ability to plot sinusodial equations []}
3. Math Errors, pretty output {1. Ensure problems like attempting to divide by 0 are not easily possible []; 2. Make all output to the user concise and consistent[].}
4. Add a file parser {1. Parse through a file as though they were regular commands from the user [✓]; 2. Able to write comments in files [✓]; 3. Able to set a variable to the finished product of a computation (for regular CLI commands as well)[]}
5. Add a basic terminal like UI {1. UI for main program []; 2. UI for graphing []}


/ Tight goals:
1. Add let functionality for computer or move let over completely.
2. Finish and merge changes before end of friday
*/


public class DSLInterpreter {
    static Shell shell = new Shell();
    static Computer computer = new Computer();
    
    static Scanner scanner = new Scanner(System.in);
    static boolean file_created = false;

    
    DSLInterpreter(){
        
    }
    
    /*
    Used to interpret the comand token and call the corresponding method.
    */
    static String detect(String command, String subject){
        String result = "";
        //Utilizes and enhanced switch
        int count = 1;
        // command isn't empty (its only empty if a ("/") is detected as the start of the line
        while(!command.equals("") && count == 1){
            switch(command){

                case "cp" -> result += computer.compute(subject);
                case "st" -> store(subject);
                case "var" -> result += computer.getVar(subject);
                case "check" -> System.out.printf("%s exists: %b" , subject, checkVar(subject));
                case "let"  -> result += computer.let(subject);
                case "clear" -> computer.clear(subject);
                case "parse" -> parseFile(subject);
                case "set" -> set(subject); 
                case "graph" -> graph(subject); //to be removed temporarily
                case "help" -> help(subject); //not yet functional
                case "quit" -> quit();
                default -> System.out.println(command + " is not a command");
            }
            count--;

            if(result.equals("")){
                //pass
            }
        }
        return result;
    }   
   
    //Need to add a function to check if a variable exists,
    private static void store(String subject){
        String var_name = parseVarName(subject);
        double var = parseVariable(subject);
        createFile();
        if(checkVar(var_name)){
            System.out.println("Already exists");
        }else{
            writeFile(var_name, var); 
        }
        
    }
    //creates a file if it doesn't already exist; doesn't take parameters though, you'll need to overload to print a specific file
    private static void createFile() {
        //create new file checks for existence first
        if(!file_created){
            try {
                File variables = new File("variables.txt");
                if (variables.createNewFile()) {
                    //System.out.println("File created: " + variables.getName());
                }else{
                    //System.out.println("File already exists.");  
                }
            }catch(IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
    //check if a var exists in a file; need to overload or change for a file other than variable.
    private static boolean checkVar(String var_name) {
        try (BufferedReader reader = new BufferedReader(new FileReader("variables.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(var_name)) {
                    return true; // Text found
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return false;
    }
    //writes to a file; DO NOT USE FOR ANYTHING NOT VARIABLE.TXT
    private static void writeFile(String var_name, double var){
        try{
            //CHECK OUT THE DOCS FOR FILEWRITER: TRUE????
            FileWriter myWriter = new FileWriter("variables.txt", true);
            myWriter.write(var_name + " = " + var + "\n");
            myWriter.close();
            //System.out.println("Successfully wrote to the file.");
        }catch(IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    //stores a  variable in a hashmap
 
    //runs through a string and collects all non-numbers
    //made public for let in computer
    public static String parseVarName(String subject){
        int index = 0;
        String temp_var = "";
        String varName = "";
        //create a queue
        //add = enqueue, offer() //remove = dequeue, poll() // peek()
        Queue<Character> variable = new LinkedList<Character>();
        
        //this checks each char of a string for some quality.
        //for index n in subject
        for (int i = index; i < subject.length(); i++) {
            //if n is number, variable:
         if(!Character.isDigit(subject.charAt(i))){
             //offer(num)
             variable.offer(subject.charAt(i));
         //else 
            //skip
         }else if(Character.isDigit(subject.charAt(i))){
             //pass
         }
         
        }
        //System.out.println("Important: Should be x if variable name is x: " + variable);
        
        //only adds one number. 
        varName += variable.remove();
        //System.out.println("Actual variabl name being passed out: " + varName);
        return varName;
        
    }
    //runs through a string and collects all numbers
    //made public for let in computer
    public static double parseVariable(String subject){
        //Remove first character
        if (subject == null || subject.length() <= 1) {
            // return empty string if input is null or only 1 character
        }
        subject = subject.substring(1);
        double result = 0.0;
        //System.out.println("Subjectee:" + subject + "e");
        for(String Token : subject.split("\\s+")){
            //if number
            if(Computer.isNumber(Token)){
                 result = Computer.stringToNumber(Token);
  
            }else{
                //pass
            }
        }

        /* 
        Former code for this (made useless because it wouldn't work with doubles) 
        System.out.println("Into parVar:" + subject + "e");
        int index = 0; //string loop
        String temp_var = ""; 
        int var = 0; //final value
        //create a queue
        //add = enqueue, offer() //remove = dequeue, poll() // peek()
        Queue<Character> variable = new LinkedList<Character>();
        
        //this checks each char of a string for some quality.
        //for index n in subject
        for (int i = index; i < subject.length(); i++) {
            //if n is number, variable:
         if(Character.isDigit(subject.charAt(i))){
             //offer(num)
             variable.offer(subject.charAt(i));
         //else 
            //skip
         }else if(!Character.isDigit(subject.charAt(i))){
             //pass
         }
         
        }
        //pop elements of variable into a string
       Iterator<Character> studentQueueIterator = variable.iterator();
      
          // Iterating Queue
          while (studentQueueIterator.hasNext()) {
            char name = studentQueueIterator.next();
            temp_var += name;
        }
        //get the integerparse of that string
        var += Integer.parseInt(temp_var);
        //System.out.println("Actual integer being passed out: " + var);

        return var;
        */
        return result;
    }
    
    //checks through a file and if a given char exists, collects it. DO NOT USE FOR ANYTHING NOT VARIABLE.TXT
    private static int readFile(String var_name){
        String filePath = "variables.txt";
        String strVar = "";
        int intVar = 0;
        String strVarname = "";
        
        
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String temp_line;
                while ((temp_line = reader.readLine()) != null) {
                    //System.out.println("current line says: " + temp_line);
                    strVarname = parseVarName(temp_line);
                    if(strVarname.equals(var_name)){
                       // System.out.println("Pulled");
                        strVar += parseVariable(temp_line);
                        break;
                    }else{
                        //pass
                    }
                }
            } catch (IOException e) {
                System.out.println("File doesn't exist");
                //System.err.println("Error reading file: " + e.getMessage());
            }
            intVar = Integer.parseInt(strVar); //make a try catch for this: NumberFormatException
            //System.out.println("Whole line:" + temp_line + "\n"); //remove later
        
    return intVar;
    }
    
    //turns an int into a Character
    private static Character varValueCharacter(int x){
        Character c = '\0';
        if (x < 10){
            //'0' -> 48; (char)u --> 'u in char';
             c = (char) (x + '0');
            
        }else if(x > 10){
            c = (char) (x);
        }
        return c; 
    }
    //parse through a file and plug each line into detect()
    private static void parseFile(String filename){
        File file = new File(filename);
        String filePath =  file.getAbsolutePath();
        System.out.println(filePath);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        //String filePath = "C:\\Users\\cadls\\OneDrive\\Desktop\\David\\Programming\\Java\\Projects\\DOS v2\\DOS\\DavidOS\\"+filename;
        System.out.println("Filepath:" + filePath);
            //Create a bufferedReader
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String temp_line;
                //For each line, as long as not empty
                while ((temp_line = reader.readLine()) != null) {
                    //tokenise the line via getCommand() and tokenize in shell, then plug into detect
                    //System.out.println("Command: " + DSLParser.getCommand(temp_line) + " Token: " + DSLParser.tokenize(temp_line));

                    System.out.println(Shell.processInput(temp_line));   
                }
            } catch (IOException e) {
                System.out.println("File doesn't exist");
                System.err.println("Error reading file: " + e.getMessage());
            }
     
    }
    //gets the value of a variable

    //set a variable to the output of another command
    private static void set(String subject){
        System.out.println("Parser out of order");
    }
    //graphs a function within a certain domain
    private static void graph(String subject){
        System.out.println("Grapher not working. Subject is: " + subject);
    }
    
    //Prints man for all commands or specific
    private static void help(String command){
        System.out.println("Helping");
    }
    //what do you think this does?
    private static void quit(){
        System.out.println("Have a good day!");
        Computer.clearAll();
    }
}//Isn't working because varValue... cannot put a two digit number into a string, obviously; Can't even remember how it worked before. Possible solution involves changing the compute functions
