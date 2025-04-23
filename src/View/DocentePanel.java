package View;
import javax.swing.*;
import java.awt.*;

public class DocentePanel extends JPanel{
    private JButton docenteButton;

    public DocentePanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 0, 5);

        // Creazione del pulsante
        docenteButton = new JButton("Docente");
        docenteButton.setFont(new Font("Arial", Font.PLAIN, 14));
        docenteButton.setPreferredSize(new Dimension(150, 40));

        // Aggiungi il pulsante al pannello
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(docenteButton, gbc);
    }

    public JButton getDocenteButton() {
        return docenteButton;
    }

}
