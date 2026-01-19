package davidos;

import java.io.*;
import java.util.*;

/**
 * Reworked Computer: tokenizer + shunting-yard -> RPN evaluator
 * - Supports numbers (decimal), variables, parentheses, functions, and operators
 * - Functions: ! (factorial as postfix), sin, cos, tan, log, ln, sqrt, abs, asin, acos, atan
 * - Operators: + - * / % ^ (with precedences & associativity)
 * - Unary minus handled as "neg"
 */
public class Computer {
    static HashMap<String, Double> variables = new HashMap<>();

    // Operator metadata
    private static final Set<String> FUNCTIONS = new HashSet<>(Arrays.asList(
        "sin", "cos", "tan", "log", "ln", "sqrt", "abs", "asin", "acos", "atan", "neg"
    ));

    private static final Set<String> BINARY_OPERATORS = new HashSet<>(Arrays.asList(
        "+", "-", "*", "/", "^", "%"
    ));

    // Precedence: higher number -> higher precedence
    private static final Map<String, Integer> PRECEDENCE = new HashMap<>();
    private static final Map<String, String> ASSOCIATIVITY = new HashMap<>();

    static {
        PRECEDENCE.put("!", 6);   // postfix factorial
        PRECEDENCE.put("neg", 6); // unary negation as function-like
        PRECEDENCE.put("^", 5);
        PRECEDENCE.put("*", 4);
        PRECEDENCE.put("/", 4);
        PRECEDENCE.put("%", 4);
        PRECEDENCE.put("+", 3);
        PRECEDENCE.put("-", 3);

        ASSOCIATIVITY.put("^", "right");
        ASSOCIATIVITY.put("*", "left");
        ASSOCIATIVITY.put("/", "left");
        ASSOCIATIVITY.put("%", "left");
        ASSOCIATIVITY.put("+", "left");
        ASSOCIATIVITY.put("-", "left");
    }

    public Computer() { }

    // Primary compute entry: accepts expressions with or without spaces
    public double compute(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }

