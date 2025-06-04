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

//Exceptions
import java.io.IOException;


/*
 * @author david.ez
 */

/*TO-DO:
1. Read docs for a couple things
2. Add means to delete files when program ends, so theres no duplicates
3. Heavily streamline st. Only minor stuff for now, if time, make it more beautiful.
*/
public class DSLInterpreter {
    static Scanner scanner = new Scanner(System.in);
    static boolean file_created = false;

    
    DSLInterpreter(){
        
    }
    
    /*
    Used to interpret the comand token and call the corresponding method.
    */
    static void detect(String command, String subject){
        //Utilizes and enhanced switch
        switch(command){
        
            case "cp" -> System.out.println(compute(subject,0));
            case "st" -> store(subject);
        
            default -> System.out.println(command + " is not a command");
        }
    
    }
    
    
    //Simple Computation
    private static int compute(String input, int index){
     //operand.push(""); 
     //operand.pop();
     //operand.peek();
     //Creating Stacks for operator and operand.
     Stack<Character> operand = new Stack<Character>();
     Stack<Character> operator = new Stack<Character>();
     int new_operand = 0;

     //Get a token from the string
     //Note that I'm converting it to a wrapper object so I can compare. Freaking comparison nonsense. 
     for (int i = index; i < input.length(); i++) {
         if(Character.isDigit(input.charAt(i))){
             operand.push(input.charAt(i));
         }else if(!Character.isDigit(input.charAt(i))){
             operator.push(input.charAt(i));
             //Order swapped because stack pops in reverse order i.2 12- = 1 
             char op2 = operand.pop();
             char op1 = operand.pop();
             
             //Pops the current operator, ready for the switch
             Character x = operator.pop();
             
             switch(x){
             
                 case '+' -> new_operand = add(op1,op2);
                 case '-' -> new_operand = subtract(op1,op2);
                 case '*' -> new_operand = multiply(op1,op2);
                 case '/' -> new_operand = divide(op1,op2);
                 case '%' -> new_operand = modulo(op1,op2);
                 default -> System.out.println(x + " is not an operator");
             }
             char new_operand_char = Character.forDigit(new_operand,new_operand+1);
             //System.out.println(Character.toString(new_operand_char)); //for test
             operand.push(new_operand_char);
             
     
         }
         
        }
        //NOTE: IF LESS BUSY, ADD SOMETHING THAT RETURNS ORIGINAL VALUE IF NO OPERATORS. CURRENTLY RETURNS 0
        int answer = new_operand;
//    
//     while((a.equals("")) == false){
//         if(Character.isDigit(input.charAt(index))){
//             operand.push(input.charAt(index));
//         }else if(!Character.isDigit(input.charAt(index))){
//             operator.push(input.charAt(index));
//         }
//     }
        //System.out.println(operand);
        //System.out.println(operator);
        
    return answer; 
    }
    
    //add function for the compute command
    private static int add(char a, char b){
        int op1 = 0;
        int op2 = 0;
        
        op1 = Character.getNumericValue(a);
        op2 = Character.getNumericValue(b);
        
        return op1+op2;
    }
    
    private static int subtract(char a, char b){
        int op1 = 0;
        int op2 = 0;
        
        op1 = Character.getNumericValue(a);
        op2 = Character.getNumericValue(b);
        
        return op1-op2;
    }
    
    private static int multiply(char a, char b){
        int op1 = 0;
        int op2 = 0;
        
        op1 = Character.getNumericValue(a);
        op2 = Character.getNumericValue(b);
        
        return op1*op2;
    }
    
    private static int divide(char a, char b){
        int op1 = 0;
        int op2 = 0;
        
        op1 = Character.getNumericValue(a);
        op2 = Character.getNumericValue(b);
        
        return op1/op2;
    }
    
    private static int modulo(char a, char b){
        int op1 = 0;
        int op2 = 0;
        
        op1 = Character.getNumericValue(a);
        op2 = Character.getNumericValue(b);
        
        return op1%op2;
    }
    
    //Need to add a function to check if a variable exists, writing stuff and also for retrieving them.
    private static void store(String subject){
        String var_name = parseVarName(subject);
        int var = parseVariable(subject);
        createFile();
        if(checkVar(var_name)){
            System.out.println("Already exists");
        }else{
            writeFile(var_name, var); 
        }
        
    }
    
    private static void createFile() {
        //For now, keep the second else, but when streamlining, remove. Remember that .createNewFile() already checks for existence.
        if(!file_created){
            try {
                File variables = new File("variables.txt");
                if (variables.createNewFile()) {
                    System.out.println("File created: " + variables.getName());
                }else{
                    System.out.println("File already exists.");
                }
            }catch(IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }else{
            System.out.println("File already created");
        }
    }
    
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
    
    private static void writeFile(String var_name, int var){
        try{
            //CHECK OUT THE DOCS FOR FILEWRITER: TRUE????
            FileWriter myWriter = new FileWriter("variables.txt", true);
            myWriter.write(var_name + " = " + var + "\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        }catch(IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    private static String parseVarName(String subject){
        
        int index = 0;
        String temp_var = "";
        String varName = "";
        //create a queue
        //add = enqueue, offer()
        //remove = dequeue, poll()
        // peek()
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
        System.out.println("Important: Should be x if variable name is x: " + variable);
        
        //only adds one number. 
        varName += variable.remove();
        System.out.println("Actual variabl name being passed out: " + varName);
        return varName;
    }
    
    private static int parseVariable(String subject){
        int index = 0; //string loop
        String temp_var = ""; 
        int var = 0; //final value
        //create a queue
        //add = enqueue, offer()
        //remove = dequeue, poll()
        // peek()
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
        System.out.println("Actual integer being passed out: " + var);
        return var;
    }
    
}
