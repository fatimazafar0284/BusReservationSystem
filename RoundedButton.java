package com.busreservation.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoundedButton extends JButton {

    private Color hoverColor;
    private Color baseColor;
    private boolean hovered = false;

    public RoundedButton(String text, Color color) {
        super(text);
        this.baseColor  = color;
        this.hoverColor = color.darker();
        setup();
    }

    public RoundedButton(String text) {
        this(text, Theme.PRIMARY);
    }

    private void setup() {
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(Theme.FONT_BTN);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setBackground(baseColor);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(isEnabled() ? (hovered ? hoverColor : baseColor) : Theme.BORDER);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.RADIUS, Theme.RADIUS);
        g2.dispose();
        super.paintComponent(g);
    }

    public void setColors(Color base, Color hover) {
        this.baseColor  = base;
        this.hoverColor = hover;
        setBackground(base);
        repaint();
    }
}