        List<String> tokens = tokenize(input);
        List<String> rpn = shuntingYard(tokens);
        return evaluateRPN(rpn);
    }

    // ---------- Tokenizer ----------
    private List<String> tokenize(String s) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        s = s.trim();
        while (i < s.length()) {
            char c = s.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
            // Number (with decimal)
            if (Character.isDigit(c) || (c == '.' && i + 1 < s.length() && Character.isDigit(s.charAt(i+1)))) {
                int j = i+1;
                while (j < s.length() && (Character.isDigit(s.charAt(j)) || s.charAt(j) == '.')) j++;
                tokens.add(s.substring(i, j));
                i = j;
                continue;
            }
            // Identifier (function or variable)
            if (Character.isLetter(c) || c == '_') {
                int j = i+1;
                while (j < s.length() && (Character.isLetterOrDigit(s.charAt(j)) || s.charAt(j) == '_')) j++;
                tokens.add(s.substring(i, j));
                i = j;
                continue;
            }
            // Operators and parentheses and factorial
            if ("+-*/^()%!,=".indexOf(c) >= 0) {
                // handle two-character operators if you ever add them
                tokens.add(String.valueOf(c));
                i++;
                continue;
            }
            // Unknown char
            throw new IllegalArgumentException("Unknown token character: " + c);
        }
        // Convert unary minus to 'neg' where appropriate (start or after operator or '(')
        List<String> normalized = new ArrayList<>();
        for (int k = 0; k < tokens.size(); k++) {
            String t = tokens.get(k);
            if (t.equals("-")) {
                if (k == 0) {
                    normalized.add("neg");
                    continue;
                }
                String prev = tokens.get(k - 1);
                if (prev.equals("(") || BINARY_OPERATORS.contains(prev) || prev.equals(",") || prev.equals("=")) {
                    normalized.add("neg");
                    continue;
                }
            }
            normalized.add(t);
        }

        return normalized;
    }

    // ---------- Shunting-yard (to RPN) ----------
    private List<String> shuntingYard(List<String> tokens) {
        List<String> output = new ArrayList<>();
        Deque<String> opStack = new ArrayDeque<>();

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);

            if (isNumber(token)) {
                output.add(token);
            } else if (isVariable(token)) {
                output.add(token);
            } else if (FUNCTIONS.contains(token)) {
                opStack.push(token);
            } else if (token.equals(",")) {
                // function arg separator: pop until '('
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
                    output.add(opStack.pop());
                }
                if (opStack.isEmpty()) throw new IllegalArgumentException("Misplaced comma or mismatched parentheses");
            } else if (BINARY_OPERATORS.contains(token) || token.equals("!") || token.equals("neg")) {
                String op1 = token;
                while (!opStack.isEmpty()) {
                    String op2 = opStack.peek();
                    if (BINARY_OPERATORS.contains(op2) || FUNCTIONS.contains(op2) || op2.equals("!") || op2.equals("neg")) {
                        int p1 = PRECEDENCE.getOrDefault(op1, 0);
                        int p2 = PRECEDENCE.getOrDefault(op2, 0);
                        String assoc1 = ASSOCIATIVITY.getOrDefault(op1, "left");
                        if ((assoc1.equals("left") && p1 <= p2) || (assoc1.equals("right") && p1 < p2)) {
                            output.add(opStack.pop());
                            continue;
                        }
                    }
                    break;
                }
                opStack.push(op1);
            } else if (token.equals("(")) {
                opStack.push(token);
            } else if (token.equals(")")) {
                while (!opStack.isEmpty() && !opStack.peek().equals("(")) {
                    output.add(opStack.pop());
                }
                if (opStack.isEmpty()) {
                    throw new IllegalArgumentException("Mismatched parentheses");
                }
                opStack.pop(); // pop "("
                // if top is a function, pop it onto output
                if (!opStack.isEmpty() && FUNCTIONS.contains(opStack.peek())) {
                    output.add(opStack.pop());
                }
            } else {
                throw new IllegalArgumentException("Unknown token in shunting-yard: " + token);
            }
        }

        while (!opStack.isEmpty()) {
            String t = opStack.pop();
            if (t.equals("(") || t.equals(")")) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.add(t);
        }

        return output;
    }

    // ---------- RPN evaluator ----------
    private double evaluateRPN(List<String> rpn) {
        Deque<Double> stack = new ArrayDeque<>();
        for (String token : rpn) {
            if (isNumber(token)) {
                stack.push(stringToNumber(token));
            } else if (isVariable(token)) {
                Double v = variables.get(token);
                if (v == null) throw new IllegalArgumentException("Unknown variable: " + token);
                stack.push(v);
            } else if (FUNCTIONS.contains(token)) {
                if (stack.isEmpty()) throw new IllegalArgumentException("Insufficient operands for function " + token);
                double a = stack.pop();
                double res = unaryOperator(token, a);
                stack.push(res);
            } else if (token.equals("!")) {
                if (stack.isEmpty()) throw new IllegalArgumentException("Insufficient operands for factorial");
                double a = stack.pop();
                stack.push((double) factorial((int) a));
            } else if (token.equals("neg")) {
                if (stack.isEmpty()) throw new IllegalArgumentException("Insufficient operands for unary minus");
                double a = stack.pop();
                stack.push(-a);
            } else if (BINARY_OPERATORS.contains(token)) {
                if (stack.size() < 2) throw new IllegalArgumentException("Insufficient operands for operator " + token);
                double b = stack.pop();
                double a = stack.pop();
                stack.push(binaryOperator(token, a, b));
            } else {
                throw new IllegalArgumentException("Unknown token in RPN evaluation: " + token);
            }
        }
        if (stack.size() != 1) throw new IllegalArgumentException("Malformed expression (stack size != 1)");
        return stack.pop();
    }

    // ---------- Utilities ----------
    public static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isVariable(String token) {
        if (token == null || token.isEmpty()) return false;
        if (isNumber(token)) return false;
        // Not an operator, not parentheses, not function name -> treat as variable name
        if (FUNCTIONS.contains(token)) return false;
        if (BINARY_OPERATORS.contains(token) || token.equals("(") || token.equals(")") || token.equals(",") || token.equals("!") || token.equals("neg"))
            return false;
        // variable naming rules: starts with letter or underscore
        return Character.isLetter(token.charAt(0)) || token.charAt(0) == '_';
    }

    public static double stringToNumber(String token) {
        try {
            return Double.parseDouble(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number: " + token);
        }
    }

    // ---------- Operators ----------
    private static double binaryOperator(String operator, double a, double b) {
        switch (operator) {
            case "+" : return a + b;
            case "-" : return a - b;
            case "*" : return a * b;
            case "/" : 
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            case "%" : return a % b;
            case "^" : return Math.pow(a, b);
            default:
                throw new IllegalArgumentException("Unknown binary operator: " + operator);
        }
    }

    private static int factorial(int operand) {
        if (operand < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers.");
        }
        if (operand == 0 || operand == 1) {
            return 1;
        }
        return operand * factorial(operand - 1);
    }

    private static double unaryOperator(String operator, double operand) {
        switch (operator) {
            case "sin": return Math.sin(Math.toRadians(operand));
            case "cos": return Math.cos(Math.toRadians(operand));
            case "tan": return Math.tan(Math.toRadians(operand));
            case "log": return Math.log10(operand);
            case "ln" : return Math.log(operand);
            case "sqrt": return Math.sqrt(operand);
            case "abs": return Math.abs(operand);
            case "asin": return Math.toDegrees(Math.asin(operand));
            case "acos": return Math.toDegrees(Math.acos(operand));
            case "atan": return Math.toDegrees(Math.atan(operand));
            default:
                throw new IllegalArgumentException("Unknown function: " + operator);
        }
    }

    // ---------- Variable helpers used by DSLInterpreter ----------
    public static String let(String subject) {
        String varName = DSLInterpreter.parseVarName(subject);
        double value = DSLInterpreter.parseVariable(subject);
        variables.put(varName, value);
        // persist variable via DSLInterpreter writeFile
        DSLInterpreter.writeOrUpdateVarFile(varName, value);
        System.out.println("[+] " + varName + " = " + value);
        return "[+]";
    }

    public static String getVar(String subject) {
        Double value = variables.get(subject);
        if (value == null) {
            String out = subject + " is undefined";
            System.out.println(out);
            return out;
        }
        String output = subject + " = " + value;
        System.out.println(output);
        return output;
    }

    public static void clear(String subject) {
        variables.remove(subject);
        DSLInterpreter.removeVarFromFile(subject);
        System.out.println("[+]");
    }

    public static void clearAll() {
        variables.clear();
        DSLInterpreter.clearVarsFile();
    }
}
