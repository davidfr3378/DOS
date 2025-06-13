package davidos;

import java.util.HashMap;
import java.util.Stack;

public class Computer {
    //static private HashMap<String, Double> variables = new HashMap<String, Double>();
    static String[] binary_operators = {"+", "-", "/", "*", "^", "%"};
    static String[] functions = {"!", "sin", "cos", "tan", "log", "ln", "sqrt", "abs", "asin", "acos", "atan"};


    Computer(){

    }

    //Arithmetic expression parser
    public double compute(String input, HashMap <String, Double> variables){
     //Creating Stacks for operator and operand.
     Stack<Double> operands = new Stack<Double>();
     Stack<String> operators = new Stack<String>();
     
     //Create array for tokens 
     String[] tokens = input.split("\\s+");

     //creates new operand
     int new_operand = 0;


     for (int i = 0; i < tokens.length; i++) {
         if(isNumber(tokens[i])){
            //Convert number to string
            double number = stringToNumber(tokens[i]);
            //Load into operand stack 
            operands.push(number);

         }else if(isVariable(tokens[i], variables)){
            //Load into operand stack
            operands.push(variables.get(tokens[i]));
            
         }else if(isOperator(tokens[i])){
            String operator = tokens[i];

            //Load into operator stack
            //operators.push(operator);

            //OPERATE
            double b = operands.pop();
            double a = operands.pop();
            double result = binaryOperator(operator, b, a);

            //Load result to operands
            operands.push(result);
            //continue
         }else if(isFuntion(tokens[i])){ //Remember to add factorial
            //Pop operand stack
            double operand = operands.pop();
            //Get Funtion
            String operator = tokens[i];
            //OPERATE
            double result = unaryOperator(operator, operand);
            System.out.println("result:" + result);
            //Load result to operands
            operands.push(result);

            //continue
        }
        //NOTE: IF LESS BUSY, ADD SOMETHING THAT RETURNS ORIGINAL VALUE IF NO OPERATORS. CURRENTLY RETURNS 0
        
    }
        double answer = operands.pop();
        
    return answer; 
    }
    //Checks
    //Checks if a token has numerical value
    private static boolean isNumber(String token){
        try {
            Double.parseDouble(token);
            return true;
        }catch(NumberFormatException e){
            return false;
        }

    }
    //Checks if a token is a recognised variable
    private static boolean isVariable(String token, HashMap <String, Double> variables){
        //Checks if its a key in token
        if(variables.get(token) != null){
            return true;
        }
        
        return false;
    }
    //Checks if a token is an operator
    private static boolean isOperator(String token){
        for(int i = 0; i < binary_operators.length; i++){
            if(token.equals(binary_operators[i])){
                return true;
            }
        }

        return false;
    }
    //Checls if a token is a recognised function
    private static boolean isFuntion(String token){
        for(int i = 0; i < functions.length; i++){
            if(token.equals(functions[i])){
                return true;
            }
        }

        return false;
    }
    
    
    //Conversions
    //Turns a string into a number
    private static double stringToNumber(String token){
        double new_number = 0.0;
        try{
            new_number = Double.parseDouble(token);
        }catch(NumberFormatException e){
            System.out.println("ERROR: NaN && aN");
        }
        return new_number;
    }


    //Computators
    //Adds two numbers together and returns result
    private static double add(double a, double b){
        double c = a + b;

        return c;
    }
    //Factorial
    private static int factorial(int operand){
        if (operand < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        if (operand == 0 || operand == 1) {
            return 1;
        }
        return operand * factorial(operand - 1);
    }

    //Dispatchers
    //Binary Operations
    private static double binaryOperator(String operator, double a, double b){
        switch(operator){
            case "+" : return a+b;
            case "-" : return a-b;
            case "*" : return a*b;
            case "/" : return a/b;
            case "%" : return a%b;
            case "^" : return Math.pow(a, b);

            default : System.out.println("ERROR: BINARY OPERATOR DOES NOT EXIST");
            
        }

        return -1;
    }
    //Unary Operations
    private static double unaryOperator(String operator, double operand){
        switch(operator){
            case "!" : return factorial((int) operand);
            case "sin" : return Math.sin(Math.toRadians(operand));
            case "cos" : return Math.cos(Math.toRadians(operand));
            case "tan" : return Math.tan(Math.toRadians(operand));
            case "log" : return Math.log10(operand);
            case "ln" : return Math.log(operand);
            case "sqrt" : return Math.sqrt(operand);
            case "abs" : return Math.abs(operand);
            case "asin" : return Math.asin(Math.toRadians(operand));
            case "acos" : return Math.acos(Math.toRadians(operand));
            case "atan" : return Math.atan(Math.toRadians(operand));

            default : System.out.println("ERROR: BINARY OPERATOR DOES NOT EXIST");
            
        }

        return -1;

    }
}
