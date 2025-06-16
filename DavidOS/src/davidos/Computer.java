package davidos;

import java.util.HashMap;
import java.util.Stack;

public class Computer {
    //static private HashMap<String, Double> variables = new HashMap<String, Double>();
    static String[] binary_operators = {"+", "-", "/", "*", "^", "%"};
    static String[] functions = {"!", "sin", "cos", "tan", "log", "ln", "sqrt", "abs", "asin", "acos", "atan"};
    static HashMap<String, Double> variables = new HashMap<String, Double>();

    Computer(){

    }

    //Arithmetic expression parser
    public double compute(String input){
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
            //System.out.println("Being pushed to operands:" + variables.get(tokens[i]) + "e");
            if(variables.get(tokens[i])% 1 == 0){
                operands.push(variables.get(tokens[i]));
            }else{
                operands.push(variables.get(tokens[i]));
            }
            
            
         }else if(isOperator(tokens[i])){
            String operator = tokens[i];

            //Load into operator stack
            //operators.push(operator);

            //OPERATE
            double b = operands.pop();
            double a = operands.pop();
            double result = binaryOperator(operator, a, b);

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
            //System.out.println("result:" + result);
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
    //Made public for parseVar in DSLInterpreter.
    public static boolean isNumber(String token){
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
    //Made public for ParseVar in DSLInterpreter. 
    public static double stringToNumber(String token){
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
            //They output radians. I need then to not. 
            case "asin" : return Math.toDegrees(Math.asin(operand));
            case "acos" : return Math.toDegrees(Math.acos(operand));
            case "atan" : return Math.toDegrees(Math.atan(operand));

            default : System.out.println("ERROR: BINARY OPERATOR DOES NOT EXIST");
            
        }

        return -1;

    }

    //I'm tired bro...
    //made public for detect in DSLInterpreter
    public static String let(String subject) {
        //System.out.println("Subject:" + subject + "e");
        //Hashmap used was made public so as to have the ability to clear it from quit()
        //Hashmap stuff:
        // hashmap.put("var_name",var); //hashmap.get("var_name"); //hashmap.remove("var_name") //hashmap.clear();
        String var_name = DSLInterpreter.parseVarName(subject);
        //System.out.println("Subject:" + var_name + "e");
        double var = DSLInterpreter.parseVariable(subject);
        //System.out.println("Subject:" + var + "e");
        //System.out.println("Var after parseVar:" + var + "e");
        variables.put(var_name, var);

        System.out.println("[+]");

        return "[+]";
    }
    //made public and moved to computer because sharing variable hashmap in DSLInterpreter
    public static String getVar(String subject){
        double value = variables.get(subject);
        String output = "";
        System.out.println(subject + " = " + value);
        output += subject + " = " + variables.get(subject);
        return output;
    }
    //made public and moved to computer because sharing variable hashmap in DSLInterpreter
    //clears a variable in hashmap
    public static void clear(String subject) {
            variables.remove(subject);
            System.out.println("[+]");
    }

    //Clears all in hashmap
    public static void clearAll(){
        variables.clear();
    }

}
