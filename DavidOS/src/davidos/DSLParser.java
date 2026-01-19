import java.util.Scanner;
import java.util.Stack;
import java.util.Set;
import java.util.Arrays;
import java.util.HashSet;

public class DSLParser {
    String subject;

    // Set of valid commands for O(1) lookup speed
    private static final Set<String> COMMAND_LIST = new HashSet<>(Arrays.asList(
        "cp", "st", "var", "check", "let", "clear", "parse", "set", "graph", "help", "quit"
    ));

    DSLParser(String subject) {
        this.subject = subject;
    }

    private static void basicParser(String input) {
        // Now just calls the getter to keep logic in one place
        getCommand(input);
    }

    public static String getCommand(String input) {
        if (input == null || input.isEmpty()) return "";
        if (input.startsWith("/")) {
            System.out.println("\"/\" detected!");
            return "";
        }

        try (Scanner parser = new Scanner(input)) {
            if (parser.hasNext()) {
                // The original logic specifically targeted the first token 
                // based on the command_token_index logic.
                return parser.next();
            }
        }
        return "";
    }

    public static String tokenize(String input) {
        StringBuilder finalToken = new StringBuilder();
        try (Scanner parser = new Scanner(input)) {
            if (parser.hasNext()) {
                parser.next(); // Skip the head command
            }
            while (parser.hasNext()) {
                finalToken.append(parser.next());
            }
        }
        return finalToken.toString();
    }

    private static String parser(String input) {
        Stack<String> commands = new Stack<>();
        int lastCmdIndex = 0;
        int commandCount = 0;
        String lastTokenStr = "";

        String[] tokens = input.split(" ");

        // Identify commands and their positions
        for (String t : tokens) {
            if (isCommand(t)) {
                commands.push(t);
                lastCmdIndex = input.lastIndexOf(t); // More reliable than indexOf
                lastTokenStr = t;
                commandCount++;
            }
        }

        if (lastTokenStr.isEmpty()) return input;

        // Extract text after the last command
        String afterToken = input.substring(lastCmdIndex + lastTokenStr.length()).trim();
        
        // Pass to Interpreter (Keeping your reference)
        String tokenOutput = DSLInterpreter.detect(lastTokenStr, afterToken);

        // Recursive step: if nested commands exist
        if (commandCount > 1) {
            String beforeToken = input.substring(0, lastCmdIndex).trim();
            // Tail recursion to resolve the next command in the stack
            return parser(beforeToken + " " + tokenOutput);
        }

        return tokenOutput;
    }

    private static boolean isCommand(String input) {
        return COMMAND_LIST.contains(input);
    }

    public static String parserManager(String input) {
        return parser(input);
    }
}
