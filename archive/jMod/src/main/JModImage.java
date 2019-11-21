package main;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class JModImage extends JLabel {
    ImageIcon icon = null;
    public JModImage() {
        super();
    }
    
    // TODO ?is there support for .png?
    
    public JModImage(String text, int x, int y) {
        super();
        icon = new ImageIcon("./grafix/" + text);
        this.setIcon(icon);
        this.setLocation(x, y);
        this.setSize(icon.getIconWidth(), icon.getIconHeight());
    }
}
