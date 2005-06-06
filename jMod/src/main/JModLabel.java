package main;

import java.awt.Font;

import javax.swing.JLabel;

public class JModLabel extends JLabel {
    public JModLabel() {
        super();
    }
    
    public JModLabel(String text, int x, int y) {
        super();
        this.setFont(new Font("Dialog", Font.PLAIN, 10));
        this.setSize(text.length()*10,16);
        this.setLocation(x-1, y-3);
        this.setText(text);
    }
}
