package davidos;

import java.util.Scanner;
import java.util.Stack;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Exceptions
import java.io.IOException;

/*
 * DSLInterpreter improvements:
 * - Robust parseVarName / parseVariable
 * - Load variables at startup from variables.txt
 * - Write/update variables on let automatically
 */

public class DSLInterpreter {
    static Shell shell = new Shell();
    static Computer computer = new Computer();

    static Scanner scanner = new Scanner(System.in);
    static boolean file_created = false;
    private static final Path VAR_FILE = Paths.get("variables.txt");

    // Load saved variables at class initialization
    static {
        loadVariables();
    }

    DSLInterpreter(){ }

    static String detect(String command, String subject){
        String result = "";
        int count = 1;
        while(!command.equals("") && count == 1){
            switch(command){
                case "cp" -> {
                    try {
                        double r = computer.compute(subject);
                        result += r;
                        System.out.println(r);
                    } catch (Exception e) {
                        System.out.println("Error computing: " + e.getMessage());
                    }
                }
                case "st" -> store(subject);
                case "var" -> result += computer.getVar(subject);
                case "check" -> System.out.printf("%s exists: %b%n" , subject, checkVar(subject));
                case "let"  -> result += computer.let(subject);
                case "clear" -> computer.clear(subject);
                case "parse" -> parseFile(subject);
                case "set" -> set(subject);
                case "graph" -> graph(subject);
                case "help" -> help(subject);
                case "quit" -> quit();
                default -> System.out.println(command + " is not a command");
            }
            count--;
        }
        return result;
    }

    // store: simple variable persistence using writeFile
    private static void store(String subject){
        String var_name = parseVarName(subject);
        double var = parseVariable(subject);
        createFile();
        if(checkVar(var_name)){
            System.out.println("Already exists");
        }else{
            writeFile(var_name, var);
            Computer.variables.put(var_name, var);
        }
    }

    private static void createFile() {
        if(!file_created){
            try {
                File variables = new File(VAR_FILE.toString());
                if (variables.createNewFile()) {
                    // created
                }
            }catch(IOException e) {
                System.out.println("An error occurred creating variables file.");
                e.printStackTrace();
            }
            file_created = true;
        }
    }

    private static boolean checkVar(String var_name) {
        if (!Files.exists(VAR_FILE)) return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(VAR_FILE.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String name = parseVarName(line);
                if (name.equals(var_name)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return false;
    }

    // append var_name = var
    private static void writeFile(String var_name, double var){
        try{
            FileWriter myWriter = new FileWriter(VAR_FILE.toString(), true);
            myWriter.write(var_name + " = " + var + "\n");
            myWriter.close();
        }catch(IOException e) {
            System.out.println("An error occurred writing variable file.");
            e.printStackTrace();
        }
    }

    // Public helpers used by Computer.let to persist changes safely
    public static void writeOrUpdateVarFile(String varName, double value) {
        createFile();
        try {
            // Read all lines and update or append
            java.util.List<String> lines = Files.readAllLines(VAR_FILE);
            boolean updated = false;
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String name = parseVarName(line);
                if (name.equals(varName)) {
                    lines.set(i, varName + " = " + value);
                    updated = true;
                    break;
                }
            }
            if (!updated) lines.add(varName + " = " + value);
            Files.write(VAR_FILE, lines);
        } catch (IOException e) {
            System.err.println("Error updating variables file: " + e.getMessage());
        }
    }

    public static void removeVarFromFile(String varName) {
        if (!Files.exists(VAR_FILE)) return;
        try {
            java.util.List<String> lines = Files.readAllLines(VAR_FILE);
            java.util.List<String> out = new java.util.ArrayList<>();
            for (String line : lines) {
                String name = parseVarName(line);
                if (!name.equals(varName)) out.add(line);
            }
            Files.write(VAR_FILE, out);
        } catch (IOException e) {
            System.err.println("Error removing var from file: " + e.getMessage());
        }
    }

    public static void clearVarsFile() {
        try {
            Files.deleteIfExists(VAR_FILE);
            Files.createFile(VAR_FILE);
        } catch (IOException e) {
            System.err.println("Error clearing vars file: " + e.getMessage());
        }
    }

    // Load saved variables into Computer.variables at startup
    private static void loadVariables() {
        if (!Files.exists(VAR_FILE)) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(VAR_FILE.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String name = parseVarName(line);
                try {
                    double value = parseVariable(line);
                    Computer.variables.put(name, value);
                } catch (Exception e) {
                    // skip malformed lines
                }
            }
        } catch (IOException e) {
            // no-op
        }
    }

    // Robust parseVarName: returns variable name from forms:
    // "x = 3", "x=3", "let x = 3", "x 3", "x= 3"
    public static String parseVarName(String subject) {
        if (subject == null) return "";
        // Remove any leading command token like "let "
        subject = subject.trim();
        // If contains '=', take left side
        int eqIndex = subject.indexOf('=');
        String left = (eqIndex >= 0) ? subject.substring(0, eqIndex).trim() : subject;
        // Split by whitespace and take last token (handles "let x", or "x 3")
        String[] parts = left.split("\\s+");
        String candidate = parts[parts.length - 1];
        // Now extract identifier characters
        Matcher m = Pattern.compile("([A-Za-z_][A-Za-z0-9_]*)").matcher(candidate);
        if (m.find()) return m.group(1);
        // Fallback: return candidate unchanged
        return candidate;
    }

    // parseVariable: returns numeric value on RHS or evaluates expression (supports computing expressions).
    // Accepts: "x = 3.14", "x=2+3", "let x 2+3", "x 2"
    public static double parseVariable(String subject) {
        if (subject == null) throw new IllegalArgumentException("No subject provided");
        subject = subject.trim();
        // Remove optional leading command (like "let ") if present
        if (subject.startsWith("let ")) subject = subject.substring(4).trim();

        // If contains '=', take RHS
        int eqIndex = subject.indexOf('=');
        String rhs;
        if (eqIndex >= 0) {
            rhs = subject.substring(eqIndex + 1).trim();
        } else {
            // else, split on whitespace and take everything after first token (the var name)
            String[] toks = subject.split("\\s+", 2);
            if (toks.length >= 2) rhs = toks[1].trim();
            else throw new IllegalArgumentException("No value for variable");
        }

        // If RHS references other variables or is an expression, evaluate with Computer.compute
        try {
            return computer.compute(rhs);
        } catch (Exception e) {
            // Try parse as simple number
            try {
                return Double.parseDouble(rhs);
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Unable to parse variable value: " + rhs);
            }
        }
    }

    // parseFile: unchanged aside from using Shell.processInput which uses DSLParser
    private static void parseFile(String filename){
        File file = new File(filename);
        String filePath =  file.getAbsolutePath();
        System.out.println(filePath);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println("Filepath:" + filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String temp_line;
            while ((temp_line = reader.readLine()) != null) {
                System.out.println(Shell.processInput(temp_line));
            }
        } catch (IOException e) {
            System.out.println("File doesn't exist");
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Other small helpers
    private static void set(String subject){ System.out.println("Parser out of order"); }
    private static void graph(String subject){ System.out.println("Grapher not working. Subject is: " + subject); }
    private static void help(String command){ System.out.println("Helping"); }
    private static void quit(){ System.out.println("Have a good day!"); Computer.clearAll(); }
}
