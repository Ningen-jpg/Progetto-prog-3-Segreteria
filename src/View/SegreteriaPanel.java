package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SegreteriaPanel extends JPanel {
    private JButton segreteriaButton = new JButton("Segreteria");
    private JFrame loginFrame;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private static JFrame segreteriaFrame;

    public SegreteriaPanel() {
        setLayout(new BorderLayout(20, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel leftPanel = new JPanel(new GridBagLayout());
        segreteriaButton.setFont(new Font("Arial", Font.PLAIN, 14));
        segreteriaButton.setPreferredSize(new Dimension(150, 40));
        leftPanel.add(segreteriaButton);
        add(leftPanel, BorderLayout.WEST);
    }

    public void openSegreteriaFrame() {
        if (segreteriaFrame == null) {
            segreteriaFrame = new JFrame("Login Segreteria");
            segreteriaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            segreteriaFrame.setSize(400, 300);
            segreteriaFrame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            JLabel titleLabel = new JLabel("Area Segreteria", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(titleLabel, BorderLayout.NORTH);

            JPanel loginPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridx = 0; gbc.gridy = 0;
            loginPanel.add(new JLabel("ID Segreteria:"), gbc);
            gbc.gridx = 1;
            idField = new JTextField(15);
            loginPanel.add(idField, gbc);

            gbc.gridx = 0; gbc.gridy = 1;
            loginPanel.add(new JLabel("Password:"), gbc);
            gbc.gridx = 1;
            passwordField = new JPasswordField(15);
            loginPanel.add(passwordField, gbc);

            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            loginButton = new JButton("Login");
            loginPanel.add(loginButton, gbc);

            panel.add(loginPanel, BorderLayout.CENTER);
            segreteriaFrame.add(panel);
        }
        segreteriaFrame.setVisible(true);
    }

    // Getters per i campi login
    public String getId() {
        return idField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Listener da aggiungere esternamente
    public void addAccessButtonListener(ActionListener l) {
        segreteriaButton.addActionListener(l);
    }

    public void addLoginButtonListener(ActionListener l) {
        loginButton.addActionListener(l);
    }

    // in SegreteriaPanel.java
    public JFrame getLoginFrame() {
        return segreteriaFrame;
    }

}
