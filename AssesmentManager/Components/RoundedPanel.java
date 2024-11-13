package AssesmentManager.Components;

import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {

    private JLabel label;

    public RoundedPanel() {
        setOpaque(false); // Para asegurar que solo se pinta la parte redondeada
        setPreferredSize(new Dimension(200, 200)); // Establece el tamaño deseado

        // Añadimos el JLabel que interpretará el HTML
        label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER); // Centrar el texto
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Merriweather", Font.BOLD, 40));
        label.setForeground(new Color(55, 125, 34)); // Color del texto
        setLayout(new BorderLayout()); // Usar BorderLayout para centrar el JLabel
        add(label, BorderLayout.CENTER); // Añadir el JLabel al panel
    }

    public void setText(String text) {
        label.setText(text); // Delegar la tarea al JLabel, que soporta HTML
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Pintar el fondo redondeado
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 300, 300); // Radio de redondeo

        g2.dispose();
    }
}