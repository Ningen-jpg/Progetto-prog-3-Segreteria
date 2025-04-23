package View;

import Model.Segreteria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;

public class SegreteriaPanel extends JPanel {
    private JButton segreteriaButton = new JButton("Segreteria");
    private JButton studentiButton;
    JPanel buttonPanel = new JPanel();
    JPanel panel = new JPanel();
    JButton infoStudenteButton;
    JButton esitiTestButton;
    JButton esitiCorsoButton;
    JButton logoutButton;
    JPanel logoutPanel;

    public SegreteriaPanel() {
        setLayout(new BorderLayout(20, 0));
        JPanel leftPanel = new JPanel(new GridBagLayout());
        segreteriaButton.setFont(new Font("Arial",Font.PLAIN,14));
        segreteriaButton.setPreferredSize(new Dimension(150,40));
        leftPanel.add(segreteriaButton);
        add(leftPanel, BorderLayout.WEST);
    }

    public void showGestioneStudentiView() {
        // Creo un nuovo pannello per le funzionalità

        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titolo
        JLabel titleLabel = new JLabel("Funzionalità Segreteria", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Pannello per i pulsanti delle funzionalità

        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // Pulsante Gestione Studenti
        studentiButton = new JButton("Gestione Studenti");

        infoStudenteButton = new JButton("Visualizza informazioni studente");

        esitiTestButton = new JButton("Visualizza esiti corso");

        esitiCorsoButton = new JButton("Visualizza esiti corso di Laurea");

        logoutButton = new JButton("Disconnetti");

        logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    }

    public void mostraInfo(String testo, String titolo){

        JTextArea textArea = new JTextArea(testo);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, textArea, titolo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostraEsitiEsame(String testo, String titolo){
        JTextArea textArea = new JTextArea(testo);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, titolo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostraEsitiCorso(String testo, String titolo){
        JTextArea textArea = new JTextArea(testo);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(this, scrollPane, titolo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public JButton getStudentiButton() { return studentiButton; }

    public JButton getSegreteriaButton() { return segreteriaButton; }

    public JPanel getButtonPanel() { return buttonPanel; }

    public JPanel getPanel() { return panel; }

    public JButton getInfoStudenteButton(){ return infoStudenteButton; }

    public JButton getEsitiTestButton() { return esitiTestButton; }

    public JButton getEsitiCorsoButton(){ return esitiCorsoButton; }

    public JButton getLogoutButton() { return logoutButton; }

    public JPanel getLogoutPanel() { return logoutPanel; }
}
