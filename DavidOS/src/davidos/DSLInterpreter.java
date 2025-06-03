package davidos;

import java.util.Scanner;
import java.util.Stack;
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
        
            case "cp" -> System.out.println(compute(subject,0));
            case "st" -> System.out.println("Storing");
        
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
    
}
