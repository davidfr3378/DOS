import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * @author David
 */

public class TerminalUI {
    private JTextArea terminalArea;
    private JTextField inputField;

    public TerminalUI() {
        // Frame setup
        JFrame frame = new JFrame("DOS Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // Colors
        Color bgColor = new Color(15, 15, 15);
        Color fgColor = new Color(0, 220, 120);
        Color borderColor = new Color(40, 40, 40);

        // Terminal output area
        terminalArea = new JTextArea();
        terminalArea.setEditable(false);
        terminalArea.setBackground(bgColor);
        terminalArea.setForeground(fgColor);
        terminalArea.setCaretColor(fgColor);
        terminalArea.setFont(new Font("Consolas", Font.PLAIN, 15));
        terminalArea.setLineWrap(true);
        terminalArea.setWrapStyleWord(true);
        terminalArea.setMargin(new Insets(10, 10, 10, 10));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(terminalArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input panel (for subtle separation)
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(bgColor);
        inputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, borderColor));

        // Input field
        inputField = new JTextField();
        inputField.setBackground(bgColor);
        inputField.setForeground(fgColor);
        inputField.setCaretColor(fgColor);
        inputField.setFont(new Font("Consolas", Font.PLAIN, 15));
        inputField.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        // Handle enter key
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText().trim();
                if (!command.isEmpty()) {
                    terminalArea.append("> " + command + "\n");
                    try {
                        String output = Shell.processInput(command);
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

        inputPanel.add(inputField, BorderLayout.CENTER);
        frame.add(inputPanel, BorderLayout.SOUTH);

        // Show frame
        frame.setVisible(true);
        inputField.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TerminalUI());
    }
}
