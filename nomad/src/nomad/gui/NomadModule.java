package nomad.gui;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NomadModule extends JPanel {

	private JLabel nameLabel = null;

    public NomadModule() {
    	super();
        setLayout(null);
    	setBorder(BorderFactory.createRaisedBevelBorder());

    	nameLabel = new JLabel("");
        
        nameLabel.setLocation(3,0);
        nameLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(nameLabel);
    }
    
    public void setNameLabel(String name, int width) {
        nameLabel.setSize(width, 16);
    	nameLabel.setText(name);
    }
}
