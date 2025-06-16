package davidos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/*
 * @author unknown
 */


public class TerminalUI {
    private JTextArea terminalArea;
    private JTextField inputField;

    public TerminalUI() {
        // Create the frame
        JFrame frame = new JFrame("DOS Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        // Terminal output area
        terminalArea = new JTextArea();
        terminalArea.setEditable(false);
        terminalArea.setBackground(Color.BLACK);
        terminalArea.setForeground(Color.GREEN);
        terminalArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        terminalArea.setLineWrap(true);
        terminalArea.setWrapStyleWord(true);

        // Add scroll bar
        JScrollPane scrollPane = new JScrollPane(terminalArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input field
        inputField = new JTextField();
        inputField.setBackground(Color.BLACK);
        inputField.setForeground(Color.GREEN);
        inputField.setCaretColor(Color.GREEN);
        inputField.setFont(new Font("Consolas", Font.PLAIN, 16));

        // Handle enter key
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText().trim();
                if (!command.isEmpty()) {
                    terminalArea.append("> " + command + "\n");
                    try {
                        // Call your backend logic here
                        String output = Shell.processInput(command); // Update this line if needed
                        if (output != null && !output.isEmpty()) {
                            terminalArea.append(output + "\n");
                        }
                    } catch (Exception ex) {
                        terminalArea.append("Error: " + ex.getMessage() + "\n");
                    }
                    inputField.setText("");
                }
            }
        });

        frame.add(inputField, BorderLayout.SOUTH);

        // Make visible
        frame.setVisible(true);
        inputField.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TerminalUI());
    }
}