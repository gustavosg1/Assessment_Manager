package AssesmentManager.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotoQuadrat extends JButton {

    String text;
    int x, y, width, height;

    public BotoQuadrat(String text, int x, int y, int width, int height) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        setBounds(x, y, width, height);
        setText(text);
        setFocusPainted(false);
        setBorderPainted(true);
        setBackground(new Color(228, 230, 235));
        setFont(new Font("Noto Sans", Font.BOLD, 12));
        setBorder(BorderFactory.createBevelBorder(0)); // Add 3D effect

        //effectes mouse clic
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setBorder(BorderFactory.createBevelBorder(1)); // Pressed effect
            }

            public void mouseReleased(MouseEvent e) {
                setBorder(BorderFactory.createBevelBorder(0)); // Pressed effect
            }

            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(208, 213, 219)); // Muda a cor quando o mouse entra
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(228, 230, 235)); // Volta à cor original quando o mouse sai
            }

        });

    }

}