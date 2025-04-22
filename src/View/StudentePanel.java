package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        studenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openStudenteFrame();
            }
        });

        // Aggiungi il pulsante al pannello
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(studenteButton, gbc);
    }

    private void openStudenteFrame() {
        // Aggiungi qui la logica per aprire la finestra dello studente
    }
}
