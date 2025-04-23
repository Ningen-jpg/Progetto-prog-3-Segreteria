package View;
import javax.swing.*;
import java.awt.*;

public class StudentePanel extends JPanel {
    private JButton studenteButton;

    public StudentePanel() {
        // Imposta il layout del pannello
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);

        // Creazione del pulsante
        studenteButton = new JButton("Studente");
        studenteButton.setFont(new Font("Arial", Font.PLAIN, 14));
        studenteButton.setPreferredSize(new Dimension(150, 40));

        // Aggiungi il pulsante al pannello
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(studenteButton, gbc);
    }

    public JButton getStudenteButton() {
        return studenteButton;
    }
}
