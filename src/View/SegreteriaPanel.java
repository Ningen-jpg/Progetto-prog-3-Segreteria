package View;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SegreteriaPanel {
    public static void pannelloSegreteria(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout(20, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        // Pannello sinistro per il pulsante Model.Segreteria
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        JButton segreteriaButton = new JButton("Segreteria");
        segreteriaButton.setFont(new Font("Arial", Font.PLAIN, 14));
        segreteriaButton.setPreferredSize(new Dimension(150, 40));
        segreteriaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openSegreteriaFrame();
            }
        });
        leftPanel.add(segreteriaButton);
        buttonPanel.add(leftPanel, BorderLayout.WEST);
    }

    public static void openSegreteriaFrame() {
        if (segreteriaFrame == null) {
            segreteriaFrame = new JFrame("Login Segreteria");
            segreteriaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            segreteriaFrame.setSize(400, 300);
            segreteriaFrame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            // Titolo
            JLabel titleLabel = new JLabel("Area Segreteria", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            panel.add(titleLabel, BorderLayout.NORTH);

            // Pannello per il login
            JPanel loginPanel = new JPanel();
            loginPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campo ID
            gbc.gridx = 0;
            gbc.gridy = 0;
            loginPanel.add(new JLabel("ID Segreteria:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            JTextField idField = new JTextField(15);
            loginPanel.add(idField, gbc);

            // Campo password
            gbc.gridx = 0;
            gbc.gridy = 1;
            loginPanel.add(new JLabel("Password:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            JPasswordField passwordField = new JPasswordField(15);
            loginPanel.add(passwordField, gbc);

            // Pulsante login
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String id = idField.getText();
                    String password = new String(passwordField.getPassword());

                    if (loginSegreteria(id, password)) {
                        // Login riuscito, mostra le funzionalità della segreteria
                        showSegreteriaFunctionality(id);
                    }
                }
            });
            loginPanel.add(loginButton, gbc);

            panel.add(loginPanel, BorderLayout.CENTER);
            segreteriaFrame.add(panel);
        }
        segreteriaFrame.setVisible(true);
    }
}
