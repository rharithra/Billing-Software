package com.billingsoftware.ui;

import javax.swing.*;
import java.awt.*;
public class PromptPasswordField extends JPasswordField{
    private String placeholder;

    public PromptPasswordField(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getPassword().length == 0 && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            g2.setColor(Color.GRAY);
            g2.drawString(placeholder, 10, getHeight() / 2 + getFont().getSize() / 2 - 3);
            g2.dispose();
        }
    }
}